package com.cafiaso.server.network.protocol.reader;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.processor.PacketProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Reads packets from a connection and processes them using a {@link PacketProcessor}.
 * <p>
 * This class is responsible for reading packets from the input stream of a connection and passing them
 * to the specified packet processor for handling.
 */
public class PacketReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketReader.class);

    private final Connection connection;
    private final FriendlyBuffer buffer = new FriendlyBuffer();

    private final PacketProcessor processor;

    public PacketReader(Connection connection, PacketProcessor processor) {
        this.connection = connection;
        this.processor = processor;
    }

    /**
     * Reads {@code n} packets from the connection.
     * <p>
     * This method will return once no more packets are available (either because the buffer is empty
     * or because a packet has not been fully sent yet).
     * <p>
     * This method should typically be called in a loop to continuously read data from the connection.
     *
     * @throws IOException if an I/O error occurs while reading from the connection
     */
    public void read() throws IOException {
        if (!connection.isOpen()) {
            throw new IllegalStateException("Connection is closed");
        }

        int readBytes = connection.read(buffer);
        if (readBytes == -1) {
            throw new IOException("End of stream reached");
        }

        LOGGER.debug("{} bytes incoming from {}", readBytes, connection);

        buffer.flip(); // Prepare the buffer for reading

        while (buffer.hasRemainingBytes()) {
            buffer.mark(); // Mark the current position in the buffer

            int packetLength = buffer.read(DataTypes.VAR_INT); // Read the packet length (VarInt)

            LOGGER.debug("Processing packet of length {} from {}", packetLength, connection);

            int remainingBytes = buffer.getRemainingBytes();
            if (remainingBytes < packetLength) {
                // Not enough bytes to read the packet data, reset the buffer and wait for more data
                buffer.reset();
                buffer.compact();

                return;
            }

            // Create a copy of the buffer for the packet body
            FriendlyBuffer packetBodyBuffer = buffer.copy(packetLength);

            processor.process(packetLength, packetBodyBuffer, connection);
        }

        buffer.compact(); // Compact the buffer to remove the processed data
    }
}
