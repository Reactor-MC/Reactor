package ink.reactor.network.internal

import ink.reactor.network.api.NetworkConnector
import ink.reactor.network.api.packet.PacketRegistry
import ink.reactor.network.internal.config.ConnectionConfig
import ink.reactor.network.internal.packet.PacketSenderInternal
import ink.reactor.network.internal.packet.storage.StatePacketHandlerStorage

class NetworkInternalConnector private constructor(
    private val config: ConnectionConfig,
) {
    companion object {

        private var ref: NetworkInternalConnector? = null

        val config: ConnectionConfig get() = connector.config

        fun init(
            config: ConnectionConfig,
        ) {
            if (ref != null) {
                error("Network internal connector already initialized")
            }
            ref = NetworkInternalConnector(config)

            NetworkConnector.init(
                StatePacketHandlerStorage(PacketRegistry.MAX_PACKETS),
                PacketSenderInternal())
        }

        private val connector: NetworkInternalConnector
            get() = ref ?: error("Network internal connector not initialized")
    }
}
