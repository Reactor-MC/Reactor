package ink.reactor.kernel.logger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class LoggerBuilder {
    private final LoggerFactory factory;

    private String prefix, suffix;
    private LoggerFormatter formatter;

    public LoggerBuilder withPrefix(final String prefix) {
        this.prefix = prefix;
        return this;
    }

    public LoggerBuilder withSuffix(final String suffix) {
        this.suffix = suffix;
        return this;
    }

    public LoggerBuilder withFormatter(final LoggerFormatter formatter) {
        this.formatter = formatter;
        return this;
    }

    public Logger build() {
        return factory.createLogger(this);
    }
}