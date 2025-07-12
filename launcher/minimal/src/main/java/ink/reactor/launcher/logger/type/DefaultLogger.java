package ink.reactor.launcher.logger.type;

import ink.reactor.kernel.logger.Logger;
import ink.reactor.kernel.logger.LoggerFormatter;
import ink.reactor.microkernel.logger.JavaLoggerFormatter;

public final class DefaultLogger implements Logger {
    @Override
    public LoggerFormatter getLoggerFormatter() {
        return new JavaLoggerFormatter();
    }

    @Override
    public void debug(final String message) {
        System.out.println("[DEBUG] " + message);
    }

    @Override
    public void log(final String message) {
        System.out.println("[LOG] " + message);
    }

    @Override
    public void info(final String message) {
        System.out.println("[INFO] " + message);
    }

    @Override
    public void warn(final String message) {
        System.out.println("[WARN] " + message);
    }

    @Override
    public void error(final String message) {
        System.err.println("[ERROR] " + message);
    }

    @Override
    public void error(final String message, final Throwable throwable) {
        System.err.println("[ERROR] " + message);
        throwable.printStackTrace(System.err);
    }
}
