package com.cafiaso.server.network.protocol.types;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;

import java.io.IOException;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class PrefixedOptionalDataType<T, D extends DataType<T>> implements DataType<Optional<T>> {

    private final D type;

    public PrefixedOptionalDataType(D type) {
        this.type = type;
    }

    @Override
    public Class<Optional<T>> getType() {
        return (Class<Optional<T>>) (Class<?>) Optional.class;
    }

    @Override
    public Optional<T> read(FriendlyBuffer in) throws IOException {
        boolean present = in.read(DataTypes.BOOLEAN);

        if (!present) {
            return Optional.empty();
        }

        return Optional.of(type.read(in));
    }

    @Override
    public void write(Optional<T> value, FriendlyBuffer out) throws IOException {
        out.write(value.isPresent(), DataTypes.BOOLEAN);

        if (value.isPresent()) {
            type.write(value.get(), out);
        }
    }
}
