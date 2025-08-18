package ink.reactor.protocol.api.packet;

public record CachedPacket(int packetId, byte[] bytes) implements PacketOutbound {
    @Override
    public byte[] write() {
        return bytes;
    }

    @Override
    public int getId() {
        return packetId;
    }
}
