package com.cafiaso.server.network.protocol.packets.server.login;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.packets.server.SerializablePacket;

import java.io.IOException;

public record EncryptionRequestPacket(String serverId, byte[] publicKey, byte[] verifyToken, boolean shouldAuthenticate) implements SerializablePacket {

    public static DataType<String> SERVER_ID_TYPE = DataTypes.STRING(20);
    public static DataType<byte[]> PUBLIC_KEY_TYPE = DataTypes.PREFIXED_BYTE_ARRAY;
    public static DataType<byte[]> VERIFY_TOKEN_TYPE = DataTypes.PREFIXED_BYTE_ARRAY;
    public static DataType<Boolean> SHOULD_AUTHENTICATE_TYPE = DataTypes.BOOLEAN;

    @Override
    public int getId() {
        return 0x01;
    }

    @Override
    public void serialize(FriendlyBuffer out) throws IOException {
        out.write(serverId, SERVER_ID_TYPE);
        out.write(publicKey, PUBLIC_KEY_TYPE);
        out.write(verifyToken, VERIFY_TOKEN_TYPE);
        out.write(shouldAuthenticate, SHOULD_AUTHENTICATE_TYPE);
    }
}
