package com.cafiaso.server.configuration;

import com.cafiaso.server.io.ResourceManager;
import jakarta.inject.Inject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * {@link ServerConfiguration} implementation that reads the configuration from a properties file.
 */
public class PropertiesServerConfiguration implements ServerConfiguration {

    private static final String CONFIGURATION_FILE = "server.properties";

    private final Properties properties = new Properties();

    private final ResourceManager resourceManager;

    @Inject
    public PropertiesServerConfiguration(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    @Override
    public void load() {
        try (InputStream input = resourceManager.getResourceAsStream(CONFIGURATION_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load server configuration", e);
        }
    }

    @Override
    public int getMaxPlayers() {
        return Integer.parseInt(properties.getProperty("max-players", String.valueOf(DEFAULT_MAX_PLAYERS)));
    }

    @Override
    public String getDescription() {
        return properties.getProperty("description", DEFAULT_DESCRIPTION);
    }

    @Override
    public String getFavicon() {
        return properties.getProperty("favicon", DEFAULT_FAVICON);
    }
}
