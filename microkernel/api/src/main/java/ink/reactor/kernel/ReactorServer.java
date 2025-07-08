package ink.reactor.kernel;

import ink.reactor.kernel.logger.Logger;
import ink.reactor.kernel.logger.LoggerFactory;

public interface ReactorServer {
    Logger getLogger();
    LoggerFactory getLoggerFactory();
}
