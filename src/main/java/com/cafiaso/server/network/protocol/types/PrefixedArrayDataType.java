package com.cafiaso.server.network.protocol.types;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;

import java.io.IOException;
import java.lang.reflect.Array;

@SuppressWarnings("unchecked")
public class PrefixedArrayDataType<T, D extends DataType<T>> implements DataType<T[]> {

    private final D type;

    public PrefixedArrayDataType(D type) {
        this.type = type;
    }

    @Override
    public Class<T[]> getType() {
        return (Class<T[]>) Array.newInstance(type.getType(), 0).getClass();
    }

    @Override
    public T[] read(FriendlyBuffer in) throws IOException {
        int length = in.read(DataTypes.VAR_INT);
        T[] array = (T[]) Array.newInstance(type.getType(), length);

        for (int i = 0; i < length; i++) {
            array[i] = type.read(in);
        }

        return array;
    }

    @Override
    public void write(T[] value, FriendlyBuffer out) throws IOException {
        out.write(value.length, DataTypes.VAR_INT);

        for (T item : value) {
            type.write(item, out);
        }
    }

    /**
     * For performance (avoid reading byte after byte) and simplicity (avoid unboxing and boxing) reasons, we
     * use a dedicated {@link DataType} for byte arrays.
     */
    public static class ByteArray implements DataType<byte[]> {

        @Override
        public Class<byte[]> getType() {
            return byte[].class;
        }

        @Override
        public byte[] read(FriendlyBuffer in) throws IOException {
            int length = in.read(DataTypes.VAR_INT);

            return in.readBytes(length);
        }

        @Override
        public void write(byte[] value, FriendlyBuffer out) throws IOException {
            out.write(value.length, DataTypes.VAR_INT);
            out.writeBytes(value);
        }
    }
}
