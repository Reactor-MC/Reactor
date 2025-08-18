package ink.reactor.bridge.protocol.v1_21;

import ink.reactor.bridge.protocol.v1_21.login.handler.LoginAcknowledgedPacketHandler;
import ink.reactor.bridge.protocol.v1_21.login.handler.LoginStartPacketHandler;
import ink.reactor.bridge.protocol.v1_21.login.packets.LoginOutPacketsV1_21;
import ink.reactor.protocol.api.connection.ConnectionState;
import ink.reactor.protocol.api.connection.PlayerConnection;
import ink.reactor.protocol.api.Protocol;
import ink.reactor.protocol.api.ProtocolBridge;
import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;
import ink.reactor.protocol.api.packet.PacketHandlerStorage;
import ink.reactor.protocol.api.packet.login.LoginOutPackets;
import ink.reactor.protocol.bridge.common.handler.state.StatePacketHandlerStorage;
import lombok.Getter;

@Getter
public final class BridgeV1_21 implements ProtocolBridge {

    private final LoginOutPackets outboundLoginPackets = new LoginOutPacketsV1_21();

    private final PacketHandlerStorage
        handshake = new StatePacketHandlerStorage(InboundPackets.MAX_HANDSHAKE_ID),
        login = new StatePacketHandlerStorage(InboundPackets.MAX_LOGIN_ID),
        configuration = new StatePacketHandlerStorage(InboundPackets.MAX_CONFIGURATION_ID),
        play = new StatePacketHandlerStorage(InboundPackets.MAX_PLAY_ID);

    private BridgeV1_21(){
        login.add(
            new LoginStartPacketHandler(),
            new LoginAcknowledgedPacketHandler()
        );
    }

    public static void register() {
        Protocol.get().registerBridge(new BridgeV1_21(), 770);
    }

    @Override
    public PacketHandlerStorage getHandlers(final ConnectionState state) {
        return switch (state) {
            case HANDSHAKE -> handshake;
            case LOGIN -> login;
            case CONFIGURATION -> configuration;
            case PLAY -> play;
        };
    }

    @Override
    public void execute(final PlayerConnection connection, final ReaderBuffer buffer, final int id) {
        getHandlers(connection.getState()).callHandlers(connection, buffer, id);
    }
}
