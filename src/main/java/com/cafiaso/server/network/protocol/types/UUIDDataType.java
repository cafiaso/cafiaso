package com.cafiaso.server.network.protocol.types;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;

import java.io.IOException;
import java.util.UUID;

/**
 * A {@link DataType} for {@link UUID} values.
 * <p>
 * UUIDs are 128-bit unsigned integers (or two unsigned 64-bit integers: the most significant bits and the least significant bits).
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Type:UUID">UUID Type</a>
 */
public class UUIDDataType implements DataType<UUID> {

    @Override
    public Class<UUID> getType() {
        return UUID.class;
    }

    @Override
    public UUID read(FriendlyBuffer in) throws IOException {
        long mostSignificantBits = in.read(DataTypes.LONG);
        long leastSignificantBits = in.read(DataTypes.LONG);

        return new UUID(mostSignificantBits, leastSignificantBits);
    }

    @Override
    public void write(UUID value, FriendlyBuffer out) throws IOException {
        out.write(value.getMostSignificantBits(), DataTypes.LONG);
        out.write(value.getLeastSignificantBits(), DataTypes.LONG);
    }
}
