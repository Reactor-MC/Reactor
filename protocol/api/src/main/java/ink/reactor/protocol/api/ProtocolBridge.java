package ink.reactor.protocol.api;

import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;
import ink.reactor.protocol.api.packet.PacketHandler;

public interface ProtocolBridge {
    void addPacketHandler(final ConnectionState connectionState, final PacketHandler handler);
    void addPacketHandlers(final ConnectionState connectionState, final PacketHandler... handlers);

    void removePacketHandler(final ConnectionState connectionState, final PacketHandler handler);

    void execute(final ReaderBuffer buffer, final int id);
}
