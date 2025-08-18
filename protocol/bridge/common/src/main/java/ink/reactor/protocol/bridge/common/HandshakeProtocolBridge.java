package ink.reactor.protocol.bridge.common;

import ink.reactor.protocol.api.connection.ConnectionState;
import ink.reactor.protocol.api.connection.PlayerConnection;
import ink.reactor.protocol.api.ProtocolBridge;
import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;
import ink.reactor.protocol.api.packet.PacketHandlerStorage;
import ink.reactor.protocol.api.packet.login.LoginOutPackets;
import ink.reactor.protocol.bridge.common.handler.handshake.HandshakePacketHandler;
import ink.reactor.protocol.bridge.common.handler.handshake.HandshakeStatusPacketHandler;
import ink.reactor.protocol.bridge.common.handler.state.StatePacketHandlerStorage;

final class HandshakeProtocolBridge implements ProtocolBridge {

    private final PacketHandlerStorage handshake = new StatePacketHandlerStorage(2);

    public HandshakeProtocolBridge() {
        handshake.add(new HandshakePacketHandler(), new HandshakeStatusPacketHandler());
    }

    @Override
    public PacketHandlerStorage getHandlers(final ConnectionState state) {
        return handshake;
    }

    @Override
    public void execute(final PlayerConnection connection, final ReaderBuffer buffer, final int id) {
        handshake.callHandlers(connection, buffer, id);
    }

    @Override
    public LoginOutPackets getOutboundLoginPackets() {
        return null;
    }
}
