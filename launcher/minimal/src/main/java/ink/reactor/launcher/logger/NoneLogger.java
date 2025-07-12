package ink.reactor.launcher.logger;

import ink.reactor.kernel.logger.Logger;
import ink.reactor.kernel.logger.LoggerFormatter;

public final class NoneLogger implements Logger {
    public LoggerFormatter getLoggerFormatter() {
        return (s, _) -> s;
    }
    public void debug(final String message) {}
    public void log(final String message) {}
    public void info(final String message) {}
    public void warn(final String message) {}
    public void error(final String message) {}
    public void error(final String message, final Throwable throwable) {}
}
