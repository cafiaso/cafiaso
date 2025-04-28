package com.cafiaso.server.network.protocol.packets.client.handshake;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.packets.client.PacketDeserializer;

import java.io.IOException;

/**
 * Packet sent by legacy clients to ping the server or by modern clients when the server does not send
 * any response within a 30-second time window.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Legacy_Server_List_Ping">Legacy Server List Ping Packet</a>
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Server_List_Ping#1.6">Legacy Server List Ping (1.6 and older)</a>
 */
public record LegacyServerListPingPacket(int payload) {

    public static class Deserializer implements PacketDeserializer<LegacyServerListPingPacket> {

        public static final DataType<Integer> PAYLOAD_TYPE = DataTypes.VAR_INT;

        @Override
        public int getId() {
            return 0xFE;
        }

        @Override
        public LegacyServerListPingPacket deserialize(FriendlyBuffer in) throws IOException {
            int payload = in.read(PAYLOAD_TYPE);

            return new LegacyServerListPingPacket(payload);
        }
    }
}
