package ink.reactor.launcher

import ink.reactor.kernel.Reactor
import ink.reactor.launcher.console.Console
import ink.reactor.launcher.console.JLineConsole.createConsole
import ink.reactor.launcher.logger.LoggersLoader
import ink.reactor.microkernel.event.simplebus.SimpleEventBus
import ink.reactor.microkernel.logger.SimpleLoggerFactory
import ink.reactor.sdk.bundled.config.yaml.YamlConfigService
import ink.reactor.sdk.config.ConfigServiceRegistry

fun main() {
    MinimalReactorLauncher().startServer()?.run()
}

class MinimalReactorLauncher internal constructor() {

    companion object {
        private val STOP_TASKS: MutableList<() -> Unit> = mutableListOf()
        fun addStopTask(task: () -> Unit) {STOP_TASKS.add(task)}
    }

    internal fun startServer(): Console? {
        val start = System.currentTimeMillis()

        Runtime.getRuntime().addShutdownHook(Thread { STOP_TASKS.forEach {
            runCatching(it)
                .onFailure { e ->
                    System.err.println("Shutdown task failed")
                    e.printStackTrace()
                }
            }})

        val console = createConsole() ?: return null

        val configService = YamlConfigService()
        ConfigServiceRegistry.register(configService)

        val logger = LoggersLoader(console.terminal.writer())
            .load(configService)

        Reactor.init(
            logger,
            SimpleLoggerFactory(logger),
            SimpleEventBus(logger)
        )

        logger.info("Server started in ${System.currentTimeMillis() - start}ms")
        return console
    }
}
