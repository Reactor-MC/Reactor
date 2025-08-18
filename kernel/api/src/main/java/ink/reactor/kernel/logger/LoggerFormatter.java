package ink.reactor.kernel.logger;

public interface LoggerFormatter {
    /**
     * Format a string replacing placeholders, example:
     * {@code format("hello %s", "world")}
     */
    String format(final String text, final Object... objects);
}