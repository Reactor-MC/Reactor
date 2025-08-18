package ink.reactor.protocol.bridge.common.handler.handshake;

import ink.reactor.protocol.api.buffer.writer.SerializedBuffer;
import ink.reactor.protocol.api.connection.ConnectionState;
import ink.reactor.protocol.api.connection.PlayerConnection;
import ink.reactor.protocol.api.Protocol;
import ink.reactor.protocol.api.ProtocolBridge;
import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;
import ink.reactor.protocol.api.connection.process.HandshakeProcess;
import ink.reactor.protocol.api.packet.CachedPacket;
import ink.reactor.protocol.api.packet.PacketHandler;
import ink.reactor.protocol.api.packet.PacketOutbound;

public final class HandshakePacketHandler implements PacketHandler {

    private static final int STATUS = 1, LOGIN = 2;

    private static final PacketOutbound DEFAULT_STATUS_RESPONSE = new CachedPacket(0, SerializedBuffer.from(
        "{\"description\":{\"text\":\"                      §eReactor §7[1.21.5]§r\\n       §3§lFASTER §7| §3§lMODULAR §7| §3§lSERVER IN JAVA\"},\"players\":{\"max\":2025,\"online\":0},\"version\":{\"name\":\"1.21.5\",\"protocol\":770}}"));

    @Override
    public void handle(final PlayerConnection connection, final ReaderBuffer buffer) {
        if (buffer.isEmpty()) {
            return;
        }

        final int protocolVersion = buffer.readVarInt();
        final String address = buffer.readString();
        final int port = buffer.readChar();
        final int intent = buffer.readVarInt();

        final HandshakeProcess handshakeProcess = new HandshakeProcess(protocolVersion, address, port, intent, DEFAULT_STATUS_RESPONSE, false);
        Protocol.get().getConnectionsInProcess().getEventBus().post(handshakeProcess);

        if (handshakeProcess.isCancelled()) {
            return;
        }

        if (intent == STATUS) {
            connection.sendPacket(handshakeProcess.getStatusResponse());
            return;
        }

        if (intent == LOGIN) {
            connection.changeState(ConnectionState.LOGIN);
        }

        final ProtocolBridge bridge = Protocol.get().getBridge(protocolVersion);
        if (bridge == null) {
            connection.disconnect();
            return;
        }

        connection.setBridge(bridge);
    }

    @Override
    public int packetId() {
        return 0;
    }
}
