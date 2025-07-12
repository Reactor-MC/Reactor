package ink.reactor.launcher.logger.file;

import ink.reactor.launcher.logger.config.LoggerConfig;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FileLogger {
    private final LoggerConfig loggerConfig;
    private final FileWriter fileWriter;

    public void debug(final String message) {
        if (loggerConfig.debug()) {
            fileWriter.write(message.getBytes());
        }
    }

    public void log(final String message) {
        if (loggerConfig.log()) {
            fileWriter.write(message.getBytes());
        }
    }

    public void info(final String message) {
        if (loggerConfig.info()) {
            fileWriter.write(message.getBytes());
        }
    }

    public void warn(final String message) {
        if (loggerConfig.warn()) {
            fileWriter.write(message.getBytes());
        }
    }

    public void error(final String message) {
        if (loggerConfig.error()) {
            fileWriter.write(message.getBytes());
        }
    }

    public void error(final String message, final Throwable throwable) {
        if (loggerConfig.error()) {
            fileWriter.write(message.getBytes());
        }
    }
}
