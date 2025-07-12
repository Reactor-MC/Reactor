package ink.reactor.launcher.logger.data;

public record LoggerLevels(
    boolean debug,
    boolean log,
    boolean info,
    boolean warn,
    boolean error
) {}
