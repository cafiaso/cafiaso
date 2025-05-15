package com.cafiaso.server.network.protocol;

import com.cafiaso.server.network.protocol.io.FriendlyBuffer;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A generic interface for data types used in the protocol.
 * <p>
 * Each data type is responsible for reading and writing its value
 * to and from a {@link FriendlyBuffer} and {@link DataOutputStream}.
 * <p>
 * Data type instances are available in {@link DataTypes} for easy access.
 *
 * @param <T> the type of data to be read and written
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Data_types">Protocol documentation</a>
 */
public interface DataType<T> {

    /**
     * Gets the class type of the data.
     *
     * @return the class type of the data
     */
    Class<T> getType();

    /**
     * Reads a value of type {@code T} from the given {@link FriendlyBuffer}.
     *
     * @param in the buffer to read from
     * @return the read value
     * @throws IOException if an I/O error occurs
     */
    T read(FriendlyBuffer in) throws IOException;

    /**
     * Writes a value of type {@code T} to the given {@link DataOutputStream}.
     *
     * @param value the value to write
     * @param out   the output stream to write to
     * @throws IOException if an I/O error occurs
     */
    void write(T value, FriendlyBuffer out) throws IOException;
}
