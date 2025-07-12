package ink.reactor.protocol.bridge.common;

import ink.reactor.protocol.api.Protocol;
import ink.reactor.protocol.api.ProtocolBridge;

import java.util.HashMap;
import java.util.Map;

public final class ReactorProtocol extends Protocol {

    private final ProtocolBridge commonBridge = new CommonProtocolBridge();
    private final Map<Integer, ProtocolBridge> bridgeMap = new HashMap<>();

    @Override
    public ProtocolBridge getCommonBridge() {
        return commonBridge;
    }

    @Override
    public ProtocolBridge getBridge(final int version) {
        return bridgeMap.get(version);
    }

    @Override
    public void registerBridge(final int version, final ProtocolBridge bridge) {
        this.bridgeMap.put(version, bridge);
    }
}
