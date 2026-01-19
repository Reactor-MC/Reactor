package ink.reactor.network.internal.player

import ink.reactor.kernel.Reactor
import ink.reactor.network.api.packet.CachedPacket
import ink.reactor.network.api.packet.Packet
import ink.reactor.network.api.player.PlayerConnection
import io.netty.channel.Channel

class NettyPlayerConnection(
    private val ctx: Channel
) : PlayerConnection {

    override fun sendPacket(packet: Packet) {
        if (!isOnline()) return

        ctx.writeAndFlush(packet)
    }

    override fun sendPackets(vararg packets: Packet) {
        if (!isOnline()) return

        for (packet in packets) {
            ctx.write(packet)
        }
        ctx.flush()
    }

    override fun sendCachedPacket(packet: CachedPacket) {
        if (!isOnline()) {
            packet.onWriteComplete()
            return
        }

        val buf = packet.retainForWrite() ?: return

        ctx.writeAndFlush(buf).addListener { future ->
            packet.onWriteComplete()
            if (!future.isSuccess) {
                Reactor.logger.debug("Failed to send cached packet: ${future.cause()?.message}")
            }
        }
    }

    override fun disconnect(reason: String) {
        ctx.close()
        Reactor.logger.debug("Disconnected: $reason")
    }

    override fun isOnline(): Boolean = ctx.isActive

    override fun getIp(): String = ctx.remoteAddress().toString()
}
