package com.cafiaso.server.network.protocol.types;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;

/**
 * A {@link DataType} for unsigned short values.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Type:Unsigned_Short">Unsigned Short Type</a>
 */
public class UnsignedShortType implements DataType<Integer> {

    @Override
    public Integer read(FriendlyBuffer in) {
        return in.readUnsignedShort();
    }

    @Override
    public void write(Integer value, FriendlyBuffer out) {
        if (value < 0 || value > 65535) {
            throw new IllegalArgumentException("Value out of range for unsigned short: %d".formatted(value));
        }

        out.writeUnsignedShort(value);
    }
}
