package ink.reactor.protocol.netty.outbound;

public interface PacketOutbound {
    byte[] write();
    int getId();
}
