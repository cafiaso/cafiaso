package com.cafiaso.server.server;

/**
 * The Minecraft server instance.
 * <p>
 * This class is the main entry point and is responsible for managing the server lifecycle.
 * <p>
 * The server can be started by calling {@link #start(String, int)} with the desired host and port
 * and can be stopped by calling either {@link #stop()} or by killing the process.
 */
public interface Server {

    /**
     * Starts the server on the specified host and port.
     *
     * @param host the host to listen on
     * @param port the port to listen on
     *
     * @throws IllegalStateException if the server is already running
     */
    void start(String host, int port);

    /**
     * Stops the server and releases all resources.
     * <p>
     * Will fail silently if the server is not running.
     */
    void stop();
}
