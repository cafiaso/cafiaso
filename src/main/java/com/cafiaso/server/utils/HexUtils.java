package com.cafiaso.server.utils;

import java.util.StringJoiner;

/**
 * Utility class for hexadecimal operations.
 */
public class HexUtils {

    private static final String HEX_FORMAT = "0x%02x";

    /**
     * Converts an integer to a hexadecimal string.
     * <p>
     * Example:
     * <pre>{@code
     *     int n = 255;
     *     String hex = IntegerUtils.toHexString(n);
     *     System.out.println(hex); // Output: 0xff
     * }</pre>
     *
     * @param n the integer to convert
     * @return the hexadecimal string
     */
    public static String toHexString(int n) {
        return HEX_FORMAT.formatted(n);
    }

    /**
     * Converts a byte array to a hexadecimal string.
     * <p>
     * Example:
     * <pre>{@code
     *     byte[] b = {0x01, 0x02, 0x03};
     *     String hex = HexUtils.toHexString(b);
     *     System.out.println(hex); // Output: 0x01 0x02 0x03
     * }</pre>
     *
     * @param b the byte array to convert
     * @return the hexadecimal string
     */
    public static String toHexString(byte[] b, int length) {
        StringJoiner joiner = new StringJoiner(" ");

        for (int i = 0; i < length; i++) {
            joiner.add(HEX_FORMAT.formatted(b[i]));
        }

        return joiner.toString();
    }
}
