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

        private val STOP_TASKS: MutableList<() -> Unit> = mutableListOf()
        fun addStopTask(task: () -> Unit) {STOP_TASKS.add(task)}

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

            Runtime.getRuntime().addShutdownHook(Thread { STOP_TASKS.forEach {
                runCatching(it)
                    .onFailure { e ->
                        System.err.println("Shutdown task failed")
                        e.printStackTrace()
                    }
            }})
        }

        private val reactor: Reactor
            get() = ref ?: error("Kernel not initialized")
    }
}
