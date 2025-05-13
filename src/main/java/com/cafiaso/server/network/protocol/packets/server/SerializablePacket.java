package com.cafiaso.server.network.protocol.packets.server;

import com.cafiaso.server.network.connection.ConnectionState;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;

import java.io.IOException;

/**
 * Represents a packet that can be sent to the client using a
 * {@link com.cafiaso.server.network.protocol.dispatcher.PacketDispatcher}.
 * <p>
 * Packet serialization is done by writing the data to a {@link FriendlyBuffer}.
 * <p>
 * Each packet must have a unique ID for a given {@link ConnectionState}.
 */
public interface SerializablePacket {

    /**
     * Gets the packet ID.
     * <p>
     * The ID must be unique for a given {@link ConnectionState}.
     *
     * @return the packet ID
     */
    int getId();

    /**
     * Serializes the packet data to the given {@link FriendlyBuffer}.
     *
     * @param out the output to write to
     * @throws IOException if an I/O error occurs while writing the packet data
     */
    void serialize(FriendlyBuffer out) throws IOException;
}
