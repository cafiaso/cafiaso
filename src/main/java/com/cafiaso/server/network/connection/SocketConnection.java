package com.cafiaso.server.network.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * Default {@link Connection} implementation using Java NIO.
 * <p>
 * Data is read from the client socket channel.
 */
public class SocketConnection implements Connection {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketConnection.class);

    private final SocketChannel channel;

    public SocketConnection(SocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void read() {
        if (!channel.isOpen()) {
            throw new IllegalStateException("Connection is closed");
        }

        LOGGER.debug("Reading socket channel");
    }

    @Override
    public boolean isOpen() {
        return channel.isOpen();
    }

    @Override
    public void close() throws IOException {
        if (!channel.isOpen()) {
            return;
        }

        // Close the socket channel
        channel.close();
    }
}
