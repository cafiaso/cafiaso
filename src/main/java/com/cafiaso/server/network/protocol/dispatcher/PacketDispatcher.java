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
 * It handles the serialization of the packet body (ID and data), computes the packet length
 * and sends the packet to the connection.
 */
public class PacketDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketDispatcher.class);

    /**
     * Sends a packet to the given {@link Connection}.
     *
     * @param packet the packet to send
     * @param connection the connection to send the packet to
     * @throws IOException if an I/O error occurs while sending the packet
     */
    public void dispatch(SerializablePacket packet, Connection connection) throws IOException {
        // Temporary buffer that holds the packet body
        FriendlyBuffer bodyBuffer = new FriendlyBuffer();
        bodyBuffer.write(packet.getId(), DataTypes.VAR_INT); // Write the packet ID
        packet.serialize(bodyBuffer); // Write the packet data

        int packetLength = bodyBuffer.getPosition();

        // Prepare the body buffer for reading in the final packet buffer
        bodyBuffer.flip();

        // Final packet buffer
        FriendlyBuffer packetBuffer = new FriendlyBuffer();
        packetBuffer.write(packetLength, DataTypes.VAR_INT); // Write the packet length
        packetBuffer.write(bodyBuffer); // Write the packet body

        // Prepare the packet buffer for reading in the connection
        packetBuffer.flip();

        LOGGER.debug("{} bytes outgoing to {}", packetLength, connection);

        // Send the buffer to the connection
        connection.write(packetBuffer);

        LOGGER.debug("Sent packet {} to {}", packet, connection);
    }
}
