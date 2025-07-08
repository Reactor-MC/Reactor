package ink.reactor.protocol.api;

public record Packet(
    int id,
    byte[] data
) {}
