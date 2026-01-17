package ink.reactor.kernel

import ink.reactor.kernel.event.EventBus
import ink.reactor.kernel.logger.Logger
import ink.reactor.kernel.logger.LoggerFactory

class Reactor private constructor(
    private val logger: Logger,
    private val loggerFactory: LoggerFactory,
    private val globalBus: EventBus
) {
    companion object {

        private var ref: Reactor? = null

        val logger: Logger get() = reactor.logger
        val loggerFactory: LoggerFactory get() = reactor.loggerFactory
        val bus: EventBus get() = reactor.globalBus

        fun init(
            logger: Logger,
            loggerFactory: LoggerFactory,
            globalBus: EventBus
        ) {
            if (ref != null) {
                error("Kernel already initialized")
            }
            ref = Reactor(logger, loggerFactory, globalBus)
        }

        private val reactor: Reactor
            get() = ref ?: error("Kernel not initialized")
    }
}
