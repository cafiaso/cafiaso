package com.cafiaso.server.network.protocol.types;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EnumDataTypeTest {

    private static final EnumDataType<City, Integer, DataType<Integer>> ENUM_VAR_INT_DATA_TYPE = new EnumDataType<>(City.class, DataTypes.VAR_INT, City::getDistrict);
    private static final EnumDataType<City, String, DataType<String>> ENUM_STRING_DATA_TYPE = new EnumDataType<>(City.class, DataTypes.STRING, City::getName);

    @Test
    void readEnumVarInt_OK() throws IOException {
        byte[] bytes1 = {0x2a}; // 42
        FriendlyBuffer in1 = new FriendlyBuffer(bytes1);

        City value1 = ENUM_VAR_INT_DATA_TYPE.read(in1);

        assertEquals(City.SAINT_ETIENNE, value1);

        byte[] bytes2 = new byte[]{0x45}; // 69
        FriendlyBuffer in2 = new FriendlyBuffer(bytes2);

        City value2 = ENUM_VAR_INT_DATA_TYPE.read(in2);

        assertEquals(City.LYON, value2);

        byte[] bytes3 = new byte[]{0x00}; // 0

        FriendlyBuffer in3 = new FriendlyBuffer(bytes3);

        IOException exception = assertThrowsExactly(IOException.class, () -> ENUM_VAR_INT_DATA_TYPE.read(in3));
        assertEquals("Invalid enum value", exception.getMessage());
    }

    @Test
    void read_EnumString_OK() throws IOException {
        byte[] bytes1 = {4, 'l', 'y', 'o', 'n'};
        FriendlyBuffer in1 = new FriendlyBuffer(bytes1);

        City value1 = ENUM_STRING_DATA_TYPE.read(in1);

        assertEquals(City.LYON, value1);

        byte[] bytes2 = new byte[]{13, 's', 'a', 'i', 'n', 't', '_', 'e', 't', 'i', 'e', 'n', 'n', 'e'};
        FriendlyBuffer in2 = new FriendlyBuffer(bytes2);

        City value2 = ENUM_STRING_DATA_TYPE.read(in2);

        assertEquals(City.SAINT_ETIENNE, value2);
    }

    @Test
    void writeEnumVarInt_OK() throws IOException {
        FriendlyBuffer out1 = new FriendlyBuffer();

        City value1 = City.SAINT_ETIENNE;

        ENUM_VAR_INT_DATA_TYPE.write(value1, out1);

        out1.flip();

        assertArrayEquals(new byte[]{0x2a}, out1.toByteArray()); // 42

        FriendlyBuffer out2 = new FriendlyBuffer();

        City value2 = City.LYON;

        ENUM_VAR_INT_DATA_TYPE.write(value2, out2);

        out2.flip();

        assertArrayEquals(new byte[] {0x45}, out2.toByteArray()); // 69
    }

    @Test
    void writeEnumString_OK() throws IOException {
        City value = City.LYON;

        FriendlyBuffer out1 = new FriendlyBuffer();

        ENUM_STRING_DATA_TYPE.write(value, out1);

        out1.flip();

        assertArrayEquals(new byte[]{4, 'l', 'y', 'o', 'n'}, out1.toByteArray());

        value = City.SAINT_ETIENNE;

        FriendlyBuffer out2 = new FriendlyBuffer();

        ENUM_STRING_DATA_TYPE.write(value, out2);

        out2.flip();

        assertArrayEquals(new byte[]{13, 's', 'a', 'i', 'n', 't', '_', 'e', 't', 'i', 'e', 'n', 'n', 'e'}, out2.toByteArray());
    }

    @Test
    void read_ShouldThrowException_WhenEnumIsInvalid() {
        byte[] bytes = new byte[]{5, 'p', 'a', 'r', 'i', 's'};
        FriendlyBuffer in = new FriendlyBuffer(bytes);

        IOException exception = assertThrowsExactly(IOException.class, () -> ENUM_STRING_DATA_TYPE.read(in));
        assertEquals("Invalid enum value", exception.getMessage());
    }

    private enum City {
        LYON(69, "lyon"),
        SAINT_ETIENNE(42, "saint_etienne"),;

        private final int district;
        private final String name;

        City(int district, String name) {
            this.district = district;
            this.name = name;
        }

        public int getDistrict() {
            return district;
        }

        public String getName() {
            return name;
        }
    }
}
