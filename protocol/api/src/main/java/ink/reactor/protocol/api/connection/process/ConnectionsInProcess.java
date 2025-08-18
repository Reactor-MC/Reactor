package ink.reactor.protocol.api.connection.process;

import ink.reactor.kernel.event.EventBus;
import ink.reactor.protocol.api.connection.PlayerConnection;

import java.util.function.Predicate;

public interface ConnectionsInProcess {
    EventBus getEventBus();

    ConnectionProcess getConnection(final PlayerConnection connection);

    void addConnection(final PlayerConnection connection, final ConnectionProcess process);

    ConnectionProcess removeConnection(final PlayerConnection connection);

    boolean isInProcess(final String userName);
    boolean untilMatch(final Predicate<ConnectionProcess> process);
}
