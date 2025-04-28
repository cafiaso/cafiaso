package com.cafiaso.server.network.protocol.packets.client;

import com.cafiaso.server.network.protocol.io.FriendlyBuffer;

import java.io.IOException;

/**
 * A deserializer for a packet sent by the client to the server.
 * <p>
 * Packets must be registered in {@link com.cafiaso.server.network.connection.ConnectionState}.
 * <p>
 * Packet deserialization is done by reading the data from a {@link FriendlyBuffer}.
 * <p>
 * Each packet must have a unique ID for a given {@link com.cafiaso.server.network.connection.ConnectionState}.
 *
 * @param <T> the packet type
 */
public interface PacketDeserializer<T> {

    /**
     * Gets the packet ID.
     * <p>
     * The ID must be unique for a given {@link com.cafiaso.server.network.connection.ConnectionState}.
     *
     * @return the packet ID
     */
    int getId();

    /**
     * Deserializes a packet from the given {@link FriendlyBuffer}.
     *
     * @param in the input to read from
     * @return the deserialized packet
     * @throws IOException if an I/O error occurs while reading the packet data
     */
    T deserialize(FriendlyBuffer in) throws IOException;
}
