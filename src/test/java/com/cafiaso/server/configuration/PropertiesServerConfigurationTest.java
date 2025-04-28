package com.cafiaso.server.configuration;

import com.cafiaso.server.io.ResourceManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertiesServerConfigurationTest {

    @Mock
    private ResourceManager resourceManager;

    @InjectMocks
    private PropertiesServerConfiguration configuration;

    @Test
    void get_OK() throws IOException {
        String config = """
                max-players=50
                description=A Cafiaso Server
                favicon=AAA=
                """;
        InputStream mockInputStream = new ByteArrayInputStream(config.getBytes());

        when(resourceManager.getResourceAsStream("server.properties")).thenReturn(mockInputStream);

        configuration.load();

        assertEquals(50, configuration.getMaxPlayers());
        assertEquals("A Cafiaso Server", configuration.getDescription());
        assertEquals("AAA=", configuration.getFavicon());
    }

    @Test
    void load_ShouldThrowException_WhenFileNotFound() throws IOException {
        when(resourceManager.getResourceAsStream("server.properties")).thenThrow(new IOException("File not found"));

        assertThrowsExactly(RuntimeException.class, configuration::load, "Failed to load server configuration");
    }

    @Test
    void get_ShouldReturnDefaultValue_WhenPropertyIsMissing() throws IOException {
        String config = """
                max-players=50
                """;
        InputStream mockInputStream = new ByteArrayInputStream(config.getBytes());

        when(resourceManager.getResourceAsStream("server.properties")).thenReturn(mockInputStream);

        configuration.load();

        assertEquals(50, configuration.getMaxPlayers());
        assertEquals(ServerConfiguration.DEFAULT_DESCRIPTION, configuration.getDescription());
    }

    @Test
    void get_ShouldReturnDefaultValues_WhenFileIsEmpty() throws IOException {
        String config = "";
        InputStream mockInputStream = new ByteArrayInputStream(config.getBytes());

        when(resourceManager.getResourceAsStream("server.properties")).thenReturn(mockInputStream);

        configuration.load();

        assertEquals(ServerConfiguration.DEFAULT_MAX_PLAYERS, configuration.getMaxPlayers());
        assertEquals(ServerConfiguration.DEFAULT_DESCRIPTION, configuration.getDescription());
    }

    @Test
    void get_ShouldReturnDefaultValues_WhenConfigurationIsNotLoaded() {
        assertEquals(ServerConfiguration.DEFAULT_MAX_PLAYERS, configuration.getMaxPlayers());
        assertEquals(ServerConfiguration.DEFAULT_DESCRIPTION, configuration.getDescription());
    }
}
