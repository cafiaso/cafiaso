package com.cafiaso.server;

import com.cafiaso.server.configuration.ServerConfiguration;
import com.cafiaso.server.network.server.NetworkServer;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Default {@link Server} implementation.
 */
public class ServerImpl implements Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerImpl.class);

    private final NetworkServer network;
    private final ServerConfiguration configuration;
    private final ServerIcon icon;

    private boolean running;

    @Inject
    public ServerImpl(NetworkServer network, ServerConfiguration configuration, ServerIcon icon) {
        this.network = network;
        this.configuration = configuration;
        this.icon = icon;
    }

    @Override
    public void start(String host, int port) {
        if (running) {
            throw new IllegalStateException("Server is already running");
        }

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
            LOGGER.error("Failed to start network server. Shutting down.", e);
        }
    }

    @Override
    public void stop() {
        if (!running) {
            return;
        }

        try {
            // Close the network server
            network.close();

            LOGGER.info("Server stopped");
        } catch (IOException e) {
            LOGGER.error("Failed to stop network server", e);
        } finally {
            running = false;
        }
    }
}
