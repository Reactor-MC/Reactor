package ink.reactor.protocol.bridge.common.handler;

import ink.reactor.protocol.api.PlayerConnection;
import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;
import ink.reactor.protocol.api.packet.PacketHandler;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public final class SingleHandlerStorage implements MutablePacketHandlerStorage {
    private final PacketHandler handler;

    @Override
    public void callHandlers(final PlayerConnection connection, final ReaderBuffer readerBuffer) {
        handler.handle(connection, readerBuffer);
    }

    @Override
    public MutablePacketHandlerStorage add(final PacketHandler handler) {
        return new ArrayHandlerStorage(new PacketHandler[] {this.handler, handler});
    }

    @Override
    public MutablePacketHandlerStorage remove(final PacketHandler handler) {
        return this.handler.equals(handler) ? null : this;
    }

    @Override
    public MutablePacketHandlerStorage add(final PacketHandler... handlers) {
        final PacketHandler[] packetHandlers = Arrays.copyOf(handlers, handlers.length + 1);
        packetHandlers[handlers.length] = handler;

        return new ArrayHandlerStorage(packetHandlers);
    }

    @Override
    public MutablePacketHandlerStorage remove(final PacketHandler... handlers) {
        return handlers.length == 0 ? this : remove(handlers[0]);
    }
}
