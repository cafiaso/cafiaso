package com.cafiaso.server.mojang;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.utils.UUIDUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents the profile of a player.
 *
 * @param id         the player UUID without dashes
 * @param name       the player name, case-sensitive
 * @param properties a list of player {@link Property}s
 * @see <a href="https://minecraft.wiki/w/Mojang_API#Query_player's_skin_and_cape">Mojang API</a>
 */
public record PlayerProfile(String id, String name, Property[] properties) {

    /**
     * Represents a property of the user.
     *
     * @param name      the name of the property. For now, the only property that exists is {@code textures}
     * @param value     Base64 encoded JSON string containing all player textures
     * @param signature signature signed with Yggdrasil private key as Base64 string
     */
    public record Property(String name, String value, String signature) {

        public static DataType<Property> TYPE = new PropertyDataType();

        public static class PropertyDataType implements DataType<Property> {

            public static final DataType<String> NAME_TYPE = DataTypes.STRING(64);
            public static final DataType<String> VALUE_TYPE = DataTypes.STRING(32767);
            public static final DataType<Optional<String>> SIGNATURE_TYPE = DataTypes.PREFIXED_OPTIONAL(DataTypes.STRING(1024));

            @Override
            public Class<Property> getType() {
                return Property.class;
            }

            @Override
            public Property read(FriendlyBuffer in) throws IOException {
                String name = NAME_TYPE.read(in);
                String value = VALUE_TYPE.read(in);
                Optional<String> signature = SIGNATURE_TYPE.read(in);

                return new Property(name, value, signature.orElse(null));
            }

            @Override
            public void write(Property value, FriendlyBuffer out) throws IOException {
                NAME_TYPE.write(value.name, out);
                VALUE_TYPE.write(value.value, out);
                SIGNATURE_TYPE.write(Optional.ofNullable(value.signature), out);
            }
        }
    }

    /**
     * Gets the ID of the user.
     *
     * @return the user ID
     */
    public UUID getUuid() {
        return UUIDUtils.fromStringWithoutDashes(id);
    }
}
