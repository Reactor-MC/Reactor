package ink.reactor.microkernel.event.simplebus;

import ink.reactor.kernel.event.ListenerPhase;

final class EventStorage {
    private static final int LISTENER_PHASES_SIZE = ListenerPhase.values().length;

    private final ListenerStorage[] listenersPerPhase = new ListenerStorage[LISTENER_PHASES_SIZE];

    public void remove(final RegisteredListener listener) {
        final int ordinal = listener.phase().ordinal();
        final ListenerStorage listenerStorage = listenersPerPhase[ordinal];
        if (listenerStorage == null || listenerStorage.isEmpty()) {
            return;
        }
        if (listenerStorage.getSize() == 1 && listenerStorage.getListeners()[0].equals(listener)) {
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

            final RegisteredListener[] listeners = listenerStorage.getListeners();
            for (final RegisteredListener listener : listeners) {
                if (listener != null) {
                    listener.executor().execute(event);
                }
            }
        }
    }
}
