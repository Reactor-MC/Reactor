package ink.reactor.protocol.bridge.common.handler;

import ink.reactor.protocol.api.PlayerConnection;
import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;
import ink.reactor.protocol.api.packet.PacketHandler;

import java.util.Arrays;

public final class ArrayHandlerStorage implements MutablePacketHandlerStorage {
    private PacketHandler[] handlers;

    public ArrayHandlerStorage(PacketHandler[] handlers) {
        this.handlers = handlers;
    }

    @Override
    public void callHandlers(final PlayerConnection connection, final ReaderBuffer readerBuffer) {
        for (final PacketHandler handler : handlers) {
            handler.handle(connection, readerBuffer);
        }
    }

    @Override
    public MutablePacketHandlerStorage add(final PacketHandler handler) {
        handlers = Arrays.copyOf(handlers, handlers.length+1);
        handlers[handlers.length - 1] = handler;
        return this;
    }

    @Override
    public MutablePacketHandlerStorage remove(final PacketHandler handler) {
        int index = -1;
        for (int i = 0; i < handlers.length; i++) {
            if (handler.equals(handlers[i])) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return this;
        }
        if (handlers.length == 2) {
            return new SingleHandlerStorage(handlers[index == 0 ? 1 : 0]);
        }
        if (handlers.length == 1) {
            return null;
        }

        final PacketHandler[] newHandlers = new PacketHandler[handlers.length - 1];

        System.arraycopy(handlers, 0, newHandlers, 0, index);
        System.arraycopy(handlers, index + 1, newHandlers, index, handlers.length - index - 1);

        this.handlers = newHandlers;
        return this;
    }

    @Override
    public MutablePacketHandlerStorage add(final PacketHandler... handlers) {
        final PacketHandler[] newHandlers = Arrays.copyOf(this.handlers, this.handlers.length + handlers.length);
        System.arraycopy(handlers, 0, newHandlers, this.handlers.length, handlers.length);
        this.handlers = newHandlers;
        return this;
    }

    @Override
    public MutablePacketHandlerStorage remove(final PacketHandler... handlers) {
        MutablePacketHandlerStorage storage = this;
        for (final PacketHandler handler : handlers) {
            storage = remove(handler);
        }
        return storage;
    }
}
