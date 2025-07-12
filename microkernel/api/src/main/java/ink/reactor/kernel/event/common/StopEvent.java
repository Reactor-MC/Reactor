package ink.reactor.kernel.event.common;

public record StopEvent(
    long start,
    long timeAlive
) {}
