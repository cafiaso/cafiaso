package com.cafiaso.server.network.protocol.packets.server.status;

import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.cafiaso.server.Server.MINECRAFT_VERSION;
import static com.cafiaso.server.Server.PROTOCOL_VERSION;
import static com.cafiaso.server.configuration.ServerConfiguration.DEFAULT_DESCRIPTION;
import static com.cafiaso.server.configuration.ServerConfiguration.DEFAULT_MAX_PLAYERS;
import static com.cafiaso.server.network.protocol.packets.server.status.StatusResponsePacket.*;
import static org.junit.jupiter.api.Assertions.*;

class StatusResponsePacketTest {

    @Test
    void serialize_OK() throws IOException {
        String jsonResponse = "{\"players\":{\"max\":%d,\"online\":10},\"description\":{\"text\":\"%s\"},\"version\":{\"protocol\":%d,\"username\":\"%s\"}}"
                .formatted(DEFAULT_MAX_PLAYERS, DEFAULT_DESCRIPTION, PROTOCOL_VERSION, MINECRAFT_VERSION);

        FriendlyBuffer buffer = new FriendlyBuffer();

        StatusResponsePacket packet = new StatusResponsePacket(jsonResponse);
        packet.serialize(buffer);

        buffer.flip();

        assertEquals(jsonResponse, buffer.read(JSON_RESPONSE_TYPE));
    }
}
