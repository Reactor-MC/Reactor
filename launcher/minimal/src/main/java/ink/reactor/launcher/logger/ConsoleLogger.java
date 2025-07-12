package ink.reactor.launcher.logger;

import ink.reactor.launcher.logger.config.LoggerConfig;
import ink.reactor.launcher.logger.config.StyleLog;
import lombok.RequiredArgsConstructor;

import java.io.PrintWriter;

@RequiredArgsConstructor
public final class ConsoleLogger {
    private final PrintWriter writer;
    private final LoggerConfig loggerConfig;
    private final StyleLog debugStyle, logStyle, infoStyle, warnStyle, errorStyle;

    public void debug(final String prefix, final String message) {
        if (loggerConfig.debug()) {
            writer.println(debugStyle.style(prefix, message));
        }
    }

    public void log(final String prefix, final String message) {
        if (loggerConfig.log()) {
            writer.println(logStyle.style(prefix, message));
        }
    }

    public void info(final String prefix, final String message) {
        if (loggerConfig.info()) {
            writer.println(infoStyle.style(prefix, message));
        }
    }

    public void warn(final String prefix, final String message) {
        if (loggerConfig.warn()) {
            writer.println(warnStyle.style(prefix, message));
        }
    }

    public void error(final String prefix, final String message) {
        if (loggerConfig.error()) {
            writer.println(errorStyle.style(prefix, message));
        }
    }

    public void error(final String prefix, final String message, final Throwable throwable) {
        if (loggerConfig.error()) {
            writer.println(errorStyle.style(prefix, message));
            throwable.printStackTrace(System.err);
        }
    }
}
