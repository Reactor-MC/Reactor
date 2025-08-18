package ink.reactor.protocol.api.connection.process;

import ink.reactor.protocol.api.connection.ConnectionState;

public sealed interface ConnectionProcess permits HandshakeProcess, LoginProcess {
    ConnectionState getConnectionState();
}
