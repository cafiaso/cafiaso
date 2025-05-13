package com.cafiaso.server;

import com.cafiaso.server.configuration.ServerConfiguration;
import com.cafiaso.server.io.ResourceManager;
import jakarta.inject.Inject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

/**
 * This class is responsible for loading and providing the server icon.
 */
public class ServerIcon {

    private static final int FAVICON_SIZE = 64;

    private final ServerConfiguration configuration;
    private final ResourceManager resourceManager;

    private String icon;

    @Inject
    public ServerIcon(ServerConfiguration configuration, ResourceManager resourceManager) {
        this.configuration = configuration;
        this.resourceManager = resourceManager;
    }

    /**
     * Loads the server icon from the resource specified in the configuration.
     *
     * @throws IOException if an error occurs while loading the icon
     */
    public void load() throws IOException {
        File faviconFile = resourceManager.getResourceAsFile(configuration.getFavicon());

        BufferedImage favicon = ImageIO.read(faviconFile);
        if (favicon == null || (favicon.getWidth() != FAVICON_SIZE || favicon.getHeight() != FAVICON_SIZE)) {
            throw new IOException("Failed to read favicon image");
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(favicon, "png", out);
            byte[] data = out.toByteArray();

            icon = Base64.getEncoder().encodeToString(data);
        }
    }

    /**
     * Returns the server icon as a base64-encoded string.
     *
     * @return an {@link Optional} containing the base64-encoded icon string, or an empty {@link Optional} if the icon is not loaded
     */
    public Optional<String> get() {
        return Optional.ofNullable(icon)
                .map("data:image/png;base64,"::concat);
    }
}
