package ink.reactor.microkernel.scheduler;

import java.util.Arrays;

public final class NowTasks {
    private static final int DEFAULT_SIZE = 16;

    private Runnable[] tasks = new Runnable[DEFAULT_SIZE];
    private int pos = 0;

    public void addTask(Runnable task) {
        if (pos+1 == tasks.length) {
            tasks = Arrays.copyOf(tasks, tasks.length * 2);
        }
        tasks[pos++] = task;
    }

    public void executeAll() {
        if (pos == 0) {
            return;
        }

        if (pos == 1) { // Common case
            pos = 0;
            tasks[0].run();
            tasks[0] = null;
            return;
        }

        final Runnable[] nowTasks = tasks;
        tasks = new Runnable[DEFAULT_SIZE];
        pos = 0;

        for (final Runnable task : nowTasks) {
            if (task != null) {
                task.run();
            }
        }
    }
}