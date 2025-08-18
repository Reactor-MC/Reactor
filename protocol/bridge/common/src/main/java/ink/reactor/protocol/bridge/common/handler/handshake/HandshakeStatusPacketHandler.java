package ink.reactor.protocol.bridge.common.handler.handshake;

import ink.reactor.protocol.api.connection.PlayerConnection;
import ink.reactor.protocol.api.buffer.DataSize;
import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;
import ink.reactor.protocol.api.buffer.writer.ExpectedSizeBuffer;
import ink.reactor.protocol.api.packet.CachedPacket;
import ink.reactor.protocol.api.packet.PacketHandler;

public final class HandshakeStatusPacketHandler implements PacketHandler {

    @Override
    public void handle(final PlayerConnection connection, final ReaderBuffer buffer) {
        final ExpectedSizeBuffer data = new ExpectedSizeBuffer(DataSize.LONG);
        long payload = buffer.readLong();
        data.writeLong(payload);
        connection.sendPacket(new CachedPacket(0x01, data.compress()));
    }

    @Override
    public int packetId() {
        return 1;
    }
}
