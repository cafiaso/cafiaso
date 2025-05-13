package com.cafiaso.server.network.protocol.handlers.handshake;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.registry.ConnectionRegistry;
import com.cafiaso.server.network.protocol.packets.client.handshake.LegacyServerListPingPacket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LegacyServerListPingPacketHandlerTest {

    @Mock
    private ConnectionRegistry connectionRegistry;

    @InjectMocks
    private LegacyServerListPingPacketHandler handler;

    @Test
    void handle_OK() {
        LegacyServerListPingPacket packet = mock(LegacyServerListPingPacket.class);

        Connection connection = mock(Connection.class);

        handler.handle(packet, connection);

        verify(connectionRegistry).closeConnection(connection);
    }
}
