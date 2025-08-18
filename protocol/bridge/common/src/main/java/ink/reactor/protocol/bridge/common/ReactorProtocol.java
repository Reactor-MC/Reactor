package ink.reactor.protocol.bridge.common;

import ink.reactor.protocol.api.Protocol;
import ink.reactor.protocol.api.ProtocolBridge;
import ink.reactor.protocol.api.connection.process.ConnectionsInProcess;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class ReactorProtocol extends Protocol {

    private final ProtocolBridge commonBridge = new HandshakeProtocolBridge();
    private final Map<Integer, ProtocolBridge> bridgeMap = new HashMap<>();

    private final ConnectionsInProcess connectionsInProcess = new CommonConnectionsInProcess();

    @Override
    public ProtocolBridge getBridge(final int version) {
        return bridgeMap.get(version);
    }

    @Override
    public void registerBridge(final ProtocolBridge bridge, final int... versions) {
        for (final int version : versions) {
            this.bridgeMap.put(version, bridge);
        }
    }

    @Override
    public Map<Integer, ProtocolBridge> getBridges() {
        return new HashMap<>(bridgeMap);
    }
}
