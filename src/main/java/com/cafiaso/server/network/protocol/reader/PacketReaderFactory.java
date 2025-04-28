package com.cafiaso.server.network.protocol.reader;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.protocol.processor.PacketProcessor;
import jakarta.inject.Inject;

/**
 * Factory class for creating {@link PacketReader} instances.
 */
public class PacketReaderFactory {

    private final PacketProcessor processor;

    @Inject
    public PacketReaderFactory(PacketProcessor processor) {
        this.processor = processor;
    }

    public PacketReader createPacketReader(Connection connection) {
        return new PacketReader(connection, processor);
    }
}
