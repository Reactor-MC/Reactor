package ink.reactor.launcher.logger.data;

public record StyleLog(
    String prefix,
    String text,
    String afterText
) {
    public String style(final String prefix, final String message) {
        return this.prefix + prefix + this.text + message + this.afterText;
    }
}
