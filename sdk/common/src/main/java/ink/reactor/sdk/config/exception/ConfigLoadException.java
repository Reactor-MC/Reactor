package ink.reactor.sdk.config.exception;

public class ConfigLoadException extends RuntimeException {
    public ConfigLoadException(String message) {
        super(message);
    }

    public ConfigLoadException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
