package com.cafiaso.server.network.protocol.packets.client.status;

import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.packets.client.PacketDeserializer;

/**
 * Packet sent by the client to request the server status (MOTD, player count, etc.).
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Status_Request">Status Request Packet</a>
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Server_List_Ping#Status_Request">Status Request</a>
 */
public record StatusRequestPacket() {

    public static class Deserializer implements PacketDeserializer<StatusRequestPacket> {

        @Override
        public int getId() {
            return 0x00;
        }

        @Override
        public StatusRequestPacket deserialize(FriendlyBuffer in) {
            return new StatusRequestPacket();
        }
    }
}
