package ink.reactor.network.internal.quic

import ink.reactor.kernel.Reactor
import ink.reactor.network.internal.player.PlayerConnectionInitializer
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.incubator.codec.quic.InsecureQuicTokenHandler
import io.netty.incubator.codec.quic.QuicCongestionControlAlgorithm
import io.netty.incubator.codec.quic.QuicServerCodecBuilder
import io.netty.incubator.codec.quic.QuicSslContext
import java.util.concurrent.TimeUnit

internal class QuicDatagramHandler(val sslContext: QuicSslContext): ChannelInboundHandlerAdapter() {

    override fun channelActive(ctx: ChannelHandlerContext) {
        val quicHandler = QuicServerCodecBuilder()
            .sslContext(sslContext)
            .tokenHandler(InsecureQuicTokenHandler.INSTANCE)
            .maxIdleTimeout(3000, TimeUnit.MILLISECONDS)
            .ackDelayExponent(3)
            .initialMaxData(524288L)
            .initialMaxStreamDataUnidirectional(0L)
            .initialMaxStreamsUnidirectional(0L)
            .initialMaxStreamDataBidirectionalLocal(131072L)
            .initialMaxStreamDataBidirectionalRemote(131072L)
            .initialMaxStreamsBidirectional(1L)
            .congestionControlAlgorithm(QuicCongestionControlAlgorithm.BBR)
            .handler(ChannelInboundHandlerAdapter())
            .streamHandler(PlayerConnectionInitializer())
            .build()

        ctx.channel().pipeline().addLast(quicHandler);

    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        Reactor.logger.error("could not initialize certificate", cause)
        ctx.close()
    }
}
