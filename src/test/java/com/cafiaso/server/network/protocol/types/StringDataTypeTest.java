package com.cafiaso.server.network.protocol.types;

import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class StringDataTypeTest {

    private static final byte[] SERIALIZED_VALUE = {0x0D, 'H', 'e', 'l', 'l', 'o', ',', ' ', 'W', 'o', 'r', 'l', 'd', '!'};
    private static final String VALUE = "Hello, World!";

    private static final StringDataType DATA_TYPE = new StringDataType();

    @Test
    void read_OK() throws IOException {
        FriendlyBuffer in = new FriendlyBuffer(SERIALIZED_VALUE);

        String value = DATA_TYPE.read(in);

        assertEquals(VALUE, value);
    }

    @Test
    void read_ShouldThrowException_WhenStringIsTooLong() {
        byte[] value1 = {(byte) 0xa0, (byte) 0x8d, 0x06}; // 100 000
        FriendlyBuffer in1 = new FriendlyBuffer(value1);

        IOException exception = assertThrowsExactly(IOException.class, () -> DATA_TYPE.read(in1));
        assertEquals(
                "String is too long. Waiting for maximum 98304 characters, received 100000.",
                exception.getMessage()
        );

        int maxLength = 100;
        StringDataType dataType = new StringDataType(maxLength);

        byte[] value2 = {(byte) 0x96, 0x01}; // 150
        FriendlyBuffer in2 = new FriendlyBuffer(value2);

        IOException exception2 = assertThrowsExactly(IOException.class, () -> dataType.read(in2));
        assertEquals(
                "String is too long. Waiting for maximum 100 characters, received 150.",
                exception2.getMessage()
        );
    }

    @Test
    void write_OK() throws IOException {
        FriendlyBuffer out = new FriendlyBuffer();

        DATA_TYPE.write(VALUE, out);

        out.flip();

        assertArrayEquals(SERIALIZED_VALUE, out.toByteArray());
    }
}
