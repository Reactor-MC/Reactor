package ink.reactor.kernel.event

interface EventExecutor {
    fun execute(event: Any)
}
