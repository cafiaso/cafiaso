package com.cafiaso.server.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HexUtilsTest {

    @Test
    void toHexString_OK() {
        int n = 255;

        assertEquals("0xff", HexUtils.toHexString(n));
    }

    @Test
    void toHexStringByteArray_OK() {
        byte[] b = {0x01, 0x02, 0x03};

        assertEquals("0x01 0x02 0x03", HexUtils.toHexString(b, b.length));
    }
}
