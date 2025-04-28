package com.cafiaso.server.configuration;

/**
 * An immutable configuration for the server.
 * <p>
 * The configuration should not be considered fully usable until calling {@link #load()}.
 * <p>
 * Implementations must provide a default value for each configuration option as specified in the method documentation,
 * even if it has not been loaded yet.
 */
public interface ServerConfiguration {

    int DEFAULT_MAX_PLAYERS = 20;

    String DEFAULT_DESCRIPTION = "A Minecraft Server";

    String DEFAULT_FAVICON = "icon.png";

    /**
     * Loads the configuration.
     */
    void load();

    /**
     * Gets the maximum number of players that can be connected to the server.
     * <p>
     * Defaults to {@code DEFAULT_MAXIMUM_PLAYERS}
     *
     * @return the maximum number of players
     */
    int getMaxPlayers();

    /**
     * Gets the description (motd) of the server.
     * <p>
     * Defaults to {@code DEFAULT_DESCRIPTION}
     *
     * @return the description of the server
     */
    String getDescription();

    /**
     * Gets the favicon of the server.
     * <p>
     * Defaults to {@code DEFAULT_FAVICON}
     *
     * @return the favicon of the server
     */
    String getFavicon();
}
