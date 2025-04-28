package com.cafiaso.server.network.protocol.packets.server.status;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.packets.server.SerializablePacket;
import com.cafiaso.server.network.protocol.packets.client.status.PingRequestPacket;
import com.cafiaso.server.network.protocol.DataTypes;

import java.io.IOException;

/**
 * Packet sent by the server in response to a {@link PingRequestPacket}.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Pong_Response_(status)">Pong Response Packet</a>
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Server_List_Ping#Pong_Response">Pong response</a>
 */
public record PongResponsePacket(long payload) implements SerializablePacket {

    public static final DataType<Long> PAYLOAD_TYPE = DataTypes.LONG;

    @Override
    public int getId() {
        return 0x01;
    }

    @Override
    public void serialize(FriendlyBuffer out) throws IOException {
        out.write(payload, PAYLOAD_TYPE);
    }
}
