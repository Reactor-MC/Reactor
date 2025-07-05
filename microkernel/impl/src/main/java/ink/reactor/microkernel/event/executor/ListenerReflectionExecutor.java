package ink.reactor.microkernel.event.executor;

import java.lang.invoke.MethodHandle;

import ink.reactor.kernel.event.EventExecutor;
import ink.reactor.kernel.event.special.Cancellable;
import ink.reactor.kernel.logger.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ListenerReflectionExecutor implements EventExecutor {

    private final Logger logger;
    private final Object listener;
    private final boolean ignoreCancelled;
    private final MethodHandle methodHandle;

    @Override
    public void execute(final Object event) {
        if (event instanceof Cancellable cancellable && cancellable.isCancelled() && !ignoreCancelled) {
            return;
        }
        try {
            methodHandle.invoke(listener, event);
        } catch (final Throwable e) {
            logger.error("Error executing the listener %s", e, listener.getClass());
        }
    }
}