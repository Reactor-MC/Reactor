package ink.reactor.microkernel.event.simplebus;


import java.util.Arrays;
import java.util.Comparator;

final class ListenerStorage {
    public RegisteredListener[] listeners;
    public int size;
    public boolean sorted;

    public ListenerStorage(RegisteredListener listener) {
        this.listeners = new RegisteredListener[] {listener};
        this.size = 1;
        this.sorted = true;
    }

    public void add(final RegisteredListener listener) {
        if (size == listeners.length) {
            listeners = Arrays.copyOf(listeners, size * 2);
        }
        listeners[size++] = listener;
        if (listener.priority() != 0) { // Optimization: Don't sort array if isn't necessary - Default priority
            sorted = false;
        }
    }

    public void remove(final RegisteredListener listener) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (listeners[i] == listener) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return;
        }

        int numToMove = size - index - 1;
        if (numToMove > 0) {
            System.arraycopy(listeners, index + 1, listeners, index, numToMove);
        }
        listeners[--size] = null;
        sorted = false;
    }

    public void ensureSorted() {
        if (!sorted) {
            Arrays.sort(listeners, 0, size, Comparator.comparingInt(RegisteredListener::priority).reversed());
            sorted = true;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
