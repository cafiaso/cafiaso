package com.cafiaso.server.network.protocol.packets.client.handshake;

import com.cafiaso.server.Server;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.packets.client.handshake.HandshakePacket.Deserializer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.cafiaso.server.network.protocol.packets.client.handshake.HandshakePacket.Deserializer.*;
import static org.junit.jupiter.api.Assertions.*;

class HandshakePacketTest {

    @Test
    void deserialize_OK() throws IOException {
        int protocolVersion = Server.PROTOCOL_VERSION;
        String serverAddress = "mc.cafiaso.org";
        int serverPort = 25565;
        HandshakePacket.Intention nextState = HandshakePacket.Intention.STATUS;

        FriendlyBuffer buffer = createBuffer(protocolVersion, serverAddress, serverPort, nextState);

        HandshakePacket packet = new Deserializer().deserialize(buffer);

        assertEquals(protocolVersion, packet.protocolVersion());
        assertEquals(serverAddress, packet.serverAddress());
        assertEquals(serverPort, packet.serverPort());
        assertEquals(nextState, packet.nextState());
    }

    @Test
    void deserialize_ShouldThrowException_WhenAddressIsTooLong() throws IOException {
        int protocolVersion = Server.PROTOCOL_VERSION;
        String serverAddress = "0".repeat(256);
        int serverPort = 25565;
        HandshakePacket.Intention nextState = HandshakePacket.Intention.STATUS;

        FriendlyBuffer buffer = createBuffer(protocolVersion, serverAddress, serverPort, nextState);

        assertThrowsExactly(IOException.class, () -> new Deserializer().deserialize(buffer));
    }

    private FriendlyBuffer createBuffer(int protocolVersion, String serverAddress, int serverPort, HandshakePacket.Intention nextState) throws IOException {
        FriendlyBuffer buffer = new FriendlyBuffer();
        buffer.write(protocolVersion, PROTOCOL_VERSION_TYPE);
        buffer.write(serverAddress, SERVER_ADDRESS_TYPE);
        buffer.write(serverPort, SERVER_PORT_TYPE);
        buffer.write(nextState, NEXT_STATE_TYPE);

        buffer.flip();

        return buffer;
    }
}
