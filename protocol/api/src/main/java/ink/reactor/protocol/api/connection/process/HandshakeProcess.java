package ink.reactor.protocol.api.connection.process;

import ink.reactor.kernel.event.special.Cancellable;
import ink.reactor.protocol.api.connection.ConnectionState;
import ink.reactor.protocol.api.packet.PacketOutbound;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public final class HandshakeProcess implements Cancellable, ConnectionProcess {
    private final int protocolVersion;
    private final String address;
    private final int port;
    private final int intent;

    private PacketOutbound statusResponse;

    private boolean cancelled;

    @Override
    public ConnectionState getConnectionState() {
        return ConnectionState.HANDSHAKE;
    }
}
