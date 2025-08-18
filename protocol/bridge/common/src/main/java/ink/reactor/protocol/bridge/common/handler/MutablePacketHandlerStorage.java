package ink.reactor.protocol.bridge.common.handler;

import ink.reactor.protocol.api.connection.PlayerConnection;
import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;
import ink.reactor.protocol.api.packet.PacketHandler;

public interface MutablePacketHandlerStorage {
    void callHandlers(final PlayerConnection connection, final ReaderBuffer readerBuffer);

    MutablePacketHandlerStorage add(final PacketHandler handler);
    MutablePacketHandlerStorage remove(final PacketHandler handler);

    MutablePacketHandlerStorage add(final PacketHandler... handlers);
    MutablePacketHandlerStorage remove(final PacketHandler... handlers);
}
