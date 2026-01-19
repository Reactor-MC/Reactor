package ink.reactor.network.internal.config

class ConnectionConfig(
    val host: String,
    val port: Int,

    val bossThreadCount: Int,
    val workerThreadCount: Int,

    val zstdCompressionLevel: Int,
    val readTimeoutSeconds: Int,
)
