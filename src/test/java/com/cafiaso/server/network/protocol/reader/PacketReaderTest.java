package com.cafiaso.server.network.protocol.reader;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.processor.PacketProcessor;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PacketReaderTest {

    @Test
    void read_OK() throws IOException {
        byte[] data = new byte[]{0x03, 0x01, 0x02, 0x03};

        Connection connection = new MockConnection(data);
        PacketProcessor processor = mock(PacketProcessor.class);

        PacketReader reader = new PacketReader(connection, processor);

        byte[] expectedData = new byte[]{0x01, 0x02, 0x03};
        FriendlyBuffer bodyBuffer = new FriendlyBuffer(expectedData);

        reader.read();

        verify(processor).process(3, bodyBuffer, connection);
    }

    @Test
    void read_ShouldThrowIllegalStateException_WhenConnectionIsClosed() {
        Connection connection = mock(Connection.class);
        PacketProcessor processor = mock(PacketProcessor.class);

        PacketReader reader = new PacketReader(connection, processor);

        when(connection.isOpen()).thenReturn(false); // Simulate closed connection

        IllegalStateException exception = assertThrowsExactly(IllegalStateException.class, reader::read);
        assertEquals("Connection is closed", exception.getMessage());
    }

    @Test
    void read_ShouldThrowIOException_WhenEndOfStreamIsReached() throws IOException {
        Connection connection = mock(Connection.class);
        PacketProcessor processor = mock(PacketProcessor.class);

        PacketReader reader = new PacketReader(connection, processor);

        when(connection.isOpen()).thenReturn(true);
        when(connection.read(any(FriendlyBuffer.class))).thenReturn(-1); // Simulate the end of stream

        IOException exception = assertThrowsExactly(IOException.class, reader::read);
        assertEquals("End of stream reached", exception.getMessage());
    }

    private static class MockConnection extends Connection {

        private final byte[] data;

        public MockConnection(byte[] data) {
            this.data = data;
        }

        @Override
        public int read(FriendlyBuffer buffer) {
            buffer.writeBytes(data);

            return data.length;
        }

        @Override
        public void write(FriendlyBuffer buffer) {

        }

        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public void close() {
            // No-op
        }
    }
}
