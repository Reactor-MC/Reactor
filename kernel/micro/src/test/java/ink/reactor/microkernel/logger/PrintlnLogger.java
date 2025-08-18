package ink.reactor.microkernel.logger;

import ink.reactor.kernel.logger.Logger;
import ink.reactor.kernel.logger.LoggerFormatter;

public final class PrintlnLogger implements Logger {
    private final JavaLoggerFormatter loggerFormatter = new JavaLoggerFormatter();

    @Override
    public LoggerFormatter getLoggerFormatter() {
        return loggerFormatter;
    }

    @Override
    public void debug(final String message) {
        System.out.println("[DEBUG]" + message);
    }

    @Override
    public void log(final String message) {
        System.out.println("[LOG]" + message);
    }

    @Override
    public void info(final String message) {
        System.out.println("[INFO]" + message);
    }

    @Override
    public void warn(final String message) {
        System.out.println("[WARN]" + message);
    }

    @Override
    public void error(final String message) {
        System.out.println("[ERROR]" + message);
    }

    @Override
    public void error(final String message, final Throwable throwable) {
        System.err.println("[ERROR]" + message);
        throwable.printStackTrace(System.err);
    }
}
