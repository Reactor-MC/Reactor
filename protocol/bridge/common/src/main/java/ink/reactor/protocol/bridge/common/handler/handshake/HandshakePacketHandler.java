package ink.reactor.protocol.bridge.common.handler.handshake;

import ink.reactor.protocol.api.PlayerConnection;
import ink.reactor.protocol.api.Protocol;
import ink.reactor.protocol.api.ProtocolBridge;
import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;
import ink.reactor.protocol.api.packet.PacketHandler;

public final class HandshakePacketHandler implements PacketHandler {

    @Override
    public void handle(final PlayerConnection connection, final ReaderBuffer readerBuffer) {
        final int protocolVersion = readerBuffer.readVarInt();
        final String address = readerBuffer.readString();
        final int port = readerBuffer.readChar();
        final int intent = readerBuffer.readVarInt();

        final ProtocolBridge bridge = Protocol.getInstance().getBridge(protocolVersion);
        if (bridge == null) {
            connection.close();
            return;
        }

        connection.setBridge(bridge);
    }

    @Override
    public int packetId() {
        return 0;
    }
}
