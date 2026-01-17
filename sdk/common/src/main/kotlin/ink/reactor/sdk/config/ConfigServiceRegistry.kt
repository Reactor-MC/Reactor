package ink.reactor.sdk.config

object ConfigServiceRegistry {
    private val services = mutableMapOf<String, ConfigService>()

    fun register(service: ConfigService) {
        service.fileExtensions.forEach { services[it] = service }
    }

    fun getService(ext: String): ConfigService = services[ext]
        ?: throw IllegalArgumentException("No service for extension: $ext")
}
