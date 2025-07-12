package ink.reactor.bridge.protocol.v1_21;

import ink.reactor.protocol.api.ConnectionState;
import ink.reactor.protocol.api.PlayerConnection;
import ink.reactor.protocol.api.Protocol;
import ink.reactor.protocol.api.ProtocolBridge;
import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;
import ink.reactor.protocol.api.packet.PacketHandlerStorage;
import ink.reactor.protocol.bridge.common.handler.state.StatePacketHandlerStorage;

public final class BridgeV1_21 implements ProtocolBridge {

    private BridgeV1_21(){}

    public static void register() {
        Protocol.getInstance().registerBridge(772, new BridgeV1_21());
    }

    private final PacketHandlerStorage
        handshake = new StatePacketHandlerStorage(OutboundPackets.MAX_HANDSHAKE_ID),
        login = new StatePacketHandlerStorage(OutboundPackets.MAX_LOGIN_ID),
        configuration = new StatePacketHandlerStorage(OutboundPackets.MAX_CONFIGURATION_ID),
        play = new StatePacketHandlerStorage(OutboundPackets.MAX_PLAY_ID);

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
