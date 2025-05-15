package com.cafiaso.server.network.protocol.packets.server.configuration;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.packets.server.SerializablePacket;
import com.cafiaso.server.pack.DataPack;

import java.io.IOException;

public record ClientboundKnownPacks(DataPack[] knownPacks) implements SerializablePacket {

    public static DataType<DataPack[]> KNOWN_PACKS_TYPE = DataTypes.PREFIXED_ARRAY(DataPack.TYPE);

    @Override
    public int getId() {
        return 0x0E;
    }

    @Override
    public void serialize(FriendlyBuffer out) throws IOException {
        out.write(knownPacks, KNOWN_PACKS_TYPE);
    }
}
