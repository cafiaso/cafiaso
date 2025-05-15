package com.cafiaso.server.network.protocol.handlers.login;

import com.cafiaso.server.encryption.Encryption;
import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.Identity;
import com.cafiaso.server.network.protocol.PacketHandler;
import com.cafiaso.server.network.protocol.dispatcher.PacketDispatcher;
import com.cafiaso.server.network.protocol.packets.client.login.LoginStartPacket;
import com.cafiaso.server.network.protocol.packets.server.login.EncryptionRequestPacket;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LoginStartPacketHandler implements PacketHandler<LoginStartPacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginStartPacketHandler.class);

    private final Encryption encryption;
    private final PacketDispatcher packetDispatcher;

    @Inject
    public LoginStartPacketHandler(Encryption encryption, PacketDispatcher packetDispatcher) {
        this.encryption = encryption;
        this.packetDispatcher = packetDispatcher;
    }

    @Override
    public void handle(LoginStartPacket packet, Connection connection) throws IOException {
        Identity identity = new Identity(packet.name(), packet.playerUuid());

        LOGGER.debug("Starting login process for {}", identity);

        // Set the identity of the connection
        connection.setIdentity(identity);

        // Generate a new token and send it to the client
        // It will be used to verify the shared secret after the client sends the EncryptionResponsePacket
        byte[] verifyToken = encryption.generateVerifyToken();
        connection.setVerifyToken(verifyToken);

       byte[] publicKey = encryption.getPublicKey();

        // Send EncryptionRequest packet to the client
        packetDispatcher.dispatch(
                new EncryptionRequestPacket(
                        Encryption.SERVER_ID, // Server id, always empty since 1.7
                        publicKey, // Server public key
                        verifyToken, // Verify token
                        true // Authentication with Mojang is mandatory for online mode servers
                ),
                connection
        );
    }
}
