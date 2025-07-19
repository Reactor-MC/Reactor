package ink.reactor.launcher;

import ink.reactor.kernel.Reactor;
import ink.reactor.kernel.logger.Logger;
import ink.reactor.launcher.console.Console;
import ink.reactor.launcher.console.JLineConsole;
import ink.reactor.launcher.logger.LoggersLoader;
import ink.reactor.launcher.network.NetworkLoader;
import ink.reactor.microkernel.event.simplebus.SimpleEventBus;
import ink.reactor.microkernel.logger.SimpleLoggerFactory;
import ink.reactor.microkernel.scheduler.TickScheduler;
import ink.reactor.sdk.bundled.config.json.JsonConfigService;
import ink.reactor.sdk.bundled.config.yaml.YamlConfigService;
import ink.reactor.sdk.config.ConfigService;
import ink.reactor.sdk.config.ConfigServiceRegistry;

import java.util.ArrayList;
import java.util.Collection;

public final class ReactorLauncher {

    public static final Collection<Runnable> STOP_TASKS = new ArrayList<>();

    public static void main(final String[] args)  {
        final Console console = ReactorLauncher.startServer();
        if (console != null) {
            console.run();
        }
    }

    private static Console startServer() {
        final long start = System.currentTimeMillis();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (final Runnable runnable : STOP_TASKS) {
                runnable.run();
            }
        }));

        final Console console = JLineConsole.createConsole();
        if (console == null) {
            return null;
        }

        final ConfigService configService = new YamlConfigService();
        ConfigServiceRegistry.addProvider(configService);
        ConfigServiceRegistry.addProvider(new JsonConfigService());

        final Logger logger = new LoggersLoader(console.getTerminal().writer()).load(configService);

        if (!new NetworkLoader(logger).load(configService)) {
            return null;
        }

        Reactor.setInstance(new Reactor(
            logger,
            new SimpleLoggerFactory(logger),
            new TickScheduler(),
            new SimpleEventBus(logger)
        ));

        logger.info("Server started in: " + (System.currentTimeMillis() - start) + "ms");
        return console;
    }
}
