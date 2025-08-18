package ink.reactor.protocol.bridge.common.handler.state;

import ink.reactor.protocol.api.connection.PlayerConnection;
import ink.reactor.protocol.api.buffer.reader.ReaderBuffer;
import ink.reactor.protocol.api.packet.PacketHandler;
import ink.reactor.protocol.api.packet.PacketHandlerStorage;
import ink.reactor.protocol.bridge.common.handler.MutablePacketHandlerStorage;
import ink.reactor.protocol.bridge.common.handler.SingleHandlerStorage;

public final class StatePacketHandlerStorage implements PacketHandlerStorage {

    private final MutablePacketHandlerStorage[] packetHandlerStorage;

    public StatePacketHandlerStorage(int size) {
        this.packetHandlerStorage = new MutablePacketHandlerStorage[size];
    }

    @Override
    public void callHandlers(final PlayerConnection connection, final ReaderBuffer buffer, final int packetID) {
        if (packetID < 0 || packetID >= packetHandlerStorage.length) {
            throw new ArrayIndexOutOfBoundsException("Packet id need be in the range 0-" + (packetHandlerStorage.length-1) + " but found " + packetID);
        }
        if (packetHandlerStorage[packetID] != null) {
            packetHandlerStorage[packetID].callHandlers(connection, buffer);
        }
    }

    @Override
    public void add(final PacketHandler handler) {
        final int packetID = handler.packetId();
        if (packetID < 0 || packetID >= packetHandlerStorage.length) {
            throw new ArrayIndexOutOfBoundsException("Packet id need be in the range 0-" + (packetHandlerStorage.length-1) + " but found " + handler.packetId());
        }

        final MutablePacketHandlerStorage mutablePacketHandlerStorages = packetHandlerStorage[packetID];
        packetHandlerStorage[packetID] = mutablePacketHandlerStorages == null
            ? new SingleHandlerStorage(handler)
            : mutablePacketHandlerStorages.add(handler);
    }

    @Override
    public void remove(final PacketHandler handler) {
        final int packetID = handler.packetId();
        if (packetID < 0 || packetID >= packetHandlerStorage.length) {
            throw new ArrayIndexOutOfBoundsException("Packet id need be in the range 0-" + (packetHandlerStorage.length-1) + " but found " + handler.packetId());
        }
        final MutablePacketHandlerStorage mutablePacketHandlerStorages = packetHandlerStorage[packetID];
        if (mutablePacketHandlerStorages != null) {
            packetHandlerStorage[packetID] = mutablePacketHandlerStorages.remove(handler);
        }
    }

    @Override
    public void add(final PacketHandler... handlers) {
        for (final PacketHandler packetHandler : handlers) {
            add(packetHandler);
        }
    }

    @Override
    public void remove(final PacketHandler... handlers) {
        for (final PacketHandler packetHandler : handlers) {
            remove(packetHandler);
        }
    }

    @Override
    public void clear(int id) {
        packetHandlerStorage[id] = null;
    }
}
