package ink.reactor.microkernel.scheduler

internal class ScheduleTasks {
    private var runnables = arrayOfNulls<Runnable>(16)
    private var ids = IntArray(16)
    private var delays = IntArray(16)
    private var countdowns = IntArray(16)

    private var taskIdCount = 0
    private var size = 0

    fun executeAll() {
        if (size == 0) {
            return
        }
        for (i in 0..size) {
            val runnable = runnables[i] ?: continue
            if (countdowns[i] > 0) {
                countdowns[i]--
                continue
            }
            countdowns[i] = delays[i]
            runnable.run()
        }
    }

    fun addTask(runnable: Runnable, startDelay: Int, delay: Int): Int {
        ensureCapacity()
        val id = ++taskIdCount
        runnables[size] = runnable
        ids[size] = id
        delays[size] = delay
        countdowns[size] = startDelay
        size++
        return id
    }

    fun removeTask(id: Int): Boolean {
        for (i in 0..size) {
            if (ids[i] == id) {
                runnables[i] = null
                compact(i)
                return true
            }
        }
        return false
    }

    private fun compact(fromIndex: Int) {
        for (i in fromIndex until size - 1) {
            runnables[i] = runnables[i + 1]
            ids[i] = ids[i + 1]
            delays[i] = delays[i + 1]
            countdowns[i] = countdowns[i + 1]
        }
        size--
        runnables[size] = null
    }

    private fun ensureCapacity() {
        if (size >= runnables.size) {
            val newSize = runnables.size * 2
            runnables = runnables.copyOf(newSize)
            ids = ids.copyOf(newSize)
            delays = delays.copyOf(newSize)
            countdowns = countdowns.copyOf(newSize)
        }
    }
}
