package com.cafiaso.server.network.protocol.types;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;

/**
 * A {@link DataType} for VarInt values.
 * <p>
 * VarInt is a variable-length integer encoding scheme used in various protocols.
 * It allows for efficient storage of integers by using fewer bytes for smaller values.
 *
 * @see <a href="https://wiki.vg/Protocol#VarInt_and_VarLong">VarInt Type</a>
 */
public class VarIntDataType implements DataType<Integer> {

    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    @Override
    public Integer read(FriendlyBuffer in) {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = in.readByte();
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }

        return value;
    }

    @Override
    public void write(Integer value, FriendlyBuffer out) {
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                out.writeByte(value.byteValue());

                return;
            }

            out.writeByte((byte) ((value & SEGMENT_BITS) | CONTINUE_BIT));
            value >>>= 7;
        }
    }
}
