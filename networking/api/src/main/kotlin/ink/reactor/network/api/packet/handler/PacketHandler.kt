package ink.reactor.network.api.packet.handler

import ink.reactor.network.api.packet.Packet
import ink.reactor.network.api.player.PlayerConnection

interface PacketHandler {
    fun handle(connection: PlayerConnection, packet: Packet)
    fun packetId(): Int
}
