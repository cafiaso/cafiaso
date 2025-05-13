package com.cafiaso.server.network.protocol.handlers.status;

import com.cafiaso.server.ServerIcon;
import com.cafiaso.server.configuration.ServerConfiguration;
import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.protocol.dispatcher.PacketDispatcher;
import com.cafiaso.server.network.protocol.PacketHandler;
import com.cafiaso.server.network.protocol.packets.client.status.StatusRequestPacket;
import com.cafiaso.server.network.protocol.packets.server.status.StatusResponsePacket;
import com.cafiaso.server.player.Identity;
import com.cafiaso.server.player.PlayerManager;
import jakarta.inject.Inject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collection;

import static com.cafiaso.server.Server.MINECRAFT_VERSION;
import static com.cafiaso.server.Server.PROTOCOL_VERSION;

public class StatusRequestPacketHandler implements PacketHandler<StatusRequestPacket> {

    private final ServerConfiguration configuration;
    private final ServerIcon icon;
    private final PlayerManager playerManager;
    private final PacketDispatcher packetDispatcher;

    @Inject
    public StatusRequestPacketHandler(
            ServerConfiguration configuration,
            ServerIcon icon,
            PlayerManager playerManager,
            PacketDispatcher packetDispatcher
    ) {
        this.configuration = configuration;
        this.icon = icon;
        this.playerManager = playerManager;
        this.packetDispatcher = packetDispatcher;
    }

    @Override
    public void handle(StatusRequestPacket packet, Connection connection) throws IOException {
        // Version
        JSONObject version = new JSONObject()
                .put("name", MINECRAFT_VERSION)
                .put("protocol", PROTOCOL_VERSION);

        Collection<Identity> onlinePlayers = playerManager.getOnlinePlayers();

        // Sample player array
        JSONArray sample = new JSONArray();
        sample.putAll(
                onlinePlayers.stream()
                        .map(player ->
                                new JSONObject()
                                        .put("name", player.name())
                                        .put("id", player.id())
                        ).toList()
        );

        // Players
        JSONObject players = new JSONObject()
                .put("max", configuration.getMaxPlayers())
                .put("online", onlinePlayers.size())
                .put("sample", sample);

        // Description
        JSONObject description = new JSONObject();
        description.put("text", configuration.getDescription());

        JSONObject status = new JSONObject()
                .put("version", version)
                .put("players", players)
                .put("description", description);

        // Favicon
        icon.get().ifPresent(encodedIcon -> status.put("favicon", encodedIcon));

        packetDispatcher.dispatch(new StatusResponsePacket(status.toString()), connection);
    }
}
