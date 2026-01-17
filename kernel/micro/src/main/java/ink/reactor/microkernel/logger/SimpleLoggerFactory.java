package ink.reactor.microkernel.logger;

import ink.reactor.kernel.logger.Logger;
import ink.reactor.kernel.logger.LoggerBuilder;
import ink.reactor.kernel.logger.LoggerFactory;
import org.jetbrains.annotations.NotNull;

public class SimpleLoggerFactory implements LoggerFactory {

    private final @NotNull Logger defaultLogger;

    public SimpleLoggerFactory(final @NotNull Logger defaultLogger) {
        this.defaultLogger = defaultLogger;
    }

    @Override
    public @NotNull Logger createLogger(final @NotNull String prefix) {
        return new WrappedLogger(prefix, "", defaultLogger, defaultLogger.getLoggerFormatter());
    }

    @Override
    public @NotNull Logger createLogger(final LoggerBuilder builder) {
        return new WrappedLogger(
            builder.getSuffix(),
            builder.getPrefix(),
            defaultLogger,
            builder.getFormatter() != null ? builder.getFormatter() : defaultLogger.getLoggerFormatter()
        );
    }
}
