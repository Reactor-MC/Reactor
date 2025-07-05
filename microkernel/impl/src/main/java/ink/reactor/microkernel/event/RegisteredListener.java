package ink.reactor.microkernel.event;

import ink.reactor.kernel.event.EventExecutor;
import ink.reactor.kernel.event.ListenerPriority;

public record RegisteredListener(
    EventExecutor executor,
    Class<?> eventClass,
    ListenerPriority priority
) {}