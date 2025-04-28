package com.cafiaso.server;

import com.cafiaso.server.configuration.ServerConfiguration;
import com.cafiaso.server.network.server.NetworkServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServerImplTest {

    @Mock
    private NetworkServer networkServer;

    @Mock
    private ServerConfiguration configuration;

    @Mock
    private ServerIcon icon;

    @InjectMocks
    private ServerImpl server;

    @Test
    void start_OK() throws IOException {
        String host = "localhost";
        int port = 25565;

        server.start(host, port);

        verify(networkServer).bind(host, port);

        verify(configuration).load();
        verify(icon).load();
        verify(networkServer).bind(host, port);
    }

    @Test
    void start_ShouldThrowIllegalStateException_WhenServerIsAlreadyRunning() {
        String host = "localhost";
        int port = 25565;

        server.start(host, port);

        assertThrowsExactly(IllegalStateException.class, () -> server.start(host, port));
    }

    @Test
    void stop_OK() throws IOException {
        server.start("localhost", 25565);

        server.stop();

        verify(networkServer).close();
    }

    @Test
    void stop_ShouldNotThrowException_WhenServerIsNotRunning() {
        assertDoesNotThrow(() -> server.stop());
    }
}
