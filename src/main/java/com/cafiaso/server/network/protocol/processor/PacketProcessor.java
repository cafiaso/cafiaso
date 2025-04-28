package com.cafiaso.server.network.protocol.processor;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.ConnectionState.PacketRegistration;
import com.cafiaso.server.network.protocol.PacketHandler;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.packets.client.PacketDeserializer;
import com.google.inject.Injector;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Processes packets received from the client.
 * <p>
 * This class is responsible for reading the packet data from the input stream, resolving the packet type,
 * and invoking the appropriate packet handler to process the packet.
 */
public class PacketProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketProcessor.class);

    private final Injector injector;

    @Inject
    public PacketProcessor(Injector injector) {
        this.injector = injector;
    }

    /**
     * Processes a packet received from the client.
     *
     * @param length the length of the packet (VarInt)
     * @param bodyBuffer the buffer containing the packet body (ID and data)
     * @param connection the connection to the client
     * @param <P> the type of the packet
     * @return the number of remaining bytes in the input stream
     * @throws IOException if an error occurs while processing the packet
     */
    public <P> int process(int length, FriendlyBuffer bodyBuffer, Connection connection) throws IOException {
        int packetId = length == 0xFE ? 0xFE : bodyBuffer.read(DataTypes.VAR_INT);

        PacketRegistration<P> registration = connection.getState()
                .<P>getPacket(packetId)
                .orElseThrow(() -> new UnknownPacketException(packetId, connection.getState()));

        PacketDeserializer<P> deserializer = registration.deserializer();
        P packet = deserializer.deserialize(bodyBuffer);

        LOGGER.debug("Received packet {} from {}", packet, connection);

        int remainingBytes = bodyBuffer.getRemainingBytes();

        if (remainingBytes > 0) {
            LOGGER.warn("{} bytes remaining after reading packet {}. They will be lost", remainingBytes, packet);
        }

        PacketHandler<P> handler = injector.getInstance(registration.handler());
        handler.handle(packet, connection);

        return remainingBytes;
    }
}
