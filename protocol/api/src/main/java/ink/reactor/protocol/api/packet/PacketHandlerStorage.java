package ink.reactor.protocol.api.packet;

import ink.reactor.protocol.api.connection.PlayerConnection;
import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;

public interface PacketHandlerStorage {
    void callHandlers(final PlayerConnection connection, final ReaderBuffer buffer, final int packetID);

    void add(final PacketHandler handler);
    void remove(final PacketHandler handler);

    void add(final PacketHandler... handlers);
    void remove(final PacketHandler... handlers);

    void clear(final int id);
}
