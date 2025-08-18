package ink.reactor.microkernel.logger;

import ink.reactor.kernel.logger.Logger;
import ink.reactor.kernel.logger.LoggerBuilder;
import ink.reactor.kernel.logger.LoggerFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleLoggerFactory implements LoggerFactory {

    private final Logger defaultLogger;

    @Override
    public Logger createLogger(final String prefix) {
        return new WrappedLogger(prefix, "", defaultLogger);
    }

    @Override
    public Logger createLogger(final LoggerBuilder builder) {
        return new WrappedLogger(
            builder.getSuffix(),
            builder.getPrefix(),
            defaultLogger
        );
    }
}
