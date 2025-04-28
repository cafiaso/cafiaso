package com.cafiaso.server.network.protocol.types;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;

/**
 * A {@link DataType} for {@link Long} values.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Type:Long">Long Type</a>
 */
public class LongDataType implements DataType<Long> {

    @Override
    public Long read(FriendlyBuffer in) {
        return in.readLong();
    }

    @Override
    public void write(Long value, FriendlyBuffer out) {
        out.writeLong(value);
    }
}
