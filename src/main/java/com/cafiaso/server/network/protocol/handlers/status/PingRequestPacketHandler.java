package com.cafiaso.server.network.protocol.handlers.status;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.registry.ConnectionRegistry;
import com.cafiaso.server.network.protocol.PacketHandler;
import com.cafiaso.server.network.protocol.dispatcher.PacketDispatcher;
import com.cafiaso.server.network.protocol.packets.client.status.PingRequestPacket;
import com.cafiaso.server.network.protocol.packets.server.status.PongResponsePacket;
import jakarta.inject.Inject;

import java.io.IOException;

public class PingRequestPacketHandler implements PacketHandler<PingRequestPacket> {

    private final PacketDispatcher packetDispatcher;
    private final ConnectionRegistry connectionRegistry;

    @Inject
    public PingRequestPacketHandler(PacketDispatcher packetDispatcher, ConnectionRegistry connectionRegistry) {
        this.packetDispatcher = packetDispatcher;
        this.connectionRegistry = connectionRegistry;
    }

    @Override
    public void handle(PingRequestPacket packet, Connection connection) throws IOException {
        long payload = packet.timestamp();

        // Send a response back to the client using the same timestamp
        packetDispatcher.dispatch(new PongResponsePacket(payload), connection);

        connectionRegistry.closeConnection(connection); // Close the connection after sending the response
    }
}
