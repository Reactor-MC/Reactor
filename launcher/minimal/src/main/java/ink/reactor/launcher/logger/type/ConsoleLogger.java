package ink.reactor.launcher.logger.type;

import ink.reactor.launcher.logger.data.LoggerLevels;
import ink.reactor.launcher.logger.data.StyleLog;
import lombok.RequiredArgsConstructor;

import java.io.PrintWriter;

@RequiredArgsConstructor
public final class ConsoleLogger {
    private final PrintWriter writer;
    private final LoggerLevels loggerLevels;
    private final StyleLog debugStyle, logStyle, infoStyle, warnStyle, errorStyle;

    public void debug(final String prefix, final String message) {
        if (loggerLevels.debug()) {
            writer.println(debugStyle.style(prefix, message));
        }
    }

    public void log(final String prefix, final String message) {
        if (loggerLevels.log()) {
            writer.println(logStyle.style(prefix, message));
        }
    }

    public void info(final String prefix, final String message) {
        if (loggerLevels.info()) {
            writer.println(infoStyle.style(prefix, message));
        }
    }

    public void warn(final String prefix, final String message) {
        if (loggerLevels.warn()) {
            writer.println(warnStyle.style(prefix, message));
        }
    }

    public void error(final String prefix, final String message) {
        if (loggerLevels.error()) {
            writer.println(errorStyle.style(prefix, message));
        }
    }

    public void error(final String prefix, final String message, final Throwable throwable) {
        if (loggerLevels.error()) {
            writer.println(errorStyle.style(prefix, message));
            throwable.printStackTrace(System.err);
        }
    }
}
