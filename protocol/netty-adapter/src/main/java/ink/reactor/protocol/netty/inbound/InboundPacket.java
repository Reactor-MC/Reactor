package ink.reactor.protocol.netty.inbound;

public record InboundPacket (
    int id,
    NettyReadBuffer data
) {}
