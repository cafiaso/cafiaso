package com.cafiaso.server.network.protocol.handlers.login;

import com.cafiaso.server.encryption.Encryption;
import com.cafiaso.server.mojang.MojangClient;
import com.cafiaso.server.mojang.PlayerProfile;
import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.registry.ConnectionRegistry;
import com.cafiaso.server.network.protocol.PacketHandler;
import com.cafiaso.server.network.protocol.dispatcher.PacketDispatcher;
import com.cafiaso.server.network.protocol.packets.client.login.EncryptionResponsePacket;
import com.cafiaso.server.network.protocol.packets.server.login.LoginSuccessPacket;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.io.IOException;

public class EncryptionResponsePacketHandler implements PacketHandler<EncryptionResponsePacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionResponsePacketHandler.class);

    private final Encryption encryption;
    private final MojangClient mojangClient;
    private final ConnectionRegistry connectionRegistry;
    private final PacketDispatcher packetDispatcher;

    @Inject
    public EncryptionResponsePacketHandler(
            Encryption encryption,
            MojangClient mojangClient,
            ConnectionRegistry connectionRegistry,
            PacketDispatcher packetDispatcher
    ) {
        this.encryption = encryption;
        this.mojangClient = mojangClient;
        this.connectionRegistry = connectionRegistry;
        this.packetDispatcher = packetDispatcher;
    }

    @Override
    public void handle(EncryptionResponsePacket packet, Connection connection) throws IOException {
        if (!encryption.isVerifyTokenValid(packet.verifyToken(), connection.getVerifyToken())) {
            // The token sent by the client does not match the one we sent, disconnect the client
            connectionRegistry.closeConnection(connection);

            return;
        }

        SecretKey sharedSecret = encryption.decryptSharedSecret(packet.sharedSecret());

        // Set the shared secret in the connection
        // From now on, all packets will be encrypted using this shared secret
        connection.setSharedSecret(sharedSecret);

        // Generate the server ID used to verify the client login session
        String digestedServerId = encryption.generateServerId(sharedSecret);

        PlayerProfile profile = mojangClient.verifyClientLoginSession(
                connection.getIdentity().name(),
                digestedServerId,
                connection.getHostAddress()
        );

        LOGGER.debug("Client {} logged in with profile {}", connection.getIdentity(), profile);

        // Send LoginSuccess packet to the client
        packetDispatcher.dispatch(
                new LoginSuccessPacket(
                        profile.getUuid(),
                        profile.name(),
                        new PlayerProfile.Property[0]
                        //profile.properties()
                ),
                connection
        );
    }
}
