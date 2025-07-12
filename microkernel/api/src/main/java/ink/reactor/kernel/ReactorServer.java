package ink.reactor.kernel;

import ink.reactor.kernel.event.EventBus;
import ink.reactor.kernel.logger.Logger;
import ink.reactor.kernel.logger.LoggerFactory;
import ink.reactor.kernel.scheduler.Scheduler;

public record ReactorServer(
    Logger logger,
    LoggerFactory loggerFactory,
    Scheduler scheduler,
    EventBus globalBus
) {}
