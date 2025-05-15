package com.cafiaso.server.network.protocol.packets.client.login;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.packets.client.PacketDeserializer;

import java.io.IOException;
import java.util.UUID;

public record LoginStartPacket(String name, UUID playerUuid) {

    public static class Deserializer implements PacketDeserializer<LoginStartPacket> {

        public static final DataType<String> USERNAME_TYPE = DataTypes.STRING(16);
        public static final DataType<UUID> UUID_TYPE = DataTypes.UUID;

        @Override
        public int getId() {
            return 0x00;
        }

        @Override
        public LoginStartPacket deserialize(FriendlyBuffer in) throws IOException {
            String username = in.read(USERNAME_TYPE);
            UUID uuid = in.read(UUID_TYPE);

            return new LoginStartPacket(username, uuid);
        }
    }
}
