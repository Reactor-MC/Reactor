package ink.reactor.kernel.logger;

public interface LoggerFactory {
    Logger createLogger();
    Logger createLogger(final LoggerBuilder builder);
}