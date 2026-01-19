package ink.reactor.network.internal.quic

import ink.reactor.kernel.Reactor
import ink.reactor.network.internal.quic.certificate.RuntimeCertificateProvider
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFactory
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.ReflectiveChannelFactory
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollDatagramChannel
import io.netty.channel.kqueue.KQueue
import io.netty.channel.kqueue.KQueueDatagramChannel
import io.netty.channel.socket.DatagramChannel
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.handler.ssl.ClientAuth
import io.netty.incubator.codec.quic.QuicSslContext
import io.netty.incubator.codec.quic.QuicSslContextBuilder
import io.netty.util.AttributeKey
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetSocketAddress
import java.nio.file.Path
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.TrustManagerFactory

class QuicTransport(
    val workerGroup: EventLoopGroup,
) {
    val CLIENT_CERTIFICATE_ATTR: AttributeKey<X509Certificate> =
        AttributeKey.valueOf<X509Certificate>("CLIENT_CERTIFICATE")

    private var bootstrapIpv4: Bootstrap
    private var bootstrapIpv6: Bootstrap

    init {
        val certPath = Path.of("certs/server.p12")
        val ks = RuntimeCertificateProvider.loadOrCreate(certPath)

        val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        kmf.init(ks, RuntimeCertificateProvider.PASSWORD)

        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(ks)

        val sslContext: QuicSslContext =
            QuicSslContextBuilder.forServer(kmf, null)
                .applicationProtocols("hytale/1")
                .earlyData(false)
                .clientAuth(ClientAuth.REQUIRE)
                .trustManager(tmf)
                .build()

        val factoryIpv4: ChannelFactory<out DatagramChannel> = getDatagramChannelFactory()
        val factoryIpv6: ChannelFactory<out DatagramChannel> = getDatagramChannelFactory()

        val quicHandler = QuicDatagramHandler(sslContext)

        Reactor.logger.info("Using IPv4 Datagram Channel: $factoryIpv4")
        bootstrapIpv4 = Bootstrap()
            .group(workerGroup)
            .channelFactory(factoryIpv4)
            .option(ChannelOption.SO_REUSEADDR, true)
            .handler(quicHandler)
            .validate()

        Reactor.logger.info("Using IPv6 Datagram Channel: $factoryIpv6")
        bootstrapIpv6 = Bootstrap()
            .group(workerGroup)
            .channelFactory(factoryIpv6)
            .option(ChannelOption.SO_REUSEADDR, true)
            .handler(quicHandler)
            .validate()

        bootstrapIpv4.register().sync()
        bootstrapIpv6.register().sync()
    }

    fun bind(address: InetSocketAddress): ChannelFuture {
        if (address.address is Inet4Address) {
            return this.bootstrapIpv4.bind(address).sync();
        }
        if (address.address is Inet6Address) {
            return this.bootstrapIpv6.bind(address).sync();
        }
        throw UnsupportedOperationException("Unsupported address type: ${address.address.javaClass.name}");
    }

    fun shutdown() {
        Reactor.logger.info("Shutting down")
        try {
            this.workerGroup.shutdownGracefully(0L, 1L, TimeUnit.SECONDS)
                .await(1L, TimeUnit.SECONDS);
        } catch (e: InterruptedException) {
            Reactor.logger.error("Failed to shutdown gracefully.", e)
        }
    }

    fun getDatagramChannelFactory(): ReflectiveChannelFactory<out DatagramChannel> {
        if (Epoll.isAvailable()) {
            return ReflectiveChannelFactory<EpollDatagramChannel>(EpollDatagramChannel::class.java)
        }
        if (KQueue.isAvailable()) {
            return ReflectiveChannelFactory<KQueueDatagramChannel>(KQueueDatagramChannel::class.java)
        }
        return ReflectiveChannelFactory<NioDatagramChannel>(NioDatagramChannel::class.java)
    }
}
