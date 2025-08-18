package ink.reactor.protocol.netty.player;

import ink.reactor.protocol.api.connection.ConnectionState;
import ink.reactor.protocol.api.connection.PlayerConnection;
import ink.reactor.protocol.api.Protocol;
import ink.reactor.protocol.api.ProtocolBridge;
import ink.reactor.protocol.api.packet.PacketOutbound;
import ink.reactor.protocol.netty.inbound.InboundPacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor
public final class NettyPlayerConnection extends SimpleChannelInboundHandler<InboundPacket> implements PlayerConnection {

    private final SocketChannel channel;
    private final EventLoop eventLoop;

    private volatile ConnectionState connectionState = ConnectionState.HANDSHAKE;

    private ProtocolBridge bridge = Protocol.get().getCommonBridge();

    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext, final InboundPacket inboundPacket) {
        try {
            bridge.execute(this, inboundPacket.data(), inboundPacket.id());
        } catch (Exception e) {
            disconnect(e.getMessage());
        }
    }

    @Override
    public void addPacketToQueue(final Collection<PacketOutbound> packets) {
        // TODO: Implement packet queue
    }

    @Override
    public void addPacketToQueue(final PacketOutbound packet) {
        // TODO: Implement packet queue
    }

    @Override
    public void sendPacket(final PacketOutbound packet) {
        if (packet == null) {
            return;
        }

        if (eventLoop.inEventLoop()) {
            channel.writeAndFlush(packet);
            return;
        }
        eventLoop.execute(() -> channel.writeAndFlush(packet));
    }

    @Override
    public void sendPackets(final Collection<PacketOutbound> packets) {
        if (packets == null || packets.isEmpty()) {
            return;
        }

        if (eventLoop.inEventLoop()) {
            for (final Object packet : packets) {
                channel.write(packet);
            }
            channel.flush();
            return;
        }

        eventLoop.execute(() -> {
            for (final Object packet : packets) {
                channel.write(packet);
            }
            channel.flush();
        });
    }

    @Override
    public ProtocolBridge getBridge() {
        return bridge;
    }

    @Override
    public void setBridge(final ProtocolBridge bridge) {
        this.bridge = bridge;
    }

    @Override
    public ConnectionState getState() {
        return connectionState;
    }

    @Override
    public void changeState(final ConnectionState state) {
        this.connectionState = state;
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) {
        Protocol.get().getConnectionsInProcess().removeConnection(this);
    }

    @Override
    public void disconnect() {
        Protocol.get().getConnectionsInProcess().removeConnection(this);
        channel.close();
    }

    @Override
    public void disconnect(final String reason) {
        try {
            if (connectionState == ConnectionState.LOGIN) {
                sendPacket(bridge.getOutboundLoginPackets().createDisconnect(reason));
            }
        } finally {
            disconnect();
        }
    }
}
