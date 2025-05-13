package com.cafiaso.server.network.protocol.processor;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.ConnectionState;
import com.cafiaso.server.network.connection.ConnectionState.PacketRegistration;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.PacketHandler;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.packets.client.PacketDeserializer;
import com.google.inject.Injector;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PacketProcessorTest {

    @Test
    void process_OK() throws IOException {
        byte[] body = new byte[]{0x00, 0x01, 0x02, 0x03}; // 2 extra bytes
        FriendlyBuffer bodyBuffer = new FriendlyBuffer(body);

        Connection connection = mock(Connection.class);

        ConnectionState state = mock(ConnectionState.class);
        when(state.<MockPacket>getPacket(0x00)).thenReturn(Optional.of(new PacketRegistration<>(new MockPacket.Deserializer(), MockPacketHandler.class)));

        when(connection.getState()).thenReturn(state);

        Injector injector = mock(Injector.class);

        MockPacketHandler handler = mock(MockPacketHandler.class);
        when(injector.getInstance(MockPacketHandler.class)).thenReturn(handler);

        PacketProcessor processor = new PacketProcessor(injector);

        int remainingBytes = processor.process(4, bodyBuffer, connection);

        assertEquals(2, remainingBytes);

        MockPacket packet = new MockPacket(1);

        verify(handler).handle(packet, connection);
    }

    @Test
    void process_ShouldThrowUnknownPacketException_WhenPacketIsUnknown() {
        byte[] body = new byte[]{0x00, 0x01};
        FriendlyBuffer bodyBuffer = new FriendlyBuffer(body);

        Connection connection = mock(Connection.class);

        ConnectionState state = mock(ConnectionState.class);
        when(state.toString()).thenReturn("HANDSHAKE");
        when(state.<MockPacket>getPacket(0x00)).thenReturn(Optional.empty());

        when(connection.getState()).thenReturn(state);

        Injector injector = mock(Injector.class);

        PacketProcessor processor = new PacketProcessor(injector);

        UnknownPacketException exception = assertThrowsExactly(UnknownPacketException.class, () -> processor.process(3, bodyBuffer, connection));
        assertEquals("Received unknown packet with id 0x00. Please check the packet id mapping for state HANDSHAKE.", exception.getMessage());
    }

    private record MockPacket(int payload) {

        public static class Deserializer implements PacketDeserializer<MockPacket> {

            @Override
            public int getId() {
                return 0x00;
            }

            @Override
            public MockPacket deserialize(FriendlyBuffer in) throws IOException {
                int payload = in.read(DataTypes.VAR_INT);

                return new MockPacket(payload);
            }
        }
    }

    private static class MockPacketHandler implements PacketHandler<MockPacket> {

        @Override
        public void handle(MockPacket packet, Connection connection) {
            // No-op
        }
    }
}
