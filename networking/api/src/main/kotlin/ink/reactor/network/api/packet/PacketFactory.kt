package ink.reactor.network.api.packet

fun interface PacketFactory {
    fun create(): Packet
}
