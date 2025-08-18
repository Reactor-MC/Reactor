package ink.reactor.bridge.protocol.v1_21.login.handler;

import ink.reactor.bridge.protocol.v1_21.InboundPackets;
import ink.reactor.protocol.api.connection.process.ConnectionsInProcess;
import ink.reactor.protocol.api.connection.PlayerConnection;
import ink.reactor.protocol.api.Protocol;
import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;
import ink.reactor.protocol.api.packet.PacketHandler;
import ink.reactor.protocol.api.connection.process.LoginProcess;
import ink.reactor.protocol.api.packet.login.LoginOutPackets;
import ink.reactor.protocol.api.player.GameProfile;

public final class LoginStartPacketHandler implements PacketHandler {

    @Override
    public void handle(final PlayerConnection connection, final ReaderBuffer buffer) {
        final String userName = buffer.readString();
        final ConnectionsInProcess connectionsInProcess = Protocol.get().getConnectionsInProcess();
        if (connectionsInProcess.isInProcess(userName)) {
            connection.disconnect("You connection is already been processed");
            return;
        }

        final LoginProcess loginProcess = new LoginProcess(connection, userName);
        loginProcess.setUuid(buffer.readUUID());
        connectionsInProcess.getEventBus().post(loginProcess);

        final LoginOutPackets outboundLoginPackets = connection.getBridge().getOutboundLoginPackets();
        if (loginProcess.isCancelled()) {
            connection.sendPacket(outboundLoginPackets.createDisconnect(String.valueOf(loginProcess.getKickMessage())));
            return;
        }

        connectionsInProcess.addConnection(connection, loginProcess);

        if (loginProcess.getNextStage() == LoginProcess.Stage.SUCCESS) {
            connection.sendPacket(outboundLoginPackets.createSuccess(new GameProfile(loginProcess.getName(), loginProcess.getUuid(), loginProcess.getProperties())));
        }
    }

    @Override
    public int packetId() {
        return InboundPackets.LOGIN_HELLO;
    }
}
