package ink.reactor.kernel.scheduler;

public interface Scheduler {
    /**
     * Run the task in this tick
     * @param task - Task to be executed by the main thread
     */
    void runNow(final Runnable task);

    /**
     * Run the task with delay
     * @param task - Task to be executed by the main thread
     * @param delay - How many ticks need wait to execute the start
     */
    void runLater(final Runnable task, final Ticks delay);

    /**
     * Run the task in this tick
     * @param task - Task to be executed by the main thread
     * @param startDelay - How many ticks need wait to start the task
     * @return taskId
     */
    int schedule(final Runnable task, final Ticks startDelay, final Ticks repeat);

    /**
     * Run the task in this tick
     * @param task - Task to be executed by the main thread
     * @return taskId
     */
    default int schedule(final Runnable task, final Ticks repeat) {
        return schedule(task, Ticks.none(), repeat);
    }

    /**
     * Cancel a running task
     * @param taskId - The task id (returned by schedule method)
     * @return if the task exists or no
     */
    boolean cancelTask(int taskId);
}
