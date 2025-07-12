package ink.reactor.protocol.netty;

public record NettyConfig(
    String ip,
    int port,

    int bossThreadCount,
    int workerThreadCount,

    boolean tcpFastOpen,
    int tcpFastOpenConnections
) {}
