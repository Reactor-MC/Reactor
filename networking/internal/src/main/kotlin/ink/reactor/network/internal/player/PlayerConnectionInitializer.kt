package ink.reactor.network.internal.player

import ink.reactor.kernel.Reactor
import ink.reactor.network.internal.NetworkInternalConnector
import ink.reactor.network.internal.io.PacketDecoder
import ink.reactor.network.internal.io.PacketEncoder
import ink.reactor.network.internal.packet.PacketInboundHandler
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.incubator.codec.quic.QuicStreamChannel
import java.security.cert.X509Certificate;

class PlayerConnectionInitializer: ChannelInitializer<Channel>() {

    override fun initChannel(ch: Channel) {
//        if (ch is QuicStreamChannel) {
//            Reactor.logger.info("Received stream %s to %s",
//                NettyUtil.formatRemoteAddress(ch) as Any?,
//                NettyUtil.formatLocalAddress(ch) as Any?
//            )
//            val parentChannel = ch.parent()
//            val clientCert: X509Certificate? = parentChannel.attr<Any?>(QUICTransport.CLIENT_CERTIFICATE_ATTR).get()
//            if (clientCert != null) {
//                channel.attr(QUICTransport.CLIENT_CERTIFICATE_ATTR).set(clientCert)
//                HytaleLogger.getLogger().at(Level.FINE)
//                    .log("Copied client certificate to stream: %s", clientCert.getSubjectX500Principal().getName())
//            }
//        } else {
//            HytaleLogger.getLogger().at(Level.INFO).log(
//                "Received connection from %s to %s",
//                NettyUtil.formatRemoteAddress(channel) as Any?,
//                NettyUtil.formatLocalAddress(channel) as Any?
//            )
//        }
        if (ch is QuicStreamChannel) {
            HytaleLogger.getLogger().at(Level.INFO).log(
                "Received stream %s to %s",
                NettyUtil.formatRemoteAddress(channel) as Any?,
                NettyUtil.formatLocalAddress(channel) as Any?
            )
            val parentChannel = ch.parent()
            val clientCert: X509Certificate? = parentChannel.attr<Any?>(QUICTransport.CLIENT_CERTIFICATE_ATTR).get()
            if (clientCert != null) {
                channel.attr(QUICTransport.CLIENT_CERTIFICATE_ATTR).set(clientCert)
                HytaleLogger.getLogger().at(Level.FINE)
                    .log("Copied client certificate to stream: %s", clientCert.getSubjectX500Principal().getName())
            }
        } else {
            HytaleLogger.getLogger().at(Level.INFO).log(
                "Received connection from %s to %s",
                NettyUtil.formatRemoteAddress(channel) as Any?,
                NettyUtil.formatLocalAddress(channel) as Any?
            )
        }

        val connection = NettyPlayerConnection(ch)
        val logger = Reactor.loggerFactory.createLogger(connection.getIp())

        val pipeline = ch.pipeline()
        pipeline.addLast("readTimeOut", ReadTimeoutHandler(NetworkInternalConnector.config.readTimeoutSeconds))
        pipeline.addLast("decoder", PacketDecoder(logger))
        pipeline.addLast("encoder", PacketEncoder())
        pipeline.addLast("packet_handler", PacketInboundHandler(connection))
    }
}
