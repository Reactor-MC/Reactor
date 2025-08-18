package ink.reactor.microkernel.event.simplebus;

import ink.reactor.kernel.event.EventExecutor;
import ink.reactor.kernel.event.ListenerPhase;

record RegisteredListener(
    EventExecutor executor,
    Class<?> eventClass,
    ListenerPhase phase,
    int priority
) {}