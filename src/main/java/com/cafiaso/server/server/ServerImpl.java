package com.cafiaso.server.server;

import com.cafiaso.server.network.server.NetworkServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default {@link Server} implementation.
 */
public class ServerImpl implements Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerImpl.class);

    private final NetworkServer network;

    private boolean running;

    public ServerImpl(NetworkServer network) {
        this.network = network;
    }

    @Override
    public void start(String host, int port) {
        if (running) {
            throw new IllegalStateException("Server is already running");
        }

        try {
            // Bind the network server and start listening for connections
            network.bind(host, port);

            running = true;

            LOGGER.info("Server started on {}:{}", host, port);
        } catch (Exception e) {
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
        } catch (Exception e) {
            LOGGER.error("Failed to stop network server", e);
        } finally {
            running = false;
        }
    }
}
