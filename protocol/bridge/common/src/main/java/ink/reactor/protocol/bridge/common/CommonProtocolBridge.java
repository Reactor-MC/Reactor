package ink.reactor.protocol.bridge.common;

import ink.reactor.protocol.api.ConnectionState;
import ink.reactor.protocol.api.PlayerConnection;
import ink.reactor.protocol.api.ProtocolBridge;
import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;
import ink.reactor.protocol.api.packet.PacketHandlerStorage;
import ink.reactor.protocol.bridge.common.handler.state.StatePacketHandlerStorage;

final class CommonProtocolBridge implements ProtocolBridge {

    private final PacketHandlerStorage handshake = new StatePacketHandlerStorage(3);

    @Override
    public PacketHandlerStorage getHandlers(final ConnectionState state) {
        return handshake;
    }

    @Override
    public void execute(final PlayerConnection connection, final ReaderBuffer buffer, final int id) {
        handshake.callHandlers(connection, buffer, id);
    }
}
