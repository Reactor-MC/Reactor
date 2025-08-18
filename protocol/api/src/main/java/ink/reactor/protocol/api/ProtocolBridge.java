package ink.reactor.protocol.api;

import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;
import ink.reactor.protocol.api.connection.ConnectionState;
import ink.reactor.protocol.api.connection.PlayerConnection;
import ink.reactor.protocol.api.packet.PacketHandlerStorage;
import ink.reactor.protocol.api.packet.login.LoginOutPackets;

public interface ProtocolBridge {
    PacketHandlerStorage getHandlers(final ConnectionState state);
    void execute(final PlayerConnection connection, final ReaderBuffer buffer, final int id);

    LoginOutPackets getOutboundLoginPackets();
}
