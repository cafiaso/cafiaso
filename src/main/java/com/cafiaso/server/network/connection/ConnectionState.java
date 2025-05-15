package com.cafiaso.server.network.connection;

import com.cafiaso.server.network.protocol.PacketHandler;
import com.cafiaso.server.network.protocol.handlers.configuration.ServerboundKnownPacksHandler;
import com.cafiaso.server.network.protocol.handlers.handshake.HandshakePacketHandler;
import com.cafiaso.server.network.protocol.handlers.handshake.LegacyServerListPingPacketHandler;
import com.cafiaso.server.network.protocol.handlers.login.EncryptionResponsePacketHandler;
import com.cafiaso.server.network.protocol.handlers.login.LoginAcknowledgedPacketHandler;
import com.cafiaso.server.network.protocol.handlers.login.LoginStartPacketHandler;
import com.cafiaso.server.network.protocol.handlers.status.PingRequestPacketHandler;
import com.cafiaso.server.network.protocol.handlers.status.StatusRequestPacketHandler;
import com.cafiaso.server.network.protocol.packets.client.PacketDeserializer;
import com.cafiaso.server.network.protocol.packets.client.configuration.ServerboundKnownPacks;
import com.cafiaso.server.network.protocol.packets.client.handshake.HandshakePacket;
import com.cafiaso.server.network.protocol.packets.client.handshake.LegacyServerListPingPacket;
import com.cafiaso.server.network.protocol.packets.client.login.EncryptionResponsePacket;
import com.cafiaso.server.network.protocol.packets.client.login.LoginAcknowledgedPacket;
import com.cafiaso.server.network.protocol.packets.client.login.LoginStartPacket;
import com.cafiaso.server.network.protocol.packets.client.status.PingRequestPacket;
import com.cafiaso.server.network.protocol.packets.client.status.StatusRequestPacket;
import com.cafiaso.server.utils.Pair;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A connection state.
 * <p>
 * Each connection state has a set of packets that can be sent by the client at that state.
 * <p>
 * Each packet is registered with its {@link PacketDeserializer} and its {@link PacketHandler}.
 */
public enum ConnectionState {

    /**
     * Initial handshake between the client and the server.
     *
     * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Handshaking">Handshaking</a>
     */
    HANDSHAKE(
            entry(new HandshakePacket.Deserializer(), HandshakePacketHandler.class),
            entry(new LegacyServerListPingPacket.Deserializer(), LegacyServerListPingPacketHandler.class)
    ),
    /**
     * Initial status request and ping.
     *
     * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Status">Status</a>
     */
    STATUS(
            entry(new StatusRequestPacket.Deserializer(), StatusRequestPacketHandler.class),
            entry(new PingRequestPacket.Deserializer(), PingRequestPacketHandler.class)
    ),
    /**
     * Login process.
     *
     * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Login">Login</a>
     */
    LOGIN(
            entry(new LoginStartPacket.Deserializer(), LoginStartPacketHandler.class),
            entry(new EncryptionResponsePacket.Deserializer(), EncryptionResponsePacketHandler.class),
            entry(new LoginAcknowledgedPacket.Deserializer(), LoginAcknowledgedPacketHandler.class)
    ),
    /**
     * Configuration process.
     *
     * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Configuration">Configuration</a>
     */
    CONFIGURATION(
            entry(new ServerboundKnownPacks.Deserializer(), ServerboundKnownPacksHandler.class)
    ),
    /**
     * Main game interaction.
     *
     * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Play">Play</a>
     */
    PLAY;

    private final Map<Integer, PacketRegistration<?>> packets;

    @SafeVarargs
    ConnectionState(Pair<Integer, PacketRegistration<?>>... packets) {
        this.packets = Arrays.stream(packets)
                .collect(Collectors.toMap(Pair::key, Pair::value));
    }

    /**
     * Maps a packet ID to its {@link PacketRegistration}.
     *
     * @param reader       the packet deserializer
     * @param handlerClass the packet handler class
     * @param <P>          the packet type
     * @return a pair of the packet ID and its {@link PacketRegistration}
     */
    private static <P> Pair<Integer, PacketRegistration<?>> entry(PacketDeserializer<P> reader, Class<? extends PacketHandler<P>> handlerClass) {
        return new Pair<>(reader.getId(), new PacketRegistration<>(reader, handlerClass));
    }

    /**
     * Gets the {@link PacketRegistration} of the specified packet ID, if it exists.
     *
     * @param id  the packet ID
     * @param <P> the packet type
     * @return an optional containing the {@link PacketRegistration} if it exists, or empty otherwise
     */
    @SuppressWarnings("unchecked")
    public <P> Optional<PacketRegistration<P>> getPacket(int id) {
        return Optional.ofNullable((PacketRegistration<P>) packets.get(id));
    }

    /**
     * Convenience class that holds the packet deserializer and its handler class.
     *
     * @param deserializer the packet deserializer
     * @param handler      the packet handler class
     * @param <P>          the packet type
     */
    public record PacketRegistration<P>(
            PacketDeserializer<P> deserializer,
            Class<? extends PacketHandler<P>> handler
    ) {
    }
}
