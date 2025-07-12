package ink.reactor.sdk.config.exception;

public class ConfigSaveException extends RuntimeException {
    public ConfigSaveException(String message) {
        super(message);
    }

    public ConfigSaveException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
