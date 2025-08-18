package ink.reactor.protocol.api.packet;

public interface PacketOutbound {
    byte[] write();
    int getId();
}
