package ink.reactor.kernel.event;

public interface EventBus {
    /**
     * Register all methods that use {@link Listener} as annotation
     * @param listener object with listener methods
     */
    void register(final Object listener);

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