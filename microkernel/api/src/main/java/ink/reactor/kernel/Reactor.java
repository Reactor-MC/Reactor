package ink.reactor.kernel;

import lombok.NonNull;

public abstract class Reactor {

    private static ReactorServer reactor;

    public static ReactorServer getServer() {
        return reactor;
    }

    public static void setServer(final @NonNull ReactorServer reactor) {
        if (Reactor.reactor != null) {
            throw new IllegalStateException("Kernel api is already set");
        }
        Reactor.reactor = reactor;
    }
}
