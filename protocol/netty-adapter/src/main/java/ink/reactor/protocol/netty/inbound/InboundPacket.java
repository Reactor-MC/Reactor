package ink.reactor.protocol.netty.inbound;

public record InboundPacket (
    int id,
    PacketInData data
) {}
