package com.cafiaso.server;

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
     * The Minecraft version this server is running.
     */
    String MINECRAFT_VERSION = "1.21.5";

    /**
     * The protocol version this server is running.
     */
    int PROTOCOL_VERSION = 770;

    /**
     * Starts the server on the specified host and port.
     *
     * @param host the host to listen on
     * @param port the port to listen on
     * @throws IllegalStateException if the server is already running
     * @throws RuntimeException      if the server fails to start
     */
    void start(String host, int port);

    /**
     * Stops the server and releases all resources.
     * <p>
     * Will fail silently if the server is not running.
     *
     * @throws RuntimeException if the server fails to stop
     */
    void stop();
}
