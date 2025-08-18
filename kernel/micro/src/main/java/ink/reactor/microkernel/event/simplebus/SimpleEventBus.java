package ink.reactor.microkernel.event.simplebus;

import ink.reactor.kernel.event.EventBus;
import ink.reactor.kernel.event.EventExecutor;
import ink.reactor.kernel.event.ListenerPhase;
import ink.reactor.kernel.logger.Logger;
import ink.reactor.microkernel.event.executor.ListenerConsumerExecutor;
import ink.reactor.microkernel.event.loader.MethodListenerLoader;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Setter
public final class SimpleEventBus implements EventBus {

    private final Map<Class<?>, EventStorage> eventsStorage;
    private final Map<Object, Collection<RegisteredListener>> owners;

    private final MethodListenerLoader methodListenerLoader;

    public SimpleEventBus(final Logger logger) {
        this.owners = new ConcurrentHashMap<>();
        this.eventsStorage = new ConcurrentHashMap<>();
        this.methodListenerLoader = new MethodListenerLoader(logger);
    }

    @Override
    public void register(final Object listener) {
        final Collection<MethodListenerLoader.MethodListener> methodListeners = methodListenerLoader.load(listener);
        if (methodListeners.isEmpty()) {
            return;
        }
        final Collection<RegisteredListener> listeners = owners.computeIfAbsent(listener, _ -> new ArrayList<>(methodListeners.size()));

        for (MethodListenerLoader.MethodListener methodListener : methodListeners) {
            final Class<?> eventClass = methodListener.eventClass();
            EventStorage storage = eventsStorage.get(eventClass);
            final RegisteredListener registeredListener = new RegisteredListener(methodListener.executor(), eventClass, methodListener.phase(), methodListener.priority());

            if (storage == null) {
                storage = new EventStorage();
                eventsStorage.put(eventClass, storage);
            }

            listeners.add(registeredListener);
            storage.addListener(registeredListener);
        }
    }

    @Override
    public <T> void register(final Class<T> eventClass, final Consumer<T> listener) {
        register(listener, eventClass, ListenerPhase.DEFAULT, new ListenerConsumerExecutor<>(listener));
    }

    @Override
    public void register(final Object listener, final Class<?> eventClass, final ListenerPhase phase, final EventExecutor executor) {
        EventStorage storage = eventsStorage.get(eventClass);
        if (storage == null) {
            storage = new EventStorage();
            eventsStorage.put(eventClass, storage);
        }

        final Collection<RegisteredListener> listeners = owners.computeIfAbsent(listener, _ -> new ArrayList<>(1));
        final RegisteredListener registeredListener = new RegisteredListener(executor, eventClass, phase, 0);

        listeners.add(registeredListener);
        storage.addListener(registeredListener);
    }

    @Override
    public void unregister(final Object listener) {
        final Collection<RegisteredListener> listeners = owners.remove(listener);
        if (listeners == null) {
            return;
        }

        for (final RegisteredListener registeredListener : listeners) {
            final EventStorage eventStorage = eventsStorage.get(registeredListener.eventClass());
            if (eventStorage != null) {
                eventStorage.remove(registeredListener);
            }
        }
    }

    @Override
    public void post(final Object event) {
        if (eventsStorage.isEmpty()) {
            return;
        }
        final EventStorage eventStorage = eventsStorage.get(event.getClass());
        if (eventStorage != null) {
            eventStorage.execute(event);
        }
    }

    @Override
    public void clear() {
        eventsStorage.clear();
    }
}
