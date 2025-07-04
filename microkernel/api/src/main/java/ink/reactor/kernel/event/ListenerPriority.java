package ink.reactor.kernel.event;

/**
 * Represents the order in which event listeners are executed.
 * <p>
 * Execution rules:
 * 1. Listeners are called in this order:
 *    FIRST -> EARLY -> DEFAULT -> LATE -> LAST -> MONITOR.
 * <p>
 * 2. If multiple listeners have the same priority, their execution order is undefined
 * (implementation-dependent, but usually registration order).
 */
public enum ListenerPriority {
    /**
     * Runs first, before most other listeners. Useful for setting initial state.
     */
    FIRST,

    /**
     * Runs early, but after FIRST. Can modify the event before DEFAULT listeners.
     */
    EARLY,

    /**
     * The default order if no priority is specified.
     */
    DEFAULT,

    /**
     * Runs after DEFAULT listeners. Can override previous changes.
     */
    LATE,

    /**
     * Runs last (before MONITOR). Has the final say in event changes.
     */
    LAST,

    /**
     * Only observes the event after all modifications are done. Should NOT modify the event.
     */
    MONITOR
}