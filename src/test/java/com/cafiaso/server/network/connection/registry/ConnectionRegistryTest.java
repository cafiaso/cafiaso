package com.cafiaso.server.network.connection.registry;

import com.cafiaso.server.network.connection.Connection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConnectionRegistryTest {

    private ConnectionRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new ConnectionRegistry();
    }

    @Test
    void addConnection_OK() {
        Connection connection = mock(Connection.class);

        registry.addConnection(connection);

        assertEquals(connection, registry.getConnections().iterator().next());
    }

    @Test
    void closeConnection_OK() throws IOException {
        Connection connection = mock(Connection.class);

        registry.addConnection(connection);
        registry.closeConnection(connection);

        verify(connection).close();
    }

    @Test
    void closeConnection_ShouldNotThrowException_WhenOneConnectionClosureFailed() throws IOException {
        Connection connection = mock(Connection.class);
        doThrow(new IOException("forced")).when(connection).close();

        registry.addConnection(connection);
        registry.closeConnection(connection);

        // Verify that the connection was closed, even if it threw an exception
        verify(connection).close();
    }

    @Test
    void closeAllConnections_OK() throws IOException {
        Connection connection1 = mock(Connection.class);
        Connection connection2 = mock(Connection.class);

        registry.addConnection(connection1);
        registry.addConnection(connection2);

        registry.closeAllConnections();

        verify(connection1).close();
        verify(connection2).close();
    }

    @Test
    void closeAllConnections_ShouldCloseAllConnection_WhenOneFailed() throws IOException {
        Connection connection1 = mock(Connection.class);
        Connection connection2 = mock(Connection.class);

        doThrow(new IOException("fail")).when(connection1).close();

        registry.addConnection(connection1);
        registry.addConnection(connection2);

        registry.closeAllConnections();

        verify(connection1).close();
        verify(connection2).close();
    }
}

