package ink.reactor.kernel.event

import java.util.function.Consumer

interface EventBus {
    /**
     * Register all methods that use {@link Listener} as annotation
     * @param listener object with listener methods
     */
    fun register(listener: Any)

    /**
     * Register a simple listener
     * @param eventClass event to hear
     * @param listener consumer of the event
     */
    fun <T> register(eventClass: Class<T>, listener: Consumer<T>)

    /**
     * Register a listener with custom event executor,
     * all process of executing the listener is handled by the event-executor
     * @param listener instance of the listener to register
     * @param eventClass event to hear
     * @param phase listener phase
     * @param executor executor of the listener
     */
    fun register(listener: Any, eventClass: Class<*>, phase: ListenerPhase, executor: EventExecutor)

    /**
     * @param listener object with listener methods
     */
    fun unregister(listener: Any)

    /**
     * Execute all listeners with the same event-class
     * @param event Custom event (can be any object)
     */
    fun post(event: Any)

    /**
     * Remove all listener from eventbus
     */
    fun clear()
}
