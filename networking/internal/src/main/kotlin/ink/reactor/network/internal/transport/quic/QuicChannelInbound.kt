package ink.reactor.network.internal.transport.quic

import ink.reactor.kernel.Reactor
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.incubator.codec.quic.QuicChannel
import java.security.cert.Certificate
import java.security.cert.X509Certificate

class QuicChannelInbound: ChannelInboundHandlerAdapter() {

    override fun isSharable(): Boolean {
        return true
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        if (ctx.channel() !is QuicChannel) {
            return
        }
        Reactor.logger.info("Received connection from ")
    }

    private fun extractClientCertificate(channel: QuicChannel): X509Certificate? {
        val sslEngine = channel.sslEngine() ?: return null
        val peerCerts: Array<Certificate?> = sslEngine.session.peerCertificates
        if (peerCerts.isNotEmpty() && peerCerts[0] is X509Certificate) {
            return peerCerts[0] as X509Certificate?
        }
        return null
    }

}
