package com.cafiaso.server.network.protocol.packets.client.login;

import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.packets.client.PacketDeserializer;

public record LoginAcknowledgedPacket() {

    public static class Deserializer implements PacketDeserializer<LoginAcknowledgedPacket> {

        @Override
        public int getId() {
            return 0x03;
        }

        @Override
        public LoginAcknowledgedPacket deserialize(FriendlyBuffer in) {
            return new LoginAcknowledgedPacket();
        }
    }
}
