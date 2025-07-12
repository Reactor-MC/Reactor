package ink.reactor.launcher.logger;

import ink.reactor.kernel.logger.Logger;
import ink.reactor.kernel.logger.LoggerFormatter;
import ink.reactor.launcher.logger.file.FileLogger;
import ink.reactor.microkernel.logger.JavaLoggerFormatter;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public final class ReactorLogger implements Logger {
    private static final ZoneId ZONE_ID = Clock.systemDefaultZone().getZone();

    private final JavaLoggerFormatter formatter;

    private final ConsoleLogger consoleLogger;
    private final FileLogger fileLogger;

    private final String debugPrefix, logPrefix, infoPrefix, warnPrefix, errorPrefix;
    private final DateTimeFormatter dateTimeFormatter;

    @Override
    public LoggerFormatter getLoggerFormatter() {
        return formatter;
    }

    @Override
    public void debug(String message) {
        final String prefix = formatTime(debugPrefix);
        if (consoleLogger != null) {
            consoleLogger.debug(prefix, message);
        }

        message = prefix + message;
        if (fileLogger != null) {
            fileLogger.debug(message);
        }
    }

    @Override
    public void log(String message) {
        final String prefix = formatTime(logPrefix);
        if (consoleLogger != null) {
            consoleLogger.log(prefix, message);
        }

        message = prefix + message;
        if (fileLogger != null) {
            fileLogger.log(message);
        }
    }

    @Override
    public void info(String message) {
        final String prefix = formatTime(infoPrefix);
        if (consoleLogger != null) {
            consoleLogger.info(prefix, message);
        }

        message = prefix + message;
        if (fileLogger != null) {
            fileLogger.info(message);
        }
    }

    @Override
    public void warn(String message) {
        final String prefix = formatTime(warnPrefix);
        if (consoleLogger != null) {
            consoleLogger.warn(prefix, message);
        }

        message = prefix + message;
        if (fileLogger != null) {
            fileLogger.warn(message);
        }
    }

    @Override
    public void error(String message) {
        final String prefix = formatTime(errorPrefix);
        if (consoleLogger != null) {
            consoleLogger.error(prefix, message);
        }

        message = prefix + message;
        if (fileLogger != null) {
            fileLogger.error(message);
        }
    }

    @Override
    public void error(String message, final Throwable throwable) {
        final String prefix = formatTime(errorPrefix);
        if (consoleLogger != null) {
            consoleLogger.error(prefix, message, throwable);
        }

        message = prefix + message;
        if (fileLogger != null) {
            fileLogger.error(message, throwable);
        }
    }

    private String formatTime(final String prefix) {
        if (dateTimeFormatter == null) {
            return prefix;
        }
        return prefix.replace("%time%", LocalDateTime.now(ZONE_ID).format(dateTimeFormatter));
    }
}
