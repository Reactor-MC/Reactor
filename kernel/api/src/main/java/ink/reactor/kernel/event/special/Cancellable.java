package ink.reactor.kernel.event.special;

public interface Cancellable {
    boolean isCancelled();
    void setCancelled(final boolean state);
}
