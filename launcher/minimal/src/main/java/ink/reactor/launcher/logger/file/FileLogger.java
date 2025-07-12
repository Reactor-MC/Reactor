package ink.reactor.launcher.logger.file;

import ink.reactor.launcher.logger.config.LoggerConfig;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FileLogger {
    private final LoggerConfig loggerConfig;
    private final FileWriter fileWriter;

    public void debug(final String message) {
        if (loggerConfig.debug() && fileWriter.canWrite()) {
            fileWriter.write(message.getBytes());
        }
    }

    public void log(final String message) {
        if (loggerConfig.log() && fileWriter.canWrite()) {
            fileWriter.write(message.getBytes());
        }
    }

    public void info(final String message) {
        if (loggerConfig.info() && fileWriter.canWrite()) {
            fileWriter.write(message.getBytes());
        }
    }

    public void warn(final String message) {
        if (loggerConfig.warn() && fileWriter.canWrite()) {
            fileWriter.write(message.getBytes());
        }
    }

    public void error(final String message) {
        if (loggerConfig.error() && fileWriter.canWrite()) {
            fileWriter.write(message.getBytes());
        }
    }

    public void error(final String message, final Throwable throwable) {
        if (loggerConfig.error() && fileWriter.canWrite()) {
            fileWriter.write(message.getBytes());
            fileWriter.write(('\n' + throwable.toString()).getBytes());

            for (StackTraceElement element : throwable.getStackTrace()) {
                fileWriter.write(("\n\tat " + element.toString()).getBytes());
            }

            Throwable cause = throwable.getCause();
            while (cause != null) {
                fileWriter.write(("\nCaused by: " + cause).getBytes());
                for (StackTraceElement element : cause.getStackTrace()) {
                    fileWriter.write(("\n\tat " + element.toString()).getBytes());
                }
                cause = cause.getCause();
            }
        }
    }
}
