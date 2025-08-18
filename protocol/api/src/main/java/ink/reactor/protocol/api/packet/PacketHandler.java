package ink.reactor.protocol.api.packet;

import ink.reactor.protocol.api.connection.PlayerConnection;
import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;

public interface PacketHandler {
    void handle(final PlayerConnection connection, final ReaderBuffer buffer);
    int packetId();
}
