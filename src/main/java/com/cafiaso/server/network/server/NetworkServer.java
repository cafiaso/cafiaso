package com.cafiaso.server.network.server;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.registry.ConnectionRegistry;
import com.cafiaso.server.network.protocol.reader.PacketReader;
import com.cafiaso.server.network.protocol.reader.PacketReaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * The network server instance.
 * <p>
 * This class is responsible for listening and accepting incoming {@link Connection} instances.
 * <p>
 * The server can be started by calling {@link #bind(String, int)} with the desired host and port
 * and can be stopped by calling {@link #close()}.
 */
public abstract class NetworkServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkServer.class);

    private final ExecutorService executorService;
    private final ConnectionRegistry connectionRegistry;
    private final PacketReaderFactory packetReaderFactory;

    public NetworkServer(ExecutorService executorService, ConnectionRegistry connectionRegistry, PacketReaderFactory packetReaderFactory) {
        this.executorService = executorService;
        this.connectionRegistry = connectionRegistry;
        this.packetReaderFactory = packetReaderFactory;
    }

    /**
     * Binds the server to the specified host and port and starts listening for incoming connections,
     * asynchronously.
     *
     * @param host the host to listen on
     * @param port the port to listen on
     */
    public abstract void bind(String host, int port) throws IOException;

    /**
     * Closes the server and releases all connections.
     * <p>
     * Will fail silently if the server is not running.
     *
     * @throws IOException if an error occurs while closing the server
     */
    public void close() throws IOException {
        connectionRegistry.closeAllConnections();
    }

    /**
     * Accepts a new connection and starts reading packets from it.
     *
     * @param connection the accepted connection
     */
    protected void acceptConnection(Connection connection) {
        LOGGER.info("Incoming connection from {}", connection);

        // Register the connection with the connection registry
        connectionRegistry.addConnection(connection);

        PacketReader packetReader = packetReaderFactory.createPacketReader(connection);

        // Start a new thread to read from the connection
        executorService.submit(() -> {
            while (connection.isOpen()) {
                try {
                    packetReader.read();
                } catch (Exception e) {
                    LOGGER.error("Failed to read from connection {}. Closing connection", connection, e);

                    connectionRegistry.closeConnection(connection);
                }
            }
        });
    }
}
