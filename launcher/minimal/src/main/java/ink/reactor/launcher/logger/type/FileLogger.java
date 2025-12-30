package ink.reactor.launcher.logger.type;

import ink.reactor.launcher.logger.data.LoggerLevels;
import ink.reactor.launcher.logger.file.FileWriter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FileLogger {
    private final LoggerLevels loggerLevels;
    private final FileWriter fileWriter;

    public void debug(final String message) {
        if (loggerLevels.debug()) {
            fileWriter.write(message.getBytes());
        }
    }

    public void log(final String message) {
        if (loggerLevels.log()) {
            fileWriter.write(message.getBytes());
        }
    }

    public void info(final String message) {
        if (loggerLevels.info()) {
            fileWriter.write(message.getBytes());
        }
    }

    public void warn(final String message) {
        if (loggerLevels.warn()) {
            fileWriter.write(message.getBytes());
        }
    }

    public void error(final String message) {
        if (loggerLevels.error()) {
            fileWriter.write(message.getBytes());
        }
    }

    public void error(final String message, final Throwable throwable) {
        if (loggerLevels.error()) {
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
