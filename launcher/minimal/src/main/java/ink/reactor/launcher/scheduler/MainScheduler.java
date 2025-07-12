package ink.reactor.launcher.scheduler;

import ink.reactor.microkernel.scheduler.TickScheduler;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
public final class MainScheduler {

    private final AtomicBoolean running = new AtomicBoolean();

    private final TickScheduler tickScheduler;
    private ScheduledExecutorService executorService;

    public void start(final TimeUnit unit, final long amount) {
        executorService = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());
        executorService.schedule(tickScheduler::tick, amount, unit);
    }

    public void stop() {
        executorService.shutdown();
    }

    public boolean isRunning() {
        return running.get();
    }
}
