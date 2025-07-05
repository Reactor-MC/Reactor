package ink.reactor.microkernel.logger;

import ink.reactor.kernel.logger.Logger;
import ink.reactor.kernel.logger.LoggerFormatter;

public record WrappedLogger(
    String suffix,
    String prefix,
    Logger logger
) implements Logger {

    @Override
    public LoggerFormatter getLoggerFormatter() {
        return logger.getLoggerFormatter();
    }

    @Override
    public void log(final String message) {
        logger.log(prefix + message + suffix);
    }

    @Override
    public void info(final String message) {
        logger.info(prefix + message + suffix);
    }

    @Override
    public void warn(final String message) {
        logger.warn(prefix + message + suffix);
    }

    @Override
    public void error(final String message) {
        logger.error(prefix + message + suffix);
    }

    @Override
    public void error(final String message, final Throwable throwable) {
        logger.error(prefix + message + suffix, throwable);
    }
}