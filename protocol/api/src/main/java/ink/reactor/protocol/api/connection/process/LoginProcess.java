package ink.reactor.protocol.api.connection.process;

import ink.reactor.kernel.event.special.Cancellable;
import ink.reactor.protocol.api.connection.ConnectionState;
import ink.reactor.protocol.api.connection.PlayerConnection;
import ink.reactor.protocol.api.player.GameProfile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public final class LoginProcess implements Cancellable, ConnectionProcess {
    private final PlayerConnection connection;
    private final String name;

    private UUID uuid;

    private boolean cancelled;
    private String kickMessage;

    private Stage currentStage = Stage.START;
    private Stage nextStage = Stage.SUCCESS;

    private Collection<GameProfile.Property> properties = Collections.emptyList();

    @Override
    public ConnectionState getConnectionState() {
        return ConnectionState.LOGIN;
    }

    public enum Stage {
        START,
        ENCRYPTION,
        COMPRESSION,
        SUCCESS
    }
}
