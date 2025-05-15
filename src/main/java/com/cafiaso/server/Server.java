package com.cafiaso.server;

import com.cafiaso.server.configuration.ServerConfiguration;
import com.cafiaso.server.encryption.Encryption;
import com.cafiaso.server.network.server.NetworkServer;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The Minecraft server instance.
 * <p>
 * This class is the main entry point and is responsible for managing the server lifecycle.
 * <p>
 * The server can be started by calling {@link #start(String, int)} with the desired host and port
 * and can be stopped by calling either {@link #stop()} or by killing the process.
 */
public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    /**
     * The Minecraft version this server is running.
     */
    public static final String MINECRAFT_VERSION = "1.21.5";

    /**
     * The protocol version this server is running.
     */
    public static final int PROTOCOL_VERSION = 770;

    private final NetworkServer network;
    private final Encryption encryption;
    private final ServerConfiguration configuration;
    private final ServerIcon icon;

    private boolean running;

    @Inject
    public Server(NetworkServer network, Encryption encryption, ServerConfiguration configuration, ServerIcon icon) {
        this.network = network;
        this.encryption = encryption;
        this.configuration = configuration;
        this.icon = icon;
    }

    /**
     * Starts the server on the specified host and port.
     *
     * @param host the host to listen on
     * @param port the port to listen on
     * @throws IllegalStateException if the server is already running
     * @throws RuntimeException      if the server fails to start
     */
    public void start(String host, int port) {
        if (running) {
            throw new IllegalStateException("Server is already running");
        }

        // Load the encryption keys
        encryption.generateKeyPair();

        // Load the server configuration
        configuration.load();

        // Load the server icon
        try {
            icon.load();
        } catch (IOException e) {
            LOGGER.warn("Failed to load icon", e);
        }

        try {
            // Bind the network server and start listening for connections
            network.bind(host, port);

            running = true;

            LOGGER.info("Server started on {}:{}", host, port);
        } catch (IOException e) {
            throw new RuntimeException("Failed to start server", e);
        }
    }

    /**
     * Stops the server and releases all resources.
     * <p>
     * Will fail silently if the server is not running.
     *
     * @throws RuntimeException if the server fails to stop
     */
    public void stop() {
        if (!running) {
            return;
        }

        try {
            // Close the network server
            network.close();

            LOGGER.info("Server stopped");
        } catch (IOException e) {
            throw new RuntimeException("Failed to stop server", e);
        } finally {
            running = false;
        }
    }
}
