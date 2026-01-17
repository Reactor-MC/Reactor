package ink.reactor.kernel.event.special

interface Cancellable {
    val isCancelled: Boolean
    fun setCancelled(state: Boolean)
}
