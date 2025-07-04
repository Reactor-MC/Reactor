package ink.reactor.kernel.event;

public interface EventExecutor {
    void handle(final Object event);
}