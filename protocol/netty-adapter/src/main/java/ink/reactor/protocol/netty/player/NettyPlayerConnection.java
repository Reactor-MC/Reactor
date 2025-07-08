package ink.reactor.protocol.netty.player;

import ink.reactor.protocol.api.PlayerConnection;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.SocketChannel;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor
public final class NettyPlayerConnection implements PlayerConnection {

    private final SocketChannel channel;
    private final EventLoop eventLoop;

    private volatile boolean online = true;

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
}
