package com.cafiaso.server.network.protocol.packets.server.status;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.packets.server.SerializablePacket;
import com.cafiaso.server.network.protocol.packets.client.status.StatusRequestPacket;
import com.cafiaso.server.network.protocol.DataTypes;

import java.io.IOException;

/**
 * Packet sent by the server in response to a {@link StatusRequestPacket}.
 * <p>
 * Example JSON response:
 * <pre>
 * {
 *   "version": {
 *     "name": "1.21.5",
 *     "protocol": 770
 *   },
 *   "players": {
 *     "max": 20,
 *     "online": 5,
 *     "sample": [
 *       {
 *         "name": "Choukas",
 *         "uuid": "123e4567-e89b-12d3-a456-426614174000"
 *       }
 *     ]
 *   },
 *   "description": {
 *     "text": "Cookies !"
 *   },
 *   "favicon": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUA"
 * }
 *
 * @param jsonResponse A JSON object containing server information.
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Status_Response">Status Response Packet</a>
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Server_List_Ping#Status_Response">Status Response</a>
 */
public record StatusResponsePacket(String jsonResponse) implements SerializablePacket {

    public static final DataType<String> JSON_RESPONSE_TYPE = DataTypes.STRING;

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    public void serialize(FriendlyBuffer out) throws IOException {
        out.write(jsonResponse, JSON_RESPONSE_TYPE);
    }
}
