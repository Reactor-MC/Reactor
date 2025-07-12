package ink.reactor.protocol.api;

import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;
import ink.reactor.protocol.api.packet.PacketHandlerStorage;

public interface ProtocolBridge {
    PacketHandlerStorage getHandlers(final ConnectionState state);
    void execute(final PlayerConnection connection, final ReaderBuffer buffer, final int id);
}
