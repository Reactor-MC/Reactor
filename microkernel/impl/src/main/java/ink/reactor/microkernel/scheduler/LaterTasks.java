package ink.reactor.microkernel.scheduler;

import lombok.AllArgsConstructor;

import java.util.Arrays;

public final class LaterTasks {

    private static final int DEFAULT_ARRAY_SIZE = 16;

    private LaterTask[] tasks;
    private int pos = 0;
    private int waitingTasks = 0;

    public LaterTasks() {
        this.tasks = new LaterTask[DEFAULT_ARRAY_SIZE];
    }

    public void addTask(final Runnable task, final int delay) {
        waitingTasks++;
        if (pos+1 == tasks.length) {
            tasks = Arrays.copyOf(tasks, tasks.length + DEFAULT_ARRAY_SIZE);
        }
        tasks[pos++] = new LaterTask(task, delay);
    }
    public void executeAll() {
        if (waitingTasks == 0) {
            return;
        }

        boolean needCompact = false;
        for (int i = 0; i < pos; i++) {
            final LaterTask task = tasks[i];
            if (task == null) {
                continue;
            }

            if (task.delay == 0) {
                task.runnable.run();
                tasks[i] = null;
                needCompact = true;
                waitingTasks--;
            } else {
                task.delay--;
            }
        }
        if (needCompact) {
            compact();
        }
    }

    private void compact() {
        if (waitingTasks == 0) {
            pos = 0;
            if (this.tasks.length != DEFAULT_ARRAY_SIZE) {
                this.tasks = new LaterTask[DEFAULT_ARRAY_SIZE];
            }
            return;
        }

        if (waitingTasks*2 < tasks.length) { // Prevent keep huge empty array
            tasks = Arrays.copyOf(tasks, waitingTasks);
            pos = waitingTasks;
            return;
        }

        int index = 0;
        for (int i = 0; i < pos; i++) {
            if (tasks[i] != null) {
                tasks[index++] = tasks[i];
            }
        }
        pos = index;
    }

    @AllArgsConstructor
    private static final class LaterTask {
        final Runnable runnable;
        int delay;
    }
}