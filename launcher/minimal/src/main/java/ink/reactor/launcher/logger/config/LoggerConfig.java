package ink.reactor.launcher.logger.config;

public record LoggerConfig(
    boolean debug,
    boolean log,
    boolean info,
    boolean warn,
    boolean error
) {}
