package com.cafiaso.server.network.protocol;

import com.cafiaso.server.network.connection.Connection;

import java.io.IOException;

/**
 * Represents a handler for a packet sent by the client to the server.
 * <p>
 * The handler is responsible for processing the packet and sending a response back to the client if necessary
 * using {@link com.cafiaso.server.network.protocol.dispatcher.PacketDispatcher}.
 *
 * @param <P> the packet type
 */
public interface PacketHandler<P> {

    /**
     * Handles the given packet.
     *
     * @param packet     the packet to handle
     * @param connection the connection that sent the packet
     * @throws IOException if an I/O error occurs while handling the packet
     */
    void handle(P packet, Connection connection) throws IOException;
}
