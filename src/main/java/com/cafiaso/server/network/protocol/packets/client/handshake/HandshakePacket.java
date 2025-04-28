package com.cafiaso.server.network.protocol.packets.client.handshake;

import com.cafiaso.server.network.protocol.io.FriendlyBuffer;
import com.cafiaso.server.network.protocol.DataType;
import com.cafiaso.server.network.protocol.DataTypes;
import com.cafiaso.server.network.protocol.packets.client.PacketDeserializer;

import java.io.IOException;

/**
 * Sent by the client to initiate the connection (ping or login).
 *
 * @param protocolVersion the version that the client plans to connect to the server
 * @param serverAddress   the host name that was used to connect
 * @param serverPort      the port that was used to connect
 * @param nextState       the next state that the client wants to switch to
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Handshake">Handshake Packet</a>
 */
public record HandshakePacket(int protocolVersion, String serverAddress, int serverPort, Intention nextState) {

    /**
     * Represents the next state that the client wants to transition to after the handshake.
     */
    public enum Intention {
        /**
         * Switch to {@link com.cafiaso.server.network.connection.ConnectionState#STATUS}
         */
        STATUS(1),
        /**
         * Switch to {@link com.cafiaso.server.network.connection.ConnectionState#LOGIN}
         */
        LOGIN(2),
        /**
         * Not supported by the server.
         */
        TRANSFER(3);

        private final int id;

        Intention(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public static class Deserializer implements PacketDeserializer<HandshakePacket> {

        public static final int MAX_SERVER_ADDRESS_LENGTH = 255;

        public static final DataType<Integer> PROTOCOL_VERSION_TYPE = DataTypes.VAR_INT;
        public static final DataType<String> SERVER_ADDRESS_TYPE = DataTypes.STRING(MAX_SERVER_ADDRESS_LENGTH);
        public static final DataType<Integer> SERVER_PORT_TYPE = DataTypes.UNSIGNED_SHORT;
        public static final DataType<Intention> NEXT_STATE_TYPE = DataTypes.ENUM(Intention.class, DataTypes.VAR_INT, Intention::getId);

        @Override
        public int getId() {
            return 0x00;
        }

        @Override
        public HandshakePacket deserialize(FriendlyBuffer in) throws IOException {
            int protocolVersion = in.read(PROTOCOL_VERSION_TYPE);
            String serverAddress = in.read(SERVER_ADDRESS_TYPE);
            int serverPort = in.read(SERVER_PORT_TYPE);
            Intention nextState = in.read(NEXT_STATE_TYPE);

            return new HandshakePacket(protocolVersion, serverAddress, serverPort, nextState);
        }
    }
}
