package ink.reactor.protocol.netty;

import ink.reactor.kernel.logger.Logger;
import ink.reactor.protocol.netty.player.NettyPlayerConnection;
import ink.reactor.protocol.netty.player.PlayerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollIoHandler;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueIoHandler;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.uring.IoUring;
import io.netty.channel.uring.IoUringIoHandler;
import io.netty.channel.uring.IoUringServerSocketChannel;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public final class ServerConnection {
    private static final WriteBufferWaterMark SERVER_WRITE_MARK = new WriteBufferWaterMark(1 << 20, 1 << 21);

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ChannelFuture future;

    private final Logger logger;

    private final List<NettyPlayerConnection> playerConnections = Collections.synchronizedList(new ArrayList<>());

    public void connect(final NettyConfig config) throws InterruptedException {
        final int workerThreadCount = config.workerThreadCount();
        final int bossThreadCount = config.bossThreadCount();

        final IoHandlerFactory IoFactory;

        Class<? extends ServerSocketChannel> socketChannel;
        if (IoUring.isAvailable()) {
            IoFactory = IoUringIoHandler.newFactory();
            socketChannel = IoUringServerSocketChannel.class;
        }else if (Epoll.isAvailable()) {
            IoFactory = EpollIoHandler.newFactory();
            socketChannel = EpollServerSocketChannel.class;
        } else if (KQueue.isAvailable()) {
            IoFactory = KQueueIoHandler.newFactory();
            socketChannel = KQueueServerSocketChannel.class;
        } else {
            IoFactory = NioIoHandler.newFactory();
            socketChannel = NioServerSocketChannel.class;
        }

        bossGroup = new MultiThreadIoEventLoopGroup(bossThreadCount, IoFactory);
        workerGroup = new MultiThreadIoEventLoopGroup(workerThreadCount, IoFactory);

        future = new ServerBootstrap().channel(socketChannel)
            .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, SERVER_WRITE_MARK)
            .childHandler(new PlayerChannelInitializer(playerConnections, config.tcpFastOpen(), config.tcpFastOpenConnections()))
            .group(bossGroup, workerGroup)
            .localAddress(config.ip(), config.port())
            .bind()
            .sync();
    }

    public void shutdown() {
        try {
            future.channel().close().sync();
        } catch(InterruptedException e) {
            logger.error("Error on shutdown server connection", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
