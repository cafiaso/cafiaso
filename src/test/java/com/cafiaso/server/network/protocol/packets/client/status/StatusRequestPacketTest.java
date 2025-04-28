package com.cafiaso.server.network.protocol.packets.client.status;

import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.packets.client.status.StatusRequestPacket.Deserializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusRequestPacketTest {

    @Test
    void deserialize_OK() {
        FriendlyBuffer buffer = new FriendlyBuffer();

        assertDoesNotThrow(() -> new Deserializer().deserialize(buffer));
    }
}
