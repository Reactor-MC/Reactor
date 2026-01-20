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
        return new WrappedLogger("", prefix.endsWith(" ") ? prefix : prefix + " ", defaultLogger, defaultLogger.getLoggerFormatter());
    }

    @Override
    public @NotNull Logger createLogger(final LoggerBuilder builder) {
        final String prefix = builder.getPrefix();
        final String suffix = builder.getSuffix();
        return new WrappedLogger(
            suffix.startsWith(" ") ? suffix : " " + suffix,
            prefix.endsWith(" ") ? prefix : prefix + " ",
            defaultLogger,
            builder.getFormatter() != null ? builder.getFormatter() : defaultLogger.getLoggerFormatter()
        );
    }
}
