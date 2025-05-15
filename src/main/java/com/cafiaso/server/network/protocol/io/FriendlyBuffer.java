package com.cafiaso.server.network.protocol.io;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.utils.EncryptionUtils;
import com.cafiaso.server.utils.HexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A {@link ByteBuffer} wrapper that provides additional functionality for reading and writing data.
 * <p>
 * This class is designed to be used by {@link com.cafiaso.server.network.protocol.reader.PacketReader}s
 * and {@link com.cafiaso.server.network.protocol.packets.client.PacketDeserializer}s to respectively
 * read and write data to/from the client.
 * <p>
 * Example usage:
 * <pre>{@code
 * FriendlyBuffer buffer = new FriendlyBuffer();
 * buffer.writeByte((byte) 42);
 * byte value = buffer.readByte();
 * }</pre>
 */
public class FriendlyBuffer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FriendlyBuffer.class);

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    private final ByteBuffer buffer;

    public FriendlyBuffer() {
        this(DEFAULT_BUFFER_SIZE);
    }

    public FriendlyBuffer(int capacity) {
        buffer = ByteBuffer.allocate(capacity);
    }

    public FriendlyBuffer(byte[] data) {
        buffer = ByteBuffer.wrap(data);
    }

    public void encrypt(SecretKey sharedSecret) throws IOException {
        LOGGER.debug("Encrypting buffer with shared secret");

        flip(); // Switch to write mode to get buffer contents

        byte[] decryptedData = toByteArray();

        LOGGER.debug("Decrypted data: {}", HexUtils.toHexString(decryptedData));

        if (sharedSecret != null) {
            // Encrypt the packet buffer using the shared secret
            byte[] encryptedData = EncryptionUtils.encrypt(decryptedData, sharedSecret);

            LOGGER.debug("Encrypted data: {}", HexUtils.toHexString(encryptedData));

            // Clear the buffer and write the encrypted data
            buffer.clear();
            buffer.put(encryptedData);
        }

        flip(); // Switch back to read mode to read the encrypted data and write it to the connection
    }

    public void decrypt(SecretKey sharedSecret) throws IOException {
        LOGGER.debug("Decrypting buffer with shared secret");

        flip(); // Switch to write mode to get buffer contents

        byte[] encryptedData = toByteArray();

        LOGGER.debug("Encrypted data: {}", HexUtils.toHexString(encryptedData));

        if (sharedSecret != null) {
            // Decrypt the packet buffer using the shared secret
            byte[] decryptedData = EncryptionUtils.decrypt(encryptedData, sharedSecret);

            LOGGER.debug("Decrypted data: {}", HexUtils.toHexString(decryptedData));

            // Clear the buffer and write the decrypted data
            buffer.clear();
            buffer.put(decryptedData);
        }

        flip(); // Switch back to read mode to read the decrypted data
    }

    /**
     * Gets the underlying {@link ByteBuffer} of this {@link FriendlyBuffer}.
     * <p>
     * <u>Note</u>: This method should be used with caution, as it exposes the internal buffer.
     * It is recommended to use the standard methods for reading and writing data to ensure
     * correct behavior. It is primarily intended for advanced read and write operations that take a
     * {@link ByteBuffer} as an argument (e.g., {@link java.nio.channels.SocketChannel}).
     *
     * @return the underlying {@link ByteBuffer}
     */
    public ByteBuffer getBuffer() {
        return buffer;
    }

    /**
     * Copies the specified number of bytes from the buffer.
     *
     * @param length the number of bytes to copy
     * @return a new {@link FriendlyBuffer} containing the copied bytes
     */
    public FriendlyBuffer copy(int length) {
        byte[] bytes = new byte[length];
        buffer.get(bytes);

        return new FriendlyBuffer(bytes);
    }

    /**
     * Reads a byte from the buffer.
     *
     * @return the byte read from the buffer
     */
    public byte readByte() {
        return buffer.get();
    }

    /**
     * Reads a long from the buffer.
     *
     * @return the long read from the buffer
     */
    public long readLong() {
        return buffer.getLong();
    }

    /**
     * Reads an unsigned short from the buffer.
     *
     * @return the unsigned short read from the buffer
     */
    public int readUnsignedShort() {
        return buffer.getShort() & 0xFFFF;
    }

    /**
     * Reads a string from the buffer.
     *
     * @param length the length of the string to read
     * @return the string read from the buffer
     */
    public String readString(int length) {
        return new String(readBytes(length));
    }

    /**
     * Reads the specified number of bytes from the buffer.
     *
     * @param length the number of bytes to read
     * @return the byte array containing the read bytes
     */
    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        buffer.get(bytes);

        return bytes;
    }

    /**
     * Reads a value from the buffer.
     * <p>
     * Example:
     * <pre>{@code
     * class Packet implements ClientPacket {
     *   private int timestamp;
     *
     *   @Override
     *   public void read(InputStream in) throws IOException {
     *     timestamp = in.read(DataType.VAR_INT);
     *   }
     * }
     * }</pre>
     *
     * @param type the data type to read
     * @param <T>  the type of the data
     * @return the value read from the buffer
     * @throws IOException if an I/O error occurs while reading the data
     * @see DataType
     */
    public <T> T read(DataType<T> type) throws IOException {
        return type.read(this);
    }

    /**
     * Writes a byte to the buffer.
     *
     * @param value the byte to write
     */
    public void writeByte(byte value) {
        buffer.put(value);
    }

    /**
     * Writes an unsigned short to the buffer.
     *
     * @param value the unsigned short to write
     */
    public void writeUnsignedShort(int value) {
        buffer.putShort((short) (value & 0xffff));
    }

    /**
     * Writes a long to the buffer.
     *
     * @param value the long to write
     */
    public void writeLong(long value) {
        buffer.putLong(value);
    }

    /**
     * Writes a byte array to the buffer.
     *
     * @param bytes the byte array to write
     */
    public void writeBytes(byte[] bytes) {
        buffer.put(bytes);
    }

    /**
     * Writes a value to the buffer.
     * <p>
     * Example:
     * <pre>{@code
     * class Packet implements ServerPacket {
     *   private final int timestamp;
     *
     *   public Packet(int timestamp) {
     *     this.timestamp = timestamp;
     *   }
     *
     *   @Override
     *   public void write(OutputStream out) throws IOException {
     *     out.write(timestamp, DataType.VAR_INT);
     *   }
     * }
     * }</pre>
     *
     * @param value the value to write
     * @param type  the data type to write
     * @param <T>   the type of the data
     * @throws IOException if an I/O error occurs while writing the data
     */
    public <T> void write(T value, DataType<T> type) throws IOException {
        type.write(value, this);
    }

    /**
     * Flips the given buffer and writes its contents to this buffer.
     *
     * @param buffer the buffer to write from
     */
    public void write(FriendlyBuffer buffer) {
        buffer.flip();

        this.buffer.put(buffer.buffer);
    }

    /**
     * Checks if the buffer has remaining bytes.
     *
     * @return {@code true} if there are remaining bytes, {@code false} otherwise
     */
    public boolean hasRemainingBytes() {
        return buffer.hasRemaining();
    }

    /**
     * Gets the number of remaining bytes in the buffer.
     *
     * @return the number of remaining bytes
     */
    public int getRemainingBytes() {
        return buffer.remaining();
    }

    /**
     * Flips the buffer.
     * <p>
     * This method sets the limit to the current position and the position to zero.
     */
    public void flip() {
        buffer.flip();
    }

    /**
     * Gets the current position of the buffer.
     *
     * @return the current position
     */
    public int getPosition() {
        return buffer.position();
    }

    /**
     * Marks the current position of the buffer.
     */
    public void mark() {
        buffer.mark();
    }

    /**
     * Resets the buffer to the marked position.
     */
    public void reset() {
        buffer.reset();
    }

    /**
     * Compacts the buffer.
     */
    public void compact() {
        buffer.compact();
    }

    /**
     * Gets the contents of the buffer as a byte array (from 0 to limit).
     *
     * @return the byte array containing the buffer contents
     */
    public byte[] toByteArray() {
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);

        return bytes;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof FriendlyBuffer that)) {
            return false;
        }

        ByteBuffer thisCopy = this.buffer.asReadOnlyBuffer();
        ByteBuffer thatCopy = that.buffer.asReadOnlyBuffer();

        if (thisCopy.limit() != thatCopy.limit()) {
            return false;
        }

        thisCopy.position(0);
        thatCopy.position(0);

        while (thisCopy.hasRemaining()) {
            if (thisCopy.get() != thatCopy.get()) {
                return false;
            }
        }

        return true;
    }
}
