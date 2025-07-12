package ink.reactor.protocol.api;

import lombok.Getter;

public abstract class Protocol {
    @Getter
    private static Protocol instance;

    public abstract ProtocolBridge getCommonBridge();
    public abstract ProtocolBridge getBridge(final int version);
    public abstract void registerBridge(final int version, ProtocolBridge bridge);

    public static void setInstance(final Protocol instance) {
        if (Protocol.instance != null) {
            throw new IllegalStateException("Protocol instance is already set");
        }
        Protocol.instance = instance;
    }
}
