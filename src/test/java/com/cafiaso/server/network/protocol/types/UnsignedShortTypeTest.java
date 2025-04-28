package com.cafiaso.server.network.protocol.types;

import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnsignedShortTypeTest {

    private static final byte[] SERIALIZED_VALUE = {0x63, (byte) 0xdd};
    private static final int VALUE = 25565;

    private static final UnsignedShortType DATA_TYPE = new UnsignedShortType();

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

    @Test
    void write_ShouldThrowException_WhenValueIsNegative() {
        FriendlyBuffer out = new FriendlyBuffer();

        int value = -1;

        IllegalArgumentException exception = assertThrowsExactly(IllegalArgumentException.class, () -> DATA_TYPE.write(value, out));
        assertEquals("Value out of range for unsigned short: -1", exception.getMessage());
    }

    @Test
    void write_ShouldThrowException_WhenValueIsGreaterThanMax() {
        FriendlyBuffer out = new FriendlyBuffer();

        int value = 65536;

        IllegalArgumentException exception = assertThrowsExactly(IllegalArgumentException.class, () -> DATA_TYPE.write(value, out));
        assertEquals("Value out of range for unsigned short: 65536", exception.getMessage());
    }
}
