package com.cafiaso.server.network.connection;

import com.cafiaso.server.network.protocol.io.FriendlyBuffer;

import java.io.IOException;
import java.net.Socket;

/**
 * Default {@link Connection} implementation using {@link Socket} to write and read data.
 */
public class SocketConnection extends Connection {

    private final Socket socket;

    public SocketConnection(Socket socket) {
        this.socket = socket;
    }

    @Override
    public int read(FriendlyBuffer buffer) throws IOException {
        return socket.getChannel().read(buffer.getBuffer());
    }

    @Override
    public void write(FriendlyBuffer buffer) throws IOException {
        socket.getChannel().write(buffer.getBuffer());
    }

    @Override
    public String getHostAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    @Override
    public boolean isOpen() {
        return !socket.isClosed();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public String toString() {
        return socket.getInetAddress().toString();
    }
}
