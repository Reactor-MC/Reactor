package ink.reactor.launcher;

import ink.reactor.kernel.Reactor;
import ink.reactor.kernel.ReactorServer;
import ink.reactor.kernel.event.common.StopEvent;
import ink.reactor.kernel.logger.Logger;
import ink.reactor.launcher.console.Console;
import ink.reactor.launcher.console.ConsoleStart;
import ink.reactor.launcher.logger.DefaultLogger;
import ink.reactor.launcher.logger.config.LoggerConfigLoader;
import ink.reactor.microkernel.event.simplebus.SimpleEventBus;
import ink.reactor.microkernel.logger.SimpleLoggerFactory;
import ink.reactor.microkernel.scheduler.TickScheduler;
import ink.reactor.sdk.bundled.config.json.JsonConfigService;
import ink.reactor.sdk.bundled.config.yaml.YamlConfigService;
import ink.reactor.sdk.config.ConfigService;
import ink.reactor.sdk.config.ConfigServiceRegistry;

import java.io.IOException;

public final class ReactorLauncher {

    public static void main(String[] args)  {
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

        Logger logger = new DefaultLogger();
        final SimpleEventBus eventBus = new SimpleEventBus();
        eventBus.setLogger(logger);

        final ConfigService configService = ConfigServiceRegistry.getProvider("yml");
        try {
            logger = new LoggerConfigLoader(terminal.writer(), eventBus).loadLogger(
                configService.createIfAbsentAndLoad("logger.yml", getClass().getClassLoader()));
        } catch (final IOException e) {
            logger.error("Can't load logger.yml", e);
            return null;
        }

        eventBus.setLogger(logger);
        Reactor.setServer(new ReactorServer(
            logger,
            new SimpleLoggerFactory(logger),
            new TickScheduler(),
            eventBus
        ));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> eventBus.post(new StopEvent(start, System.currentTimeMillis() + start))));

        final Console console = ConsoleStart.createConsole(terminal, eventBus);
        logger.info("Server started in: " + (System.currentTimeMillis() - start));
        return console;
    }
}
