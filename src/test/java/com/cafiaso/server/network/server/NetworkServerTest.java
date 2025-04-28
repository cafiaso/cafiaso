package com.cafiaso.server.network.server;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.registry.ConnectionRegistry;
import com.cafiaso.server.network.protocol.reader.PacketReader;
import com.cafiaso.server.network.protocol.reader.PacketReaderFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.*;

class NetworkServerTest {

    private ExecutorService executor;
    private ConnectionRegistry connectionRegistry;
    private PacketReaderFactory packetReaderFactory;
    private NetworkServer server;

    @BeforeEach
    void setUp() {
        executor = Executors.newSingleThreadExecutor(); // pour tests simples multi-threads
        connectionRegistry = mock(ConnectionRegistry.class);
        packetReaderFactory = mock(PacketReaderFactory.class);

        server = new NetworkServer(executor, connectionRegistry, packetReaderFactory) {
            @Override
            public void bind(String host, int port) {
                // no-op for test
            }
        };
    }

    @AfterEach
    void tearDown() {
        executor.shutdownNow(); // Nettoyage threads
    }

    @Test
    void close_shouldCloseAllConnections() throws IOException {
        server.close();
        verify(connectionRegistry).closeAllConnections();
    }

    @Test
    void acceptConnection_shouldRegisterAndReadOnce() throws Exception {
        Connection connection = mock(Connection.class);
        when(connection.isOpen()).thenReturn(true, false); // Une seule itération

        PacketReader packetReader = mock(PacketReader.class);
        when(packetReaderFactory.createPacketReader(connection)).thenReturn(packetReader);

        server.acceptConnection(connection);

        // Laisser au thread le temps de s’exécuter
        Thread.sleep(100);

        verify(connectionRegistry).addConnection(connection);
        verify(packetReader).read();
        verify(connectionRegistry, never()).closeConnection(connection);
    }

    @Test
    void acceptConnection_shouldCloseConnection_OnReadFailure() throws Exception {
        Connection connection = mock(Connection.class);
        when(connection.isOpen()).thenReturn(true, false); // Une seule itération

        PacketReader packetReader = mock(PacketReader.class);
        when(packetReaderFactory.createPacketReader(connection)).thenReturn(packetReader);
        doThrow(new IOException("fail")).when(packetReader).read();

        server.acceptConnection(connection);

        Thread.sleep(100);

        verify(connectionRegistry).addConnection(connection);
        verify(packetReader).read();
        verify(connectionRegistry).closeConnection(connection);
    }
}
