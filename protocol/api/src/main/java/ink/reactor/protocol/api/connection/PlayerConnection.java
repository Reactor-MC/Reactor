package ink.reactor.protocol.api.connection;

import ink.reactor.protocol.api.ProtocolBridge;
import ink.reactor.protocol.api.packet.PacketOutbound;

import java.util.Collection;

public interface PlayerConnection {
    void addPacketToQueue(final PacketOutbound packet);
    void addPacketToQueue(final Collection<PacketOutbound> packets);

    void sendPacket(final PacketOutbound packet);
    void sendPackets(final Collection<PacketOutbound> packets);

    ProtocolBridge getBridge();
    ConnectionState getState();

    void setBridge(final ProtocolBridge bridge);
    void changeState(final ConnectionState state);

    void disconnect();
    void disconnect(final String reason);
}
