package ink.reactor.kernel.logger;

public interface LoggerFormatter {
    String format(final String text, final Object... objects);
}