package com.cafiaso.server.network.protocol.processor;

import com.cafiaso.server.network.connection.ConnectionState;
import com.cafiaso.server.utils.HexUtils;

/**
 * Exception thrown when an unknown packet is received.
 * <p>
 * This usually indicates that the packet ID is not registered for the current connection state.
 */
public class UnknownPacketException extends RuntimeException {

    public UnknownPacketException(int packetId, ConnectionState state) {
        super(
                "Received unknown packet with id %s. Please check the packet id mapping for state %s."
                        .formatted(HexUtils.toHexString(packetId), state)
        );
    }
}
