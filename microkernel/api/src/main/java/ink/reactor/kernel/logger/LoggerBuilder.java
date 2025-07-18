package ink.reactor.kernel.logger;

import ink.reactor.kernel.Reactor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class LoggerBuilder {
    private final LoggerFactory factory;

    public LoggerBuilder() {
        this.factory = Reactor.get().loggerFactory();
    }

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
