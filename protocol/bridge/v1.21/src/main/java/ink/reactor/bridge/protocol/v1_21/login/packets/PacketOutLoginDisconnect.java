package ink.reactor.bridge.protocol.v1_21.login.packets;

import ink.reactor.protocol.api.buffer.writer.SerializedBuffer;
import ink.reactor.protocol.api.packet.PacketOutbound;

public record PacketOutLoginDisconnect(String reason) implements PacketOutbound {
    @Override
    public byte[] write() {
        return SerializedBuffer.from(reason);
    }

    @Override
    public int getId() {
        return 0;
    }
}
