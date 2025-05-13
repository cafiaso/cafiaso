package com.cafiaso.server.network.protocol.types;

import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LongDataTypeTest {

    private static final byte[] SERIALIZED_VALUE = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
    private static final long VALUE = 72623859790382856L;

    private static final LongDataType DATA_TYPE = new LongDataType();

    @Test
    void read_OK() {
        FriendlyBuffer in = new FriendlyBuffer(SERIALIZED_VALUE);

        long value = DATA_TYPE.read(in);

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
