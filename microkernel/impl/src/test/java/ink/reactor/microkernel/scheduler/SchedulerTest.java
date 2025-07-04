package ink.reactor.microkernel.scheduler;

import ink.reactor.kernel.scheduler.Ticks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class SchedulerTest {

    @Test
    public void testNowTasks() {
        final TickScheduler tickScheduler = new TickScheduler();
        final AtomicInteger executed = new AtomicInteger();

        tickScheduler.runNow(executed::incrementAndGet);
        tickScheduler.runNow(executed::incrementAndGet);
        tickScheduler.runNow(executed::incrementAndGet);

        tickScheduler.tick();

        Assertions.assertEquals(3, executed.get());
    }

    @Test
    public void testLaterTasks() {
        final TickScheduler tickScheduler = new TickScheduler();

        final AtomicInteger ticksElapsed = new AtomicInteger();

        final AtomicBoolean task1 = new AtomicBoolean();
        final AtomicBoolean task2 = new AtomicBoolean();
        final AtomicBoolean task3 = new AtomicBoolean();

        tickScheduler.runAfterDelay(() -> task1.set(true), new Ticks(4));
        tickScheduler.runAfterDelay(() -> task2.set(true), new Ticks(2));

        tickScheduler.runAtTick(() -> task3.set(true), new Ticks(3));

        for (int i = 0; i < 4; i++) {
            tickScheduler.tick();
            ticksElapsed.incrementAndGet();

            Assertions.assertFalse(task1.get() && ticksElapsed.get() <= 4, "Task1 execute in the tick " + ticksElapsed.get() + " But expected execute in the tick 4");
            Assertions.assertFalse(task2.get() && ticksElapsed.get() <= 2,  "Task2 execute in the tick " + ticksElapsed.get() + " But expected execute in the tick 3 (2 delay + 1 real)");

            Assertions.assertFalse(task3.get() && ticksElapsed.get() < 3,  "Task3 execute in the tick " + ticksElapsed.get() + " But expected execute in the tick 3 (exact)");
        }
    }

    @Test
    public void testScheduleAfterTasks() {
        final TickScheduler tickScheduler = new TickScheduler();
        final AtomicInteger task1 = new AtomicInteger();
        final AtomicInteger task2 = new AtomicInteger();

        final int firstTaskId = tickScheduler.scheduleWithDelayBetween(
            task1::incrementAndGet,
            new Ticks(2),
            new Ticks(3)
        );

        final int secondTaskId = tickScheduler.scheduleAtTick(
            task2::incrementAndGet,
            new Ticks(1),
            new Ticks(3)
        );

        // Tick 1 (before starting)
        tickScheduler.tick();
        Assertions.assertEquals(0, task1.get(), "Task1: Should not execute on first tick");
        Assertions.assertEquals(1, task2.get(), "Task2: Should not execute yet");

        // Tick 2
        tickScheduler.tick();
        Assertions.assertEquals(0, task1.get(), "Task1: Should not execute yet");

        // Tick 3 (first execution)
        tickScheduler.tick();
        Assertions.assertEquals(1, task1.get(), "Task1: First execution on tick 2");

        // Tick 4
        tickScheduler.tick();
        Assertions.assertEquals(1, task1.get(), "Task1: Should not repeat yet");
        Assertions.assertEquals(2, task2.get(), "Task2: Should not repeat yet");

        // Tick 5
        tickScheduler.tick();
        Assertions.assertEquals(1, task1.get(), "Task1: Should not repeat yet");

        // Tick 6
        tickScheduler.tick();
        Assertions.assertEquals(1, task1.get(), "Task1: Should not repeat yet");

        // Tick 7 (second execution - 3 ticks after first execution)
        tickScheduler.tick();
        Assertions.assertEquals(2, task1.get(), "Task1: Second execution on tick 7");
        Assertions.assertEquals(3, task2.get(), "Task2: Three execution on tick 7");

        // Tick 8
        tickScheduler.tick();
        Assertions.assertEquals(2, task1.get(), "Task1: Should not repeat yet");

        // Cancel the task
        tickScheduler.cancelScheduleTask(firstTaskId);
        tickScheduler.cancelScheduleTask(secondTaskId);

        // Tick 9 (should not execute after cancellation)
        for (int i = 0; i < 10; i++) {
            tickScheduler.tick();
        }
        Assertions.assertEquals(2, task1.get(), "Task1: Should not execute after cancellation");
        Assertions.assertEquals(3, task2.get(), "Task2: Should not execute after cancellation");
    }
}
