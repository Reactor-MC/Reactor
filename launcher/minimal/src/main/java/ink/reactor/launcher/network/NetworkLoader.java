package ink.reactor.launcher.network;

import ink.reactor.bridge.protocol.v1_21.BridgeV1_21;
import ink.reactor.kernel.logger.Logger;
import ink.reactor.launcher.ReactorLauncher;
import ink.reactor.protocol.api.Protocol;
import ink.reactor.protocol.bridge.common.ReactorProtocol;
import ink.reactor.protocol.netty.NettyConfig;
import ink.reactor.protocol.netty.ServerConnection;
import ink.reactor.sdk.config.ConfigService;
import ink.reactor.sdk.config.section.ConfigSection;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public final class NetworkLoader {

    private final Logger logger;

    public boolean load(final ConfigService configService) {
        try {
            startConnection(configService.createIfAbsentAndLoad("network.yml", getClass().getClassLoader()));
            return true;
        } catch (final IOException e) {
            logger.error("Can't load logger.yml", e);
        } catch (final InterruptedException e) {
            logger.error("Can't start network", e);
        }
        return false;
    }

    public void startConnection(final ConfigSection section) throws InterruptedException {
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

        ReactorLauncher.STOP_TASKS.add(serverConnection::shutdown);

        serverConnection.connect(new NettyConfig(
            host,
            port,
            bossThreadCount,
            workerThreadCount,
            section.getBoolean("tcp-fast-open"),
            Math.max(1, section.getInt("tcp-fast-open-connections"))
        ));

        Protocol.set(new ReactorProtocol());
        BridgeV1_21.register();

        logger.info("Listening in: " + host + ":" + port);
    }
}
