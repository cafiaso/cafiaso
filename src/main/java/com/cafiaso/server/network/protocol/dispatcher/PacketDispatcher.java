package com.cafiaso.server.network.protocol.dispatcher;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.packets.server.SerializablePacket;
import com.cafiaso.server.network.protocol.DataTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * A dispatcher to send a {@link SerializablePacket}s to the given {@link Connection}.
 * <p>
 * It handles the serialization of the packet body (ID and data), computes the packet length,
 * encrypts the packet if necessary, and writes it to the connection.
 */
public class PacketDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketDispatcher.class);

    /**
     * Sends a packet to the given {@link Connection}.
     *
     * @param packet     the packet to send
     * @param connection the connection to send the packet to
     * @throws IOException if an I/O error occurs while sending the packet
     */
    public void dispatch(SerializablePacket packet, Connection connection) throws IOException {
        // Holds the packet body (ID and data) (unencrypted)
        FriendlyBuffer bodyBuffer = new FriendlyBuffer();
        bodyBuffer.write(packet.getId(), DataTypes.VAR_INT); // Write the packet ID
        packet.serialize(bodyBuffer); // Write the packet data

        int packetLength = bodyBuffer.getPosition();

        LOGGER.debug("{} bytes outgoing to {}", packetLength, connection);

        // Holds the packet (length and body) (unencrypted)
        FriendlyBuffer packetBuffer = new FriendlyBuffer();
        packetBuffer.write(packetLength, DataTypes.VAR_INT); // Write the packet length
        packetBuffer.write(bodyBuffer); // Write the packet body

        // Encrypt the packet buffer using the shared secret
        packetBuffer.encrypt(connection.getSharedSecret());

        // Write the packet to the connection
        connection.write(packetBuffer);

        LOGGER.debug("Sent packet {} to {}", packet, connection);
    }
}
