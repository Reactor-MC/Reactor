package ink.reactor.kernel.event;

public interface EventBus {
    void register(final Object listener);
    void unregister(final Object listener);

    void post(final Object event);

    void clear();
}