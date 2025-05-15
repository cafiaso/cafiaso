package com.cafiaso.server.network.protocol.types;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;

import java.io.IOException;

/**
 * A {@link DataType} for {@link String} values.
 * <p>
 * Strings are values encoded as a length-prefixed byte array. The length of the string is encoded as a
 * {@code VarInt}.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Type:String">String Type</a>
 */
public class StringDataType implements DataType<String> {

    public static final int MAX_LENGTH = 3 * 32767 + 3; // 3 * number of characters in a string + 3 (VarInt maximum size)

    private final int maxLength;

    public StringDataType(int maxLength) {
        this.maxLength = maxLength;
    }

    public StringDataType() {
        this(MAX_LENGTH);
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String read(FriendlyBuffer in) throws IOException {
        // Read the length of the string
        int length = in.read(DataTypes.VAR_INT);

        if (length > maxLength) {
            throw new IOException("String is too long. Waiting for maximum %d characters, received %d.".formatted(maxLength, length));
        }

        // Read the content itself
        return in.readString(length);
    }

    @Override
    public void write(String value, FriendlyBuffer out) throws IOException {
        byte[] bytes = value.getBytes();

        // Write the length of the string
        DataTypes.VAR_INT.write(bytes.length, out);

        // Write the content itself
        out.writeBytes(bytes);
    }
}
