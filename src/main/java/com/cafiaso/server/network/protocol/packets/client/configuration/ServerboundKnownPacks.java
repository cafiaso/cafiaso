package com.cafiaso.server.network.protocol.packets.client.configuration;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.packets.client.PacketDeserializer;
import com.cafiaso.server.pack.DataPack;

import java.io.IOException;

public record ServerboundKnownPacks(DataPack[] knownPacks) {

    public static DataType<DataPack[]> KNOWN_PACKS_TYPE = DataTypes.PREFIXED_ARRAY(DataPack.TYPE);

    public static class Deserializer implements PacketDeserializer<ServerboundKnownPacks> {

        @Override
        public int getId() {
            return 0x07;
        }

        @Override
        public ServerboundKnownPacks deserialize(FriendlyBuffer in) throws IOException {
            DataPack[] knownPacks = in.read(KNOWN_PACKS_TYPE);

            return new ServerboundKnownPacks(knownPacks);
        }
    }
}
