package ink.reactor.microkernel.scheduler;

import ink.reactor.kernel.scheduler.Scheduler;
import ink.reactor.kernel.scheduler.Ticks;

public final class TickScheduler implements Scheduler {

    private final NowTasks nowTasks = new NowTasks();
    private final LaterTasks laterTasks = new LaterTasks();
    private final ScheduleTasks scheduleTasks = new ScheduleTasks();

    public void tick() {
        nowTasks.executeAll();
        laterTasks.executeAll();
        scheduleTasks.executeAll();
    }

    @Override
    public void runNow(final Runnable task) {
        nowTasks.addTask(task);
    }

    @Override
    public void runAtTick(final Runnable task, final Ticks tickToExecute) {
        if (tickToExecute.duration()-1 <= 0) {
            runNow(task);
            return;
        }
        laterTasks.addTask(task, tickToExecute.duration()-1);
    }

    @Override
    public int scheduleAtTick(final Runnable task, final Ticks tickToStart, final Ticks executeInTheTick) {
        final int tickToStartValue = Math.max(0, tickToStart.duration()-1);
        final int delayBetween = Math.max(0, executeInTheTick.duration()-1);
        return scheduleTasks.addTask(task, tickToStartValue, delayBetween);
    }

    @Override
    public void runAfterDelay(final Runnable task, final Ticks delay) {
        final int ticks = delay.duration();
        if (ticks <= 0) {
            nowTasks.addTask(task);
            return;
        }
        laterTasks.addTask(task, delay.duration());
    }

    @Override
    public int scheduleWithDelayBetween(final Runnable task, final Ticks delayFirstExecute, final Ticks delayBetweenExecute) {
        return scheduleTasks.addTask(task, delayFirstExecute.duration(), delayBetweenExecute.duration());
    }

    @Override
    public boolean cancelScheduleTask(final int taskId) {
        return scheduleTasks.removeTask(taskId);
    }

    @Override
    public Scheduler createNewScheduler() {
        return new TickScheduler();
    }
}