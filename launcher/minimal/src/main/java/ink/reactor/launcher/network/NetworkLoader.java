package ink.reactor.launcher.network;

import ink.reactor.bridge.protocol.v1_21.BridgeV1_21;
import ink.reactor.kernel.event.EventBus;
import ink.reactor.kernel.event.common.StopEvent;
import ink.reactor.kernel.logger.Logger;
import ink.reactor.protocol.api.Protocol;
import ink.reactor.protocol.bridge.common.ReactorProtocol;
import ink.reactor.protocol.netty.NettyConfig;
import ink.reactor.protocol.netty.ServerConnection;
import ink.reactor.sdk.config.ConfigSection;
import ink.reactor.sdk.config.ConfigService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public final class NetworkLoader {

    private final Logger logger;
    private final EventBus eventBus;

    public void load(final ConfigService configService) {
        try {
            startConnection(configService.createIfAbsentAndLoad("network.yml", getClass().getClassLoader()));
        } catch (final IOException e) {
            logger.error("Can't load logger.yml", e);
        }
    }

    public void startConnection(final ConfigSection section) {
        if (section == null) {
            logger.warn("Can't found network section in network.yml");
            return;
        }
        final String host = section.getOrDefault("host", "0.0.0.0");
        int port = section.getInt("port");
        if (port < 1024 || port > 49151) {
            logger.warn("The port range need be 1024->49151. See iana registered ports. Set port in 25565...");
            port = 25565;
        }

        final ServerConnection serverConnection = new ServerConnection(logger);

        int bossThreadCount = section.getInt("boss-thread-count");
        int workerThreadCount = section.getInt("worker-thread-count");
        if (bossThreadCount < 0) {
            bossThreadCount = Runtime.getRuntime().availableProcessors();
        }
        if (workerThreadCount < 0) {
            workerThreadCount = Runtime.getRuntime().availableProcessors();
        }

        serverConnection.connect(new NettyConfig(
            host,
            port,
            bossThreadCount,
            workerThreadCount,
            section.getBoolean("tcp-fast-open"),
            Math.max(1, section.getInt("tcp-fast-open-connections"))
        ));

        eventBus.register(StopEvent.class, (_) -> serverConnection.shutdown());

        Protocol.setInstance(new ReactorProtocol());
        BridgeV1_21.register();

        logger.info("Listening in: " + host + ":" + port);
    }
}
