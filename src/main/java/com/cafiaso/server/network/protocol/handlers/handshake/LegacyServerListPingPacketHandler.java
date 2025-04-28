package com.cafiaso.server.network.protocol.handlers.handshake;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.registry.ConnectionRegistry;
import com.cafiaso.server.network.protocol.PacketHandler;
import com.cafiaso.server.network.protocol.packets.client.handshake.LegacyServerListPingPacket;
import jakarta.inject.Inject;

public class LegacyServerListPingPacketHandler implements PacketHandler<LegacyServerListPingPacket> {

    private final ConnectionRegistry connectionRegistry;

    @Inject
    public LegacyServerListPingPacketHandler(ConnectionRegistry connectionRegistry) {
        this.connectionRegistry = connectionRegistry;
    }

    @Override
    public void handle(LegacyServerListPingPacket packet, Connection connection) {
        // Legacy server list pings are not supported by the server
        connectionRegistry.closeConnection(connection);
    }
}
