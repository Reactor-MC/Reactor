package ink.reactor.microkernel.event.simplebus;

import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;

@Getter
final class ListenerStorage {
    private RegisteredListener[] listeners;
    private int size;
    private boolean sorted;

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
        sorted = false;
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