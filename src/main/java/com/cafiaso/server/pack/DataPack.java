package com.cafiaso.server.pack;

import com.cafiaso.server.Server;
import com.cafiaso.server.namespace.Namespace;
import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;

import java.io.IOException;

public record DataPack(String namespace, String id, String version) {

    public static DataType<DataPack> TYPE = new DataPackDataType();

    public static DataPack CORE = new DataPack(Namespace.MINECRAFT, "core", Server.MINECRAFT_VERSION);

    public static class DataPackDataType implements DataType<DataPack> {

        @Override
        public Class<DataPack> getType() {
            return DataPack.class;
        }

        @Override
        public DataPack read(FriendlyBuffer in) throws IOException {
            String namespace = in.read(DataTypes.STRING);
            String id = in.read(DataTypes.STRING);
            String version = in.read(DataTypes.STRING);

            return new DataPack(namespace, id, version);
        }

        @Override
        public void write(DataPack value, FriendlyBuffer out) throws IOException {
            out.write(value.namespace, DataTypes.STRING);
            out.write(value.id, DataTypes.STRING);
            out.write(value.version, DataTypes.STRING);
        }
    }
}
