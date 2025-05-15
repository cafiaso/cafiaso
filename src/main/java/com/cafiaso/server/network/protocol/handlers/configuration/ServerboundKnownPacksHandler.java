package com.cafiaso.server.network.protocol.handlers.configuration;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.protocol.PacketHandler;
import com.cafiaso.server.network.protocol.packets.client.configuration.ServerboundKnownPacks;

import java.io.IOException;

public class ServerboundKnownPacksHandler implements PacketHandler<ServerboundKnownPacks> {

    @Override
    public void handle(ServerboundKnownPacks packet, Connection connection) throws IOException {

    }
}
