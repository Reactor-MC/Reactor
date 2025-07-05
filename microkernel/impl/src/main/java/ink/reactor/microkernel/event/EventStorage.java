package ink.reactor.microkernel.event;

import ink.reactor.kernel.event.ListenerPriority;

import java.util.ArrayList;
import java.util.Collection;

public final class EventStorage {
    private static final ListenerPriority[] LISTENER_PRIORITIES = ListenerPriority.values();

    @SuppressWarnings("unchecked")
    private final Collection<RegisteredListener>[] priorities = new Collection[LISTENER_PRIORITIES.length];

    public void remove(final RegisteredListener registeredListener) {
        final Collection<RegisteredListener> registeredListeners = priorities[registeredListener.priority().ordinal()];
        if (registeredListeners == null || registeredListeners.isEmpty()) {
            return;
        }
        registeredListeners.remove(registeredListener);
    }

    public void addListener(final RegisteredListener listener) {
        final int ordinal = listener.priority().ordinal();
        Collection<RegisteredListener> listeners = priorities[ordinal];
        if (listeners == null) {
            listeners = new ArrayList<>(1);
            priorities[ordinal] = listeners;
        }
        listeners.add(listener);
    }

    public void execute(final Object event) {
        for (final Collection<RegisteredListener> priorityExecutors : priorities) {
            if (priorityExecutors == null) {
                continue;
            }
            for (final RegisteredListener eventExecutor : priorityExecutors) {
                eventExecutor.executor().execute(event);
            }
        }
    }
}
