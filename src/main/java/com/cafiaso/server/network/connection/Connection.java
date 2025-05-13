package com.cafiaso.server.network.connection;

import com.cafiaso.server.network.protocol.io.FriendlyBuffer;

import java.io.IOException;

/**
 * A connection between a client and the server.
 * <p>
 * The connection is established as soon as the client emits a ping or is trying to connect to the server
 * and is closed when the ping request has been handled or when the client disconnects from the server.
 */
public abstract class Connection {

    private ConnectionState state = ConnectionState.HANDSHAKE;

    /**
     * Reads data from the client and writes it to the given {@link FriendlyBuffer}.
     *
     * @param buffer the buffer to write data to
     * @return the number of bytes read
     * @throws IOException if an I/O error occurs while writing data
     */
    public abstract int read(FriendlyBuffer buffer) throws IOException;

    /**
     * Sends data from the given {@link FriendlyBuffer} to the client.
     * <p>
     * This method should block until all data is written.
     *
     * @param buffer the buffer to read data from
     * @throws IOException if an I/O error occurs while reading data
     */
    public abstract void write(FriendlyBuffer buffer) throws IOException;

    /**
     * Checks if the connection is still open.
     *
     * @return {@code true} if the connection is open, {@code false} otherwise
     */
    public abstract boolean isOpen();

    /**
     * Closes the connection.
     * <p>
     * When the connection is closed, it will no longer be able to read or write data.
     * <p>
     * This method will fail silently if the connection is already closed.
     *
     * @throws IOException if an error occurs while closing the connection
     */
    public abstract void close() throws IOException;

    /**
     * Gets the current {@link ConnectionState} of the connection.
     *
     * @return the current connection state
     */
    public ConnectionState getState() {
        return state;
    }

    /**
     * Sets the current {@link ConnectionState} of the connection.
     *
     * @param state the new connection state
     */
    public void setState(ConnectionState state) {
        this.state = state;
    }
}
