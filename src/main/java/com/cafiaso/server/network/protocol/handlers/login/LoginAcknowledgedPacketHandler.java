package com.cafiaso.server.network.protocol.handlers.login;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.ConnectionState;
import com.cafiaso.server.network.protocol.PacketHandler;
import com.cafiaso.server.network.protocol.dispatcher.PacketDispatcher;
import com.cafiaso.server.network.protocol.packets.client.login.LoginAcknowledgedPacket;
import com.cafiaso.server.network.protocol.packets.server.configuration.ClientboundKnownPacks;
import com.cafiaso.server.pack.DataPack;
import jakarta.inject.Inject;

import java.io.IOException;

public class LoginAcknowledgedPacketHandler implements PacketHandler<LoginAcknowledgedPacket> {

    private final PacketDispatcher packetDispatcher;

    @Inject
    public LoginAcknowledgedPacketHandler(PacketDispatcher packetDispatcher) {
        this.packetDispatcher = packetDispatcher;
    }

    @Override
    public void handle(LoginAcknowledgedPacket packet, Connection connection) throws IOException {
        // The client has acknowledged the login success, we can now move to the configuration state
        connection.setState(ConnectionState.CONFIGURATION);

        // Send the known packs to the client
        packetDispatcher.dispatch(
                new ClientboundKnownPacks(
                        new DataPack[] {
                                DataPack.CORE
                        }
                ),
                connection
        );
    }
}
