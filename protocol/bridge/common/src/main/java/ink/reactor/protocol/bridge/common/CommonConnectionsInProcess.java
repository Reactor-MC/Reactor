package ink.reactor.protocol.bridge.common;

import ink.reactor.kernel.Reactor;
import ink.reactor.kernel.event.EventBus;
import ink.reactor.protocol.api.connection.process.ConnectionProcess;
import ink.reactor.protocol.api.connection.process.ConnectionsInProcess;
import ink.reactor.protocol.api.connection.PlayerConnection;
import ink.reactor.protocol.api.connection.process.LoginProcess;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public final class CommonConnectionsInProcess implements ConnectionsInProcess {

    private final Map<PlayerConnection, ConnectionProcess> connectionsToProcess = new ConcurrentHashMap<>();

    @Override
    public EventBus getEventBus() {
        return Reactor.get().globalBus();
    }

    @Override
    public ConnectionProcess getConnection(final PlayerConnection connection) {
        return connectionsToProcess.get(connection);
    }

    @Override
    public void addConnection(final PlayerConnection connection, final ConnectionProcess process) {
        connectionsToProcess.put(connection, process);
    }

    @Override
    public ConnectionProcess removeConnection(final PlayerConnection connection) {
        return connectionsToProcess.remove(connection);
    }

    @Override
    public boolean isInProcess(final String userName) {
        return untilMatch((process -> process instanceof LoginProcess loginProcess && loginProcess.getName().equals(userName)));
    }

    @Override
    public boolean untilMatch(final Predicate<ConnectionProcess> processPredicate) {
        if (connectionsToProcess.isEmpty()) {
            return false;
        }
        for (final ConnectionProcess process : connectionsToProcess.values()) {
            if (processPredicate.test(process)) {
                return false;
            }
        }
        return false;
    }
}
