package ink.reactor.kernel;

import ink.reactor.kernel.event.EventBus;
import ink.reactor.kernel.logger.Logger;
import ink.reactor.kernel.logger.LoggerFactory;
import ink.reactor.kernel.scheduler.Scheduler;
import lombok.NonNull;

public record Reactor(
    Logger logger,
    LoggerFactory loggerFactory,
    Scheduler scheduler,
    EventBus globalBus
) {
    private static Reactor instance;

    public static Reactor get() {
        return instance;
    }

    public static void setInstance(final @NonNull Reactor instance) {
        if (Reactor.instance != null) {
            throw new IllegalStateException("Kernel api is already set");
        }
        Reactor.instance = instance;
    }
}
