package com.cafiaso.server.network.protocol.packets.server.login;

import com.cafiaso.server.mojang.PlayerProfile;
import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.packets.server.SerializablePacket;

import java.io.IOException;
import java.util.UUID;

public record LoginSuccessPacket(UUID uuid, String username, PlayerProfile.Property[] properties) implements SerializablePacket {

    public static final DataType<UUID> UUID_TYPE = DataTypes.UUID;
    public static final DataType<String> USERNAME_TYPE = DataTypes.STRING(20);
    public static final DataType<PlayerProfile.Property[]> PROPERTIES_TYPE = DataTypes.PREFIXED_ARRAY(PlayerProfile.Property.TYPE);

    @Override
    public int getId() {
        return 0x02;
    }

    @Override
    public void serialize(FriendlyBuffer out) throws IOException {
        out.write(uuid, UUID_TYPE);
        out.write(username, USERNAME_TYPE);
        out.write(properties, PROPERTIES_TYPE);
    }
}
