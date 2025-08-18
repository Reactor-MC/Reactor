package ink.reactor.protocol.api;

import ink.reactor.protocol.api.connection.process.ConnectionsInProcess;

import java.util.Map;

public abstract class Protocol {
    private static Protocol instance;

    public abstract ProtocolBridge getCommonBridge();
    public abstract ProtocolBridge getBridge(final int version);
    public abstract Map<Integer, ProtocolBridge> getBridges();

    public abstract ConnectionsInProcess getConnectionsInProcess();

    public abstract void registerBridge(ProtocolBridge bridge, final int... versions);

    public static void set(final Protocol instance) {
        if (Protocol.instance != null) {
            throw new IllegalStateException("Protocol instance is already set");
        }
        Protocol.instance = instance;
    }

    public static Protocol get() {
        return instance;
    }
}
