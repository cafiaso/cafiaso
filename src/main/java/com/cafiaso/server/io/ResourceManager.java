package com.cafiaso.server.io;

import com.cafiaso.server.Main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * A utility class for managing files under the {@code resources} directory.
 * <p>
 * This class provides methods to load resources from the classpath, either as an {@link InputStream} or as a {@link File}.
 * </p>
 */
public class ResourceManager {

    /**
     * Get a resource as an {@link InputStream}.
     *
     * @param path the path to the resource (relative to the {@code resources} directory)
     * @return an input stream to the resource
     * @throws IOException if the resource cannot be found
     */
    public InputStream getResourceAsStream(String path) throws IOException {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(path);

        if (inputStream == null) {
            throw new IOException("Resource not found: %s".formatted(path));
        }

        return inputStream;
    }

    /**
     * Get a resource as a {@link File}.
     *
     * @param path the path to the resource (relative to the {@code resources} directory)
     * @return the file
     * @throws IOException if the resource cannot be found
     */
    public File getResourceAsFile(String path) throws IOException {
        URL url = Main.class.getClassLoader().getResource(path);

        if (url == null) {
            throw new IOException("Resource not found: %s".formatted(path));
        }

        return new File(url.getFile());
    }
}
