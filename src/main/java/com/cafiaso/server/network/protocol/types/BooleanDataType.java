package com.cafiaso.server.network.protocol.types;

import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;

import java.io.IOException;

public class BooleanDataType implements DataType<Boolean> {

    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }

    @Override
    public Boolean read(FriendlyBuffer in) throws IOException {
        byte value = in.readByte();

        if (value == 0x00) {
            return false;
        } else if (value == 0x01) {
            return true;
        } else {
            throw new IOException("Invalid value");
        }
    }

    @Override
    public void write(Boolean value, FriendlyBuffer out) throws IOException {
        out.writeByte((byte) (value ? 0x01 : 0x00));
    }
}
