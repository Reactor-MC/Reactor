package ink.reactor.launcher;

import ink.reactor.kernel.Reactor;
import ink.reactor.kernel.ReactorServer;
import ink.reactor.kernel.event.common.StopEvent;
import ink.reactor.kernel.logger.Logger;
import ink.reactor.launcher.console.Console;
import ink.reactor.launcher.console.ConsoleStart;
import ink.reactor.launcher.logger.type.DefaultLogger;
import ink.reactor.launcher.logger.LoggersLoader;
import ink.reactor.launcher.network.NetworkLoader;
import ink.reactor.microkernel.event.simplebus.SimpleEventBus;
import ink.reactor.microkernel.logger.SimpleLoggerFactory;
import ink.reactor.microkernel.scheduler.TickScheduler;
import ink.reactor.sdk.bundled.config.json.JsonConfigService;
import ink.reactor.sdk.bundled.config.yaml.YamlConfigService;
import ink.reactor.sdk.config.ConfigService;
import ink.reactor.sdk.config.ConfigServiceRegistry;

public final class ReactorLauncher {

    public static void main(final String[] args)  {
        final Console console = new ReactorLauncher().startServer();
        if (console != null) {
            console.run();
        }
    }

    private Console startServer() {
        final long start = System.currentTimeMillis();

        final ConsoleStart.TerminalHolder terminal = ConsoleStart.createTerminal();
        if (terminal == null) {
            return null;
        }

        ConfigServiceRegistry.addProvider(new YamlConfigService());
        ConfigServiceRegistry.addProvider(new JsonConfigService());

        final Logger defaultLogger = new DefaultLogger();
        final SimpleEventBus eventBus = new SimpleEventBus();
        eventBus.setLogger(defaultLogger);

        final ConfigService configService = ConfigServiceRegistry.getProvider("yml");

        final Logger logger = new LoggersLoader(terminal.writer(), eventBus).load(configService, defaultLogger);
        eventBus.setLogger(logger);

        Reactor.setServer(new ReactorServer(
            logger,
            new SimpleLoggerFactory(logger),
            new TickScheduler(),
            eventBus
        ));

        new NetworkLoader(logger, eventBus).load(configService);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> eventBus.post(new StopEvent(start, System.currentTimeMillis() + start))));

        final Console console = ConsoleStart.createConsole(terminal, eventBus);
        logger.info("Server started in: " + (System.currentTimeMillis() - start));
        return console;
    }
}
