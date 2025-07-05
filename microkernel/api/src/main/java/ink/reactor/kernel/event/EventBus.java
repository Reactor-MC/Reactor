package ink.reactor.kernel.event;

import java.util.function.Consumer;

public interface EventBus {
    /**
     * Register all methods that use {@link Listener} as annotation
     * @param listener object with listener methods
     */
    void register(final Object listener);

    /**
     * Register a simple listener
     * @param eventClass event to hear
     * @param listener consumer of the event
     */
    <T> void register(final Class<T> eventClass, final Consumer<T> listener);

    /**
     * Register a listener with custom event executor,
     * all process of executing the listener is handled by the event-executor
     * @param listener instance of the listener to register
     * @param eventClass event to hear
     * @param priority listener priority
     * @param executor executor of the listener
     */
    void register(final Object listener, final Class<?> eventClass, final ListenerPriority priority, final EventExecutor executor);

    /**
     * Unregister all methods that use {@link Listener} as annotation
     * @param listener object with listener methods
     */
    void unregister(final Object listener);

    /**
     * Execute all listeners with the same event-class
     * @param event Custom event (can be any object)
     */
    void post(final Object event);

    /**
     * Remove all listener from eventbus
     */
    void clear();
}