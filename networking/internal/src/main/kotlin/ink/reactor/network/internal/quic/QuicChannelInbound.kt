package ink.reactor.network.internal.quic

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

    @Override
    public void channelActive(@Nonnull ChannelHandlerContext ctx) throws Exception {
        QuicChannel channel = (QuicChannel)ctx.channel();
        LOGGER.at(Level.INFO).log("Received connection from %s to %s", (Object)NettyUtil.formatRemoteAddress(channel), (Object)NettyUtil.formatLocalAddress(channel));
        X509Certificate clientCert = this.extractClientCertificate(channel);
        if (clientCert == null) {
            LOGGER.at(Level.WARNING).log("Connection rejected: no client certificate from %s", NettyUtil.formatRemoteAddress(channel));
            ProtocolUtil.closeConnection(channel);
            return;
        }
        channel.attr(CLIENT_CERTIFICATE_ATTR).set(clientCert);
        LOGGER.at(Level.FINE).log("Client certificate: %s", clientCert.getSubjectX500Principal().getName());
    }

    @Override
    public void channelInactive(@Nonnull ChannelHandlerContext ctx) {
        ((QuicChannel)ctx.channel()).collectStats().addListener(f -> {
            if (f.isSuccess()) {
                LOGGER.at(Level.INFO).log("Connection closed: %s", f.getNow());
            }
        });
    }

    @Override
    public void exceptionCaught(@Nonnull ChannelHandlerContext ctx, Throwable cause) {
        ((HytaleLogger.Api)LOGGER.at(Level.WARNING).withCause(cause)).log("Got exception from netty pipeline in ChannelInitializer!");
        Channel channel = ctx.channel();
        if (channel.isWritable()) {
            channel.writeAndFlush(new Disconnect("Internal server error!", DisconnectType.Crash)).addListener((GenericFutureListener)ProtocolUtil.CLOSE_ON_COMPLETE);
        } else {
            ProtocolUtil.closeApplicationConnection(channel);
        }
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
