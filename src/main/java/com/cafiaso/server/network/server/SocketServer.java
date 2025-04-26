package com.cafiaso.server.network.server;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.SocketConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default {@link NetworkServer} implementation using Java NIO.
 * <p>
 * This server listens for incoming connections and creates a new thread for each connection to handle communication.
 */
public class SocketServer implements NetworkServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketServer.class);

    private static final int TIMEOUT = 5000;

    private ServerSocketChannel channel;

    private final Set<Connection> connections = ConcurrentHashMap.newKeySet();

    @Override
    public void bind(String host, int port) throws Exception {
        channel = ServerSocketChannel.open();
        channel.bind(new InetSocketAddress(host, port));

        LOGGER.info("Listening connections on {}", channel.getLocalAddress());

        // Listen to connections in a separate thread
        new Thread(() -> {
            while (true) {
                try {
                    // Accept a new connection
                    SocketChannel clientChannel = channel.accept();

                    LOGGER.info("Accepted connection from {}", clientChannel.getRemoteAddress());

                    // Client socket configuration
                    Socket socket = clientChannel.socket();
                    socket.setSoTimeout(TIMEOUT); // If the client doesn't send data within TIMEOUT milliseconds, close the connection
                    socket.setTcpNoDelay(true); // Disable Nagle's algorithm (packets are sent immediately)

                    Connection connection = new SocketConnection(clientChannel);
                    connections.add(connection);

                    // Start a new thread to read from the connection
                    new Thread(() -> {
                        while (connection.isOpen()) {
                            try {
                                connection.read();
                            } catch (Exception e) {
                                LOGGER.error("Failed to read from connection {}", connection, e);
                            }
                        }
                    }).start();
                } catch (IOException e) {
                    LOGGER.error("Failed to accept connection", e);
                }
            }
        }).start();
    }

    @Override
    public void close() throws Exception {
        if (channel == null || !channel.isOpen()) {
            return;
        }

        for (Connection connection : connections) {
            try {
                connection.close();
            } catch (IOException e) {
                // Log the error but continue closing other connections
                LOGGER.error("Failed to close connection {}", connection, e);
            }
        }

        channel.close();

        LOGGER.info("Stopped listening for connections");
    }
}
