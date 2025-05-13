package com.cafiaso.server.network.protocol.packets.client.handshake;

import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.packets.client.handshake.LegacyServerListPingPacket.Deserializer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.cafiaso.server.network.protocol.packets.client.handshake.LegacyServerListPingPacket.Deserializer.*;
import static org.junit.jupiter.api.Assertions.*;

class LegacyServerListPingPacketTest {

    @Test
    void deserialize_OK() throws IOException {
        int payload = 1;

        FriendlyBuffer buffer = createBuffer(payload);

        LegacyServerListPingPacket packet = new Deserializer().deserialize(buffer);

        assertEquals(payload, packet.payload());
    }

    private FriendlyBuffer createBuffer(int payload) throws IOException {
        FriendlyBuffer buffer = new FriendlyBuffer();
        buffer.write(payload, PAYLOAD_TYPE);

        buffer.flip();

        return buffer;
    }
}
