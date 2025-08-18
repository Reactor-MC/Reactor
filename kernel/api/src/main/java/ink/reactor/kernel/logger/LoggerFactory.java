package ink.reactor.kernel.logger;

public interface LoggerFactory {
    Logger createLogger(final String name);
    Logger createLogger(final LoggerBuilder builder);
}