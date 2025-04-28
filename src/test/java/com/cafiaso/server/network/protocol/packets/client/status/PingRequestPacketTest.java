package com.cafiaso.server.network.protocol.packets.client.status;

import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.packets.client.status.PingRequestPacket.Deserializer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.cafiaso.server.network.protocol.packets.client.status.PingRequestPacket.Deserializer.*;
import static org.junit.jupiter.api.Assertions.*;

class PingRequestPacketTest {

    @Test
    void deserialize_OK() throws IOException {
        long payload = 1;

        FriendlyBuffer buffer = createBuffer(payload);

        PingRequestPacket packet = new Deserializer().deserialize(buffer);

        assertEquals(payload, packet.timestamp());
    }

    private FriendlyBuffer createBuffer(long payload) throws IOException {
        FriendlyBuffer buffer = new FriendlyBuffer();
        buffer.write(payload, PAYLOAD_TYPE);

        buffer.flip();

        return buffer;
    }
}
