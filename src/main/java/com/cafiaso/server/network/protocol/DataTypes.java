package com.cafiaso.server.network.protocol;

import com.cafiaso.server.network.protocol.types.EnumDataType;
import com.cafiaso.server.network.protocol.types.LongDataType;
import com.cafiaso.server.network.protocol.types.StringDataType;
import com.cafiaso.server.network.protocol.types.UnsignedShortType;
import com.cafiaso.server.network.protocol.types.VarIntDataType;

import java.util.function.Function;

/**
 * A collection of common {@link DataType} instances.
 * <p>
 * This class provides static instances of various data types used in the protocol.
 * It also provides utility methods for creating custom data types.
 */
public class DataTypes {

    public static final DataType<Integer> UNSIGNED_SHORT = new UnsignedShortType();
    public static final DataType<Long> LONG = new LongDataType();
    public static final DataType<String> STRING = new StringDataType();
    public static final DataType<Integer> VAR_INT = new VarIntDataType();

    public static DataType<String> STRING(int maxLength) {
        return new StringDataType(maxLength);
    }

    public static <E extends Enum<E>, T, D extends DataType<T>> DataType<E> ENUM(Class<E> enumClass, D type, Function<E, T> getter) {
        return new EnumDataType<>(enumClass, type, getter);
    }
}
