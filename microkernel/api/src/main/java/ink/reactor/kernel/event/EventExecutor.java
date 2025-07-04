package ink.reactor.kernel.event;

public interface EventExecutor {
    void execute(final Object listener, final Object event);
}