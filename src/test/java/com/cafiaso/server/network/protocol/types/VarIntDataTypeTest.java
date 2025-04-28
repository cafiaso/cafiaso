package com.cafiaso.server.network.protocol.types;

import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VarIntDataTypeTest {

    private static final byte[] SERIALIZED_VALUE = {(byte) 0xdd, (byte) 0xc7, 0x01};
    private static final int VALUE = 25565;

    private static final VarIntDataType DATA_TYPE = new VarIntDataType();

    @Test
    void read_OK() {
        FriendlyBuffer in = new FriendlyBuffer(SERIALIZED_VALUE);

        int value = DATA_TYPE.read(in);

        assertEquals(VALUE, value);
    }

    @Test
    void write_OK() {
        FriendlyBuffer out = new FriendlyBuffer();

        DATA_TYPE.write(VALUE, out);

        out.flip();

        assertArrayEquals(SERIALIZED_VALUE, out.toByteArray());
    }
}
