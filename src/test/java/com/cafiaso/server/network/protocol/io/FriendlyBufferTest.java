package com.cafiaso.server.network.protocol.io;

import com.cafiaso.server.network.protocol.DataType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FriendlyBufferTest {

    @Test
    void writeByte_OK() {
        FriendlyBuffer buffer = new FriendlyBuffer();
        buffer.writeByte((byte) 0x42);
        buffer.flip();

        assertEquals((byte) 0x42, buffer.readByte());
    }

    @Test
    void writeLong_OK() {
        FriendlyBuffer buffer = new FriendlyBuffer();
        buffer.writeLong(0x123456789ABCDEFL);
        buffer.flip();

        assertEquals(0x123456789ABCDEFL, buffer.readLong());
    }

    @Test
    void writeUnsignedShort_OK() {
        FriendlyBuffer buffer = new FriendlyBuffer();
        buffer.writeUnsignedShort(0xABCD);
        buffer.flip();

        assertEquals(0xABCD, buffer.readUnsignedShort());
    }

    @Test
    void writeBytes_OK() {
        byte[] data = {1, 2, 3};
        FriendlyBuffer buffer = new FriendlyBuffer();
        buffer.writeBytes(data);
        buffer.flip();

        assertArrayEquals(data, buffer.readBytes(3));
    }

    @Test
    void writeDataType_OK() throws IOException {
        @SuppressWarnings("unchecked")
        DataType<Integer> mockType = mock(DataType.class);
        FriendlyBuffer buffer = new FriendlyBuffer();

        // write
        buffer.write(42, mockType);
        verify(mockType).write(42, buffer);

        // read
        when(mockType.read(buffer)).thenReturn(42);
        int result = buffer.read(mockType);
        assertEquals(42, result);
    }

    @Test
    void writeFriendlyBuffer_OK() {
        FriendlyBuffer source = new FriendlyBuffer();
        source.writeBytes(new byte[]{9, 8});
        source.flip();

        FriendlyBuffer target = new FriendlyBuffer();
        target.write(source);
        target.flip();

        assertArrayEquals(new byte[]{9, 8}, target.readBytes(2));
    }

    @Test
    void copy_OK() {
        FriendlyBuffer buffer = new FriendlyBuffer();
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        buffer.flip();

        buffer.readByte(); // advance position

        FriendlyBuffer copy = buffer.copy(2);

        assertArrayEquals(new byte[]{2, 3}, copy.toByteArray());
    }

    @Test
    void hasRemainingBytes_OK() {
        FriendlyBuffer buffer = new FriendlyBuffer();
        buffer.writeBytes(new byte[]{1, 2});
        buffer.flip();

        assertTrue(buffer.hasRemainingBytes());
        assertEquals(2, buffer.getRemainingBytes());

        buffer.readByte();
        assertTrue(buffer.hasRemainingBytes());
        assertEquals(1, buffer.getRemainingBytes());

        buffer.readByte();
        assertFalse(buffer.hasRemainingBytes());
    }

    @Test
    void flip_OK() {
        FriendlyBuffer buffer = new FriendlyBuffer();
        buffer.writeBytes(new byte[]{1, 2});
        assertEquals(2, buffer.getPosition());

        buffer.flip();
        assertEquals(0, buffer.getPosition());
    }

    @Test
    void mark_OK() {
        FriendlyBuffer buffer = new FriendlyBuffer();
        buffer.writeBytes(new byte[]{10, 20, 30});
        buffer.flip();

        buffer.readByte(); // 10
        buffer.mark();
        buffer.readByte(); // 20

        buffer.reset();
        assertEquals(20, buffer.readByte());
    }

    @Test
    void compact_OK() {
        FriendlyBuffer buffer = new FriendlyBuffer();
        buffer.writeBytes(new byte[]{1, 2, 3});
        buffer.flip();

        buffer.readByte(); // Advance position

        buffer.compact(); // Should move the remaining bytes to the beginning

        assertEquals(2, buffer.getPosition()); // Position should be at the end of the remaining bytes
    }

    @Test
    void toByteArray_OK() {
        byte[] data = {1, 2, 3};
        FriendlyBuffer buffer = new FriendlyBuffer();
        buffer.writeBytes(data);
        buffer.flip();

        assertArrayEquals(data, buffer.toByteArray());
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    void equals_OK() {
        FriendlyBuffer buf1 = new FriendlyBuffer();
        FriendlyBuffer buf2 = new FriendlyBuffer();

        buf1.writeBytes(new byte[]{1, 2, 3});
        buf2.writeBytes(new byte[]{1, 2, 3});

        assertEquals(buf1, buf2);

        assertEquals(buf1, buf1); // Same object
    }

    @Test
    void equals_ShouldReturnFalse_WhenBufferContentsAreDifferent() {
        FriendlyBuffer buf1 = new FriendlyBuffer();
        FriendlyBuffer buf2 = new FriendlyBuffer();

        buf1.writeBytes(new byte[]{1, 2, 3});
        buf2.writeBytes(new byte[]{4, 5, 6});

        assertNotEquals(buf1, buf2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenBufferSizesAreDifferent() {
        FriendlyBuffer buf1 = new FriendlyBuffer();
        FriendlyBuffer buf2 = new FriendlyBuffer();

        buf1.writeBytes(new byte[]{1, 2, 3});
        buf2.writeBytes(new byte[]{1, 2});

        assertNotEquals(buf1, buf2);
    }

    @SuppressWarnings("AssertBetweenInconvertibleTypes")
    @Test
    void equals_ShouldReturnFalse_WhenObjectIsNotFriendlyBuffer() {
        FriendlyBuffer buffer = new FriendlyBuffer();
        buffer.writeBytes(new byte[]{1, 2, 3});

        ByteBuffer otherBuffer = ByteBuffer.allocate(3);
        otherBuffer.put(new byte[]{1, 2, 3});

        assertNotEquals(buffer, otherBuffer);
    }
}
