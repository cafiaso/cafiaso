package com.cafiaso.server.network.protocol.handlers.handshake;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.ConnectionState;
import com.cafiaso.server.network.connection.registry.ConnectionRegistry;
import com.cafiaso.server.network.protocol.packets.client.handshake.HandshakePacket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HandshakePacketHandlerTest {

    @Mock
    private ConnectionRegistry connectionRegistry;

    @InjectMocks
    private HandshakePacketHandler handler;

    @Test
    void handle_ShouldSetConnectionStateToStatus_WhenIntentIsStatus() {
        HandshakePacket packet = mock(HandshakePacket.class);
        when(packet.nextState()).thenReturn(HandshakePacket.Intention.STATUS);

        Connection connection = mock(Connection.class);

        handler.handle(packet, connection);

        verify(connection).setState(ConnectionState.STATUS);
    }

    @Test
    void handle_ShouldSetConnectionStateToLogin_WhenIntentIsLogin() {
        HandshakePacket packet = mock(HandshakePacket.class);
        when(packet.nextState()).thenReturn(HandshakePacket.Intention.LOGIN);

        Connection connection = mock(Connection.class);

        handler.handle(packet, connection);

        verify(connection).setState(ConnectionState.LOGIN);
    }

    @Test
    void handle_ShouldCloseConnection_WhenIntentIsTransfer() {
        HandshakePacket packet = mock(HandshakePacket.class);
        when(packet.nextState()).thenReturn(HandshakePacket.Intention.TRANSFER);

        Connection connection = mock(Connection.class);

        handler.handle(packet, connection);

        verify(connectionRegistry).closeConnection(connection);
    }
}
