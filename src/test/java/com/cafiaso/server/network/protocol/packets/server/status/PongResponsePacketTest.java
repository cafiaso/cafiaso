package com.cafiaso.server.network.protocol.packets.server.status;

import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.cafiaso.server.network.protocol.packets.server.status.PongResponsePacket.*;
import static org.junit.jupiter.api.Assertions.*;

class PongResponsePacketTest {

    @Test
    void serialize_OK() throws IOException {
        long payload = 123L;

        FriendlyBuffer buffer = new FriendlyBuffer();

        PongResponsePacket packet = new PongResponsePacket(payload);
        packet.serialize(buffer);

        buffer.flip();

        assertEquals(payload, buffer.read(PAYLOAD_TYPE));
    }
}
