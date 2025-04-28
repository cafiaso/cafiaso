package com.cafiaso.server.network.protocol.packets.client.status;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.packets.client.PacketDeserializer;

import java.io.IOException;

/**
 * Packet sent by the client to request a pong response from the server.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Ping_Request_(status)">Ping Request Packet</a>
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Server_List_Ping#Ping_Request">Ping Request</a>
 */
public record PingRequestPacket(long timestamp) {

    public static class Deserializer implements PacketDeserializer<PingRequestPacket> {

        public static final DataType<Long> PAYLOAD_TYPE = DataTypes.LONG;

        @Override
        public int getId() {
            return 0x01;
        }

        @Override
        public PingRequestPacket deserialize(FriendlyBuffer in) throws IOException {
            long payload = in.read(PAYLOAD_TYPE);

            return new PingRequestPacket(payload);
        }
    }
}
