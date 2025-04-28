package com.cafiaso.server.network.connection.registry;

import com.cafiaso.server.network.connection.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A registry for managing active {@link Connection}s.
 */
public class ConnectionRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionRegistry.class);

    private final Set<Connection> connections = ConcurrentHashMap.newKeySet();

    /**
     * Adds a new connection to the registry.
     *
     * @param connection the connection to add
     */
    public void addConnection(Connection connection) {
        connections.add(connection);
    }

    /**
     * Closes a connection and removes it from the registry.
     *
     * @param connection the connection to close
     */
    public void closeConnection(Connection connection) {
        close(connection);
        connections.remove(connection);
    }

    /**
     * Closes all connections in the registry.
     * <p>
     * This method is strictly equivalent to:
     * <pre>{@code
     * for (Connection connection : getConnections()) {
     *     connection.close();
     * }
     * }</pre>
     */
    public void closeAllConnections() {
        connections.forEach(this::close);
    }

    /**
     * Gets the active connections.
     *
     * @return the active connections
     */
    public Set<Connection> getConnections() {
        return connections;
    }

    /**
     * Closes a connection, safely handling any exceptions that may occur.
     *
     * @param connection the connection to close
     */
    private void close(Connection connection) {
        try {
            connection.close();

            LOGGER.info("Closed connection {}", connection);
        } catch (IOException e) {
            LOGGER.error("Failed to close connection {}", connection, e);
        }
    }
}
