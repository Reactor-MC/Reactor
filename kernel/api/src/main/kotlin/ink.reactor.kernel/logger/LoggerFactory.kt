package ink.reactor.kernel.logger

interface LoggerFactory {
    fun createLogger(name: String): Logger
    fun createLogger(builder: LoggerBuilder): Logger
}
