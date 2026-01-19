package ink.reactor.network.internal.packet

import ink.reactor.network.api.packet.CachedPacket
import ink.reactor.network.api.packet.Packet
import ink.reactor.network.api.packet.PacketsSender
import ink.reactor.network.api.player.PlayerConnection
import ink.reactor.network.internal.io.PacketEncoder
import ink.reactor.network.internal.io.frame.PacketFramer

class PacketSenderInternal: PacketsSender {

    override fun sendPacket(
        connections: Collection<PlayerConnection>,
        packet: Packet
    ) {
        if (connections.isEmpty()) return

        val targets = connections.filter { it.isOnline() }
        if (targets.isEmpty()) return

        val buf = PacketEncoder.allocateBuf(packet) ?: return
        try {
            PacketFramer.writeFramedPacket(packet, buf)
            val cached = CachedPacket(buf, targets.size)

            for (connection in targets) {
                connection.sendCachedPacket(cached)
            }
        } catch (e: Exception) {
            if (buf.refCnt() > 0) buf.release()
            throw e
        }
    }

    override fun sendPackets(
        connections: Collection<PlayerConnection>,
        vararg packets: Packet
    ) {
        if (connections.isEmpty()) return

        for (packet in packets) {
            sendPacket(connections, packet)
        }
    }
}
