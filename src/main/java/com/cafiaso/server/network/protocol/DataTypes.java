package com.cafiaso.server.network.protocol;

import com.cafiaso.server.network.protocol.types.BooleanDataType;
import com.cafiaso.server.network.protocol.types.EnumDataType;
import com.cafiaso.server.network.protocol.types.LongDataType;
import com.cafiaso.server.network.protocol.types.PrefixedArrayDataType;
import com.cafiaso.server.network.protocol.types.PrefixedOptionalDataType;
import com.cafiaso.server.network.protocol.types.StringDataType;
import com.cafiaso.server.network.protocol.types.UUIDDataType;
import com.cafiaso.server.network.protocol.types.UnsignedShortType;
import com.cafiaso.server.network.protocol.types.VarIntDataType;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * A collection of common {@link DataType} instances.
 * <p>
 * This class provides static instances of various data types used in the protocol.
 * It also provides utility methods for creating custom data types.
 */
public class DataTypes {

    // Primitive types
    public static final DataType<Boolean> BOOLEAN = new BooleanDataType();
    public static final DataType<Integer> UNSIGNED_SHORT = new UnsignedShortType();
    public static final DataType<Long> LONG = new LongDataType();
    public static final DataType<String> STRING = new StringDataType();
    public static final DataType<Integer> VAR_INT = new VarIntDataType();
    public static final DataType<UUID> UUID = new UUIDDataType();

    // Usual array types
    public static final DataType<byte[]> PREFIXED_BYTE_ARRAY = new PrefixedArrayDataType.ByteArray();

    /**
     * Creates a new {@link StringDataType} with the specified maximum length.
     *
     * @param maxLength the maximum length of the string
     * @return a new {@link StringDataType}
     */
    public static DataType<String> STRING(int maxLength) {
        return new StringDataType(maxLength);
    }

    /**
     * Creates a new {@link PrefixedOptionalDataType} with the specified data type.
     * <p>
     * Example usage for a {@code Property} optional:
     * <pre>{@code
     * DataType<Optional<Property>> type = DataTypes.PREFIXED_OPTIONAL(Property.TYPE);
     * }</pre>
     *
     * @param type the {@link DataType} for the optional value
     * @param <T>  the type of the data
     * @param <D>  the DataType for the data
     * @return a new {@link PrefixedOptionalDataType}
     */
    public static <T, D extends DataType<T>> DataType<Optional<T>> PREFIXED_OPTIONAL(D type) {
        return new PrefixedOptionalDataType<>(type);
    }

    /**
     * Creates a new {@link PrefixedArrayDataType} with the specified data type.
     * <p>
     * <u>Note</u>: DO NOT use this method for {@code Byte[]} arrays.
     * It will cause performance issues and is not the intended use case.
     * Use {@link #PREFIXED_BYTE_ARRAY} instead.
     * <p>
     * Example usage for a {@code Property} array:
     * <pre>{@code
     * DataType<Property[]> type = DataTypes.PREFIXED_ARRAY(DataTypes.Property);
     * }</pre>
     *
     * @param type the {@link DataType} for the array elements
     * @param <T>  the type of the data
     * @param <D>  the DataType for the data
     * @return a new {@link PrefixedArrayDataType}
     */
    public static <T, D extends DataType<T>> DataType<T[]> PREFIXED_ARRAY(D type) {
        return new PrefixedArrayDataType<>(type);
    }

    /**
     * Creates a new {@link EnumDataType} for the given enum class and data type.
     * <p>
     * This is simply a convenience method for creating an {@link EnumDataType} without
     * explicitly specifying a mapper function (which is not needed in most cases).
     * <p>
     * Example usage for a {@code VarInt} enum:
     * <pre>{@code
     * public enum TestEnum implements Supplier<Integer> {
     *   A(0x00),
     *   B(0x01),
     *   C(0x02);
     *
     *   private final int value;
     *
     *   TestEnum(int value) {
     *     this.value = value;
     *   }
     *
     *   @Override
     *   public Integer get() {
     *     return value;
     *   }
     * }
     *
     * DataType<TestEnum> type = DataTypes.ENUM(TestEnum.class, DataTypes.VAR_INT);
     * }</pre>
     *
     * @param enumClass the enum class (must implement {@link Supplier})
     * @param type      the {@link DataType} for the enum values
     * @param <E>       the enum type
     * @param <T>       the type of the data
     * @param <D>       the DataType for the data
     * @return a new {@link EnumDataType}
     */
    public static <E extends Enum<E> & Supplier<T>, T, D extends DataType<T>> DataType<E> ENUM(Class<E> enumClass, D type) {
        return new EnumDataType<>(enumClass, type, Supplier::get);
    }
}
