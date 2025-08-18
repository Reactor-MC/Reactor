package ink.reactor.bridge.protocol.v1_21.login.handler;

import ink.reactor.bridge.protocol.v1_21.InboundPackets;
import ink.reactor.protocol.api.Protocol;
import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;
import ink.reactor.protocol.api.connection.ConnectionState;
import ink.reactor.protocol.api.connection.PlayerConnection;
import ink.reactor.protocol.api.connection.process.LoginProcess;
import ink.reactor.protocol.api.packet.PacketHandler;

public final class LoginAcknowledgedPacketHandler implements PacketHandler {

    @Override
    public void handle(final PlayerConnection connection, final ReaderBuffer buffer) {
        final LoginProcess process = (LoginProcess)Protocol.get().getConnectionsInProcess().removeConnection(connection);
        connection.changeState(ConnectionState.CONFIGURATION);


    }

    @Override
    public int packetId() {
        return InboundPackets.LOGIN_ACKNOWLEDGED;
    }
}
