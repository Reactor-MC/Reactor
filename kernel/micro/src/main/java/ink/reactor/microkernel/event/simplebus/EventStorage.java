package ink.reactor.microkernel.event.simplebus;

import ink.reactor.kernel.event.ListenerPhase;

final class EventStorage {
    private static final int LISTENER_PHASES_SIZE = ListenerPhase.getEntries().size();

    private final ListenerStorage[] listenersPerPhase = new ListenerStorage[LISTENER_PHASES_SIZE];

    public void remove(final RegisteredListener listener) {
        final int ordinal = listener.phase().ordinal();
        final ListenerStorage listenerStorage = listenersPerPhase[ordinal];
        if (listenerStorage == null || listenerStorage.isEmpty()) {
            return;
        }
        if (listenerStorage.size == 1 && listenerStorage.listeners[0].equals(listener)) {
            listenersPerPhase[ordinal] = null;
            return;
        }
        listenerStorage.remove(listener);
    }

    public void addListener(final RegisteredListener listener) {
        if (listener.executor() == null) {
            throw new IllegalStateException(
                "RegisteredListener has null executor: event="
                    + listener.eventClass() + " phase=" + listener.phase()
            );
        }

        final int ordinal = listener.phase().ordinal();
        ListenerStorage listenerStorage = listenersPerPhase[ordinal];
        if (listenerStorage == null) {
            listenerStorage = new ListenerStorage(listener);
            listenersPerPhase[ordinal] = listenerStorage;
            return;
        }
        listenerStorage.add(listener);
    }

    public void execute(final Object event) {
        for (final ListenerStorage listenerStorage : listenersPerPhase) {
            if (listenerStorage == null) {
                continue;
            }

            listenerStorage.ensureSorted();

            final RegisteredListener[] listeners = listenerStorage.listeners;
            final int size = listenerStorage.size;
            for (int i = 0; i < size; i++) {
                listeners[i].executor().execute(event);
            }
        }
    }
}
