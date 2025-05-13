package com.cafiaso.server.network.protocol.handlers.status;

import com.cafiaso.server.ServerIcon;
import com.cafiaso.server.configuration.ServerConfiguration;
import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.protocol.dispatcher.PacketDispatcher;
import com.cafiaso.server.network.protocol.packets.client.status.StatusRequestPacket;
import com.cafiaso.server.network.protocol.packets.server.status.StatusResponsePacket;
import com.cafiaso.server.player.Identity;
import com.cafiaso.server.player.PlayerManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static com.cafiaso.server.Server.MINECRAFT_VERSION;
import static com.cafiaso.server.Server.PROTOCOL_VERSION;
import static com.cafiaso.server.configuration.ServerConfiguration.DEFAULT_DESCRIPTION;
import static com.cafiaso.server.configuration.ServerConfiguration.DEFAULT_MAX_PLAYERS;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatusRequestPacketHandlerTest {

    @Mock
    private ServerConfiguration configuration;

    @Mock
    private ServerIcon icon;

    @Mock
    private PlayerManager playerManager;

    @Mock
    private PacketDispatcher packetDispatcher;

    @InjectMocks
    private StatusRequestPacketHandler handler;

    @Test
    void handle_OK() throws IOException {
        when(configuration.getMaxPlayers()).thenReturn(DEFAULT_MAX_PLAYERS);
        when(configuration.getDescription()).thenReturn(DEFAULT_DESCRIPTION);

        when(icon.get()).thenReturn(Optional.of("data:image/png;base64,AQID"));

        Identity player1 = new Identity("player1", UUID.randomUUID());
        Identity player2 = new Identity("player2", UUID.randomUUID());
        when(playerManager.getOnlinePlayers()).thenReturn(Arrays.asList(player1, player2));

        StatusRequestPacket packet = mock(StatusRequestPacket.class);
        Connection connection = mock(Connection.class);

        handler.handle(packet, connection);

        StatusResponsePacket statusResponsePacket = new StatusResponsePacket(
                "{\"favicon\":\"data:image/png;base64,AQID\",\"players\":{\"max\":%d,\"online\":2,\"sample\":[{\"name\":\"player1\",\"id\":\"%s\"},{\"name\":\"player2\",\"id\":\"%s\"}]},\"description\":{\"text\":\"%s\"},\"version\":{\"protocol\":%d,\"name\":\"%s\"}}"
                        .formatted(DEFAULT_MAX_PLAYERS, player1.id(), player2.id(), DEFAULT_DESCRIPTION, PROTOCOL_VERSION, MINECRAFT_VERSION)
        );

        verify(packetDispatcher).dispatch(statusResponsePacket, connection);
    }

    @Test
    void handle_ShouldNotThrowException_WhenNoFavicon() throws IOException {
        when(configuration.getMaxPlayers()).thenReturn(DEFAULT_MAX_PLAYERS);
        when(configuration.getDescription()).thenReturn(DEFAULT_DESCRIPTION);

        when(icon.get()).thenReturn(Optional.empty());

        Identity player1 = new Identity("player1", UUID.randomUUID());
        Identity player2 = new Identity("player2", UUID.randomUUID());
        when(playerManager.getOnlinePlayers()).thenReturn(Arrays.asList(player1, player2));

        StatusRequestPacket packet = mock(StatusRequestPacket.class);
        Connection connection = mock(Connection.class);

        handler.handle(packet, connection);

        StatusResponsePacket statusResponsePacket = new StatusResponsePacket(
                "{\"players\":{\"max\":%d,\"online\":2,\"sample\":[{\"name\":\"player1\",\"id\":\"%s\"},{\"name\":\"player2\",\"id\":\"%s\"}]},\"description\":{\"text\":\"%s\"},\"version\":{\"protocol\":%d,\"name\":\"%s\"}}"
                        .formatted(DEFAULT_MAX_PLAYERS, player1.id(), player2.id(), DEFAULT_DESCRIPTION, PROTOCOL_VERSION, MINECRAFT_VERSION)
        );

        verify(packetDispatcher).dispatch(statusResponsePacket, connection);
    }
}
