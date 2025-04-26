package com.cafiaso.server.network.server;

import com.cafiaso.server.network.connection.Connection;

/**
 * The network server instance.
 * <p>
 * This class is responsible for listening and accepting incoming {@link Connection} instances.
 * <p>
 * The server can be started by calling {@link #bind(String, int)} with the desired host and port
 * and can be stopped by calling {@link #close()}.
 */
public interface NetworkServer {

    /**
     * Binds the server to the specified host and port and starts listening for incoming connections,
     * asynchronously.
     *
     * @param host the host to listen on
     * @param port the port to listen on
     */
    void bind(String host, int port) throws Exception;

    /**
     * Closes the server and releases all connections.
     * <p>
     * Will fail silently if the server is not running.
     *
     * @throws Exception if an error occurs while closing the server
     */
    void close() throws Exception;
}
