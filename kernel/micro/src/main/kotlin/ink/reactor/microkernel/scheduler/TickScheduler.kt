package ink.reactor.microkernel.scheduler

import ink.reactor.kernel.scheduler.Scheduler
import ink.reactor.kernel.scheduler.Ticks
import kotlin.math.max

class TickScheduler : Scheduler {

    private val nowTasks = NowTasks()
    private val laterTasks = LaterTasks()
    private val scheduleTasks = ScheduleTasks()

    fun tick() {
        nowTasks.executeAll()
        laterTasks.executeAll()
        scheduleTasks.executeAll()
    }

    override fun runNow(task: Runnable) {
        nowTasks.addTask(task)
    }

    override fun runAtTick(task: Runnable, tickToExecute: Ticks) {
        if (tickToExecute.duration - 1 <= 0) {
            runNow(task)
            return
        }
        laterTasks.addTask(task, tickToExecute.duration - 1)
    }

    override fun scheduleAtTick(
        task: Runnable,
        tickToStart: Ticks,
        executeInTheTick: Ticks
    ): Int {
        val tickToStartValue = max(0, tickToStart.duration - 1)
        val delayBetween = max(0, executeInTheTick.duration - 1)
        return scheduleTasks.addTask(task, tickToStartValue, delayBetween)
    }

    override fun runAfterDelay(task: Runnable, delay: Ticks) {
        val ticks = delay.duration
        if (ticks <= 0) {
            nowTasks.addTask(task)
            return
        }
        laterTasks.addTask(task, delay.duration)
    }

    override fun scheduleWithDelayBetween(
        task: Runnable,
        delayFirstExecute: Ticks,
        delayBetweenExecute: Ticks
    ): Int {
        return scheduleTasks.addTask(task, delayFirstExecute.duration, delayBetweenExecute.duration)
    }

    override fun cancelScheduleTask(taskId: Int): Boolean {
        return scheduleTasks.removeTask(taskId)
    }

    override fun createNewScheduler(): Scheduler {
        return TickScheduler()
    }
}
