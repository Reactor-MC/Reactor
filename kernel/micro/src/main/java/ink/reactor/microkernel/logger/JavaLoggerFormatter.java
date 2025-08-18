package ink.reactor.microkernel.logger;

import ink.reactor.kernel.logger.LoggerFormatter;

import java.util.Formatter;

public final class JavaLoggerFormatter implements LoggerFormatter {
    private final Formatter formatter;

    public JavaLoggerFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    public JavaLoggerFormatter() {
        this.formatter = new Formatter();
    }

    @Override
    public String format(final String text, final Object... objects) {
        return (objects == null || objects.length == 0) ? text : formatter.format(text, objects).toString();
    }
}
