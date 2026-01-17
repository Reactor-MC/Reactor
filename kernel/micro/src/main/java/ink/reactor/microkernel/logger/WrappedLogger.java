package ink.reactor.microkernel.logger;

import ink.reactor.kernel.logger.Logger;
import ink.reactor.kernel.logger.LoggerFormatter;
import org.jetbrains.annotations.NotNull;

public record WrappedLogger(
    String suffix,
    String prefix,
    Logger logger,
    LoggerFormatter loggerFormatter
) implements Logger {

    @Override
    public @NotNull LoggerFormatter getLoggerFormatter() {
        return loggerFormatter;
    }

    @Override
    public void debug(final @NotNull String message) {
        logger.debug(prefix + message + suffix);
    }

    @Override
    public void log(final @NotNull String message) {
        logger.log(prefix + message + suffix);
    }

    @Override
    public void info(final @NotNull String message) {
        logger.info(prefix + message + suffix);
    }

    @Override
    public void warn(final @NotNull String message) {
        logger.warn(prefix + message + suffix);
    }

    @Override
    public void error(final @NotNull String message) {
        logger.error(prefix + message + suffix);
    }

    @Override
    public void error(final @NotNull String message, final @NotNull Throwable throwable) {
        logger.error(prefix + message + suffix, throwable);
    }
}
