package com.cafiaso.server.network.connection;

import java.io.IOException;

/**
 * Represents a connection between a client and the {@link com.cafiaso.server.network.server.NetworkServer}.
 */
public interface Connection {

    /**
     * Reads data from the connection.
     * <p>
     * If no data is available, this method will return immediately.
     * <p>
     * This method is typically called in a loop to continuously read data from the connection.
     *
     *
     * @throws Exception if an error occurs while reading data
     * @throws IllegalStateException if the connection is closed
     */
    void read() throws Exception;

    /**
     * Checks if the connection is still open.
     *
     * @return {@code true} if the connection is open, {@code false} otherwise
     */
    boolean isOpen();

    /**
     * Closes the connection.
     * <p>
     * When the connection is closed, it will no longer be able to read or write data.
     * <p>
     * Will fail silently if the connection is already closed.
     *
     * @throws IOException if an error occurs while closing the connection
     */
    void close() throws IOException;
}
