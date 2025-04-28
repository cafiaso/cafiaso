package com.cafiaso.server.network.protocol.handlers.status;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.registry.ConnectionRegistry;
import com.cafiaso.server.network.protocol.dispatcher.PacketDispatcher;
import com.cafiaso.server.network.protocol.packets.client.status.PingRequestPacket;
import com.cafiaso.server.network.protocol.packets.server.status.PongResponsePacket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PingRequestPacketHandlerTest {

    @Mock
    private PacketDispatcher packetDispatcher;

    @Mock
    private ConnectionRegistry connectionRegistry;

    @InjectMocks
    private PingRequestPacketHandler handler;

    @Test
    void handle_OK() throws IOException {
        PingRequestPacket packet = mock(PingRequestPacket.class);
        when(packet.timestamp()).thenReturn(123L);

        Connection connection = mock(Connection.class);

        handler.handle(packet, connection);

        verify(connectionRegistry).closeConnection(connection);

        PongResponsePacket pongResponsePacket = new PongResponsePacket(123L);
        verify(packetDispatcher).dispatch(pongResponsePacket, connection);
    }
}
