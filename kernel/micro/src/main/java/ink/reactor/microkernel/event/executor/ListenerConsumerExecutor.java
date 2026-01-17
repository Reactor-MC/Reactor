package ink.reactor.microkernel.event.executor;

import java.util.function.Consumer;

import ink.reactor.kernel.event.EventExecutor;
import ink.reactor.kernel.event.special.Cancellable;
import org.jetbrains.annotations.NotNull;

public final class ListenerConsumerExecutor<T> implements EventExecutor {
    private final Consumer<T> consumer;

    public ListenerConsumerExecutor(final Consumer<T> consumer) {
        this.consumer = consumer;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(final @NotNull Object event) {
        if (event instanceof Cancellable cancellable && cancellable.isCancelled()) {
            return;
        }
        consumer.accept((T)event);
    }
}
