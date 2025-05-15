package com.cafiaso.server.network.protocol.dispatcher;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.packets.server.SerializablePacket;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

class PacketDispatcherTest {

    @Test
    void dispatch_OK() throws IOException {
        PacketDispatcher dispatcher = new PacketDispatcher();

        MockServerPacket packet = new MockServerPacket(0x10);

        MockConnection connection = new MockConnection();

        dispatcher.dispatch(packet, connection);

        assertArrayEquals(new byte[]{0x02, 0x00, 0x10}, connection.data);
    }

    private static class MockConnection extends Connection {

        private byte[] data;

        @Override
        public int read(FriendlyBuffer buffer) {
            return 0;
        }

        @Override
        public void write(FriendlyBuffer buffer) {
            data = buffer.toByteArray();
        }

        @Override
        public String getHostAddress() {
            return "";
        }

        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public void close() {

        }
    }

    private record MockServerPacket(int payload) implements SerializablePacket {

        @Override
        public int getId() {
            return 0x00;
        }

        @Override
        public void serialize(FriendlyBuffer out) throws IOException {
            out.write(payload, DataTypes.VAR_INT);
        }
    }
}
