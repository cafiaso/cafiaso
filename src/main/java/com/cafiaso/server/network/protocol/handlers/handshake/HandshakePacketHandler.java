package com.cafiaso.server.network.protocol.handlers.handshake;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.protocol.PacketHandler;
import com.cafiaso.server.network.connection.registry.ConnectionRegistry;
import com.cafiaso.server.network.connection.ConnectionState;
import com.cafiaso.server.network.protocol.packets.client.handshake.HandshakePacket;
import jakarta.inject.Inject;

public class HandshakePacketHandler implements PacketHandler<HandshakePacket> {

    private final ConnectionRegistry connectionRegistry;

    @Inject
    public HandshakePacketHandler(ConnectionRegistry connectionRegistry) {
        this.connectionRegistry = connectionRegistry;
    }

    @Override
    public void handle(HandshakePacket packet, Connection connection) {
        switch (packet.nextState()) {
            case STATUS -> connection.setState(ConnectionState.STATUS);
            case LOGIN -> connection.setState(ConnectionState.LOGIN);
            default -> connectionRegistry.closeConnection(connection); // The server does not support TRANSFER
        }
    }
}
