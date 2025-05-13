package com.cafiaso.server.network.protocol.packets.server.status;

import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.cafiaso.server.network.protocol.packets.server.status.StatusResponsePacket.*;
import static org.junit.jupiter.api.Assertions.*;

class StatusResponsePacketTest {

    @Test
    void serialize_OK() throws IOException {
        String jsonResponse = "{\"players\":{\"max\":20,\"online\":10},\"description\":{\"text\":\"A Minecraft Server\"},\"version\":{\"protocol\":770,\"name\":\"1.21.5\"}}";

        FriendlyBuffer buffer = new FriendlyBuffer();

        StatusResponsePacket packet = new StatusResponsePacket(jsonResponse);
        packet.serialize(buffer);

        buffer.flip();

        assertEquals(jsonResponse, buffer.read(JSON_RESPONSE_TYPE));
    }
}
