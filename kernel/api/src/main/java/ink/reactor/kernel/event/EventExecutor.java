package ink.reactor.kernel.event;

public interface EventExecutor {
    void execute(final Object event);
}