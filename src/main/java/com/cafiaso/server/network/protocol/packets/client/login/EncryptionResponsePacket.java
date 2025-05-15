package com.cafiaso.server.network.protocol.packets.client.login;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.packets.client.PacketDeserializer;

import java.io.IOException;

public record EncryptionResponsePacket(byte[] sharedSecret, byte[] verifyToken) {

    public static class Deserializer implements PacketDeserializer<EncryptionResponsePacket> {

        public static final DataType<byte[]> SHARED_SECRET_TYPE = DataTypes.PREFIXED_BYTE_ARRAY;
        public static final DataType<byte[]> VERIFY_TOKEN_TYPE = DataTypes.PREFIXED_BYTE_ARRAY;

        @Override
        public int getId() {
            return 0x01;
        }

        @Override
        public EncryptionResponsePacket deserialize(FriendlyBuffer in) throws IOException {
            byte[] sharedSecret = in.read(SHARED_SECRET_TYPE);
            byte[] verifyToken = in.read(VERIFY_TOKEN_TYPE);

            return new EncryptionResponsePacket(sharedSecret, verifyToken);
        }
    }
}
