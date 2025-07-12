package ink.reactor.protocol.netty.player;

import ink.reactor.kernel.Reactor;
import ink.reactor.protocol.api.ConnectionState;
import ink.reactor.protocol.api.PlayerConnection;
import ink.reactor.protocol.api.ProtocolBridge;
import ink.reactor.protocol.netty.inbound.InboundPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor
public final class NettyPlayerConnection extends SimpleChannelInboundHandler<InboundPacket> implements PlayerConnection {

    private final Collection<NettyPlayerConnection> playerConnections;

    private final SocketChannel channel;
    private final EventLoop eventLoop;

    private volatile ConnectionState connectionState;

    private ProtocolBridge bridge;

    @Override
    public void sendPacket(final Object packet) {

    }

    @Override
    public void sendPackets(final Collection<Object> packets) {

    }

    @Override
    public void sendNowPacket(final Object packet) {

    }

    @Override
    public void sendNowPackets(final Collection<Object> packets) {

    }

    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext, final InboundPacket inboundPacket) {

    }

    @Override
    public ProtocolBridge getProtocol() {
        return null;
    }

    @Override
    public ConnectionState getState() {
        return connectionState;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        playerConnections.add(this);
        Reactor.getServer().logger().info("Player connected");
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) {
        playerConnections.remove(this);
        Reactor.getServer().logger().info("Player disconnected");
    }
}
