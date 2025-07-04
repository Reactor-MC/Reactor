package ink.reactor.kernel.logger;

public interface Logger {
    LoggerFormatter getLoggerFormatter();

    void log(final String message);
    void info(final String message);
    void warn(final String message);
    void error(final String message);
    void error(final String message, final Throwable throwable);

    default void log(final String message, Object... toFormat) {
        log(getLoggerFormatter().format(message, toFormat));
    }
    default void info(final String message, Object... toFormat) {
        info(getLoggerFormatter().format(message, toFormat));
    }
    default void warn(final String message, Object... toFormat) {
        warn(getLoggerFormatter().format(message, toFormat));
    }
    default void error(final String message, Object... toFormat) {
        error(getLoggerFormatter().format(message, toFormat));
    }
    default void error(final String message, final Throwable throwable, Object... toFormat) {
        error(getLoggerFormatter().format(message, toFormat), throwable);
    }
}