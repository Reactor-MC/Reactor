package ink.reactor.protocol.netty.player;

import ink.reactor.protocol.netty.inbound.decoder.NoCompressionDecoder;
import ink.reactor.protocol.netty.outbound.encoder.NoCompressionEncoder;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PlayerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final boolean tcpFastOpen;
    private final int tcpFastOpenConnections;

    @Override
    protected void initChannel(final SocketChannel socketChannel) {
        final NettyPlayerConnection nettyPlayerConnection = new NettyPlayerConnection(socketChannel, socketChannel.eventLoop());

        final ChannelConfig config = socketChannel.config();
        config.setOption(ChannelOption.TCP_NODELAY, true);

        if (tcpFastOpen) {
            config.setOption(ChannelOption.TCP_FASTOPEN, tcpFastOpenConnections);
            config.setOption(ChannelOption.TCP_FASTOPEN_CONNECT,true);
        }

        config.setOption(ChannelOption.IP_TOS, 0x18);
        config.setAllocator(ByteBufAllocator.DEFAULT);

        socketChannel.pipeline()
            .addLast("timeout", new ReadTimeoutHandler(20))
            .addLast("decoder", new NoCompressionDecoder())
//            .addLast("manager", new NetworkManager(connection))
            .addLast("encoder", new NoCompressionEncoder());
    }
}
