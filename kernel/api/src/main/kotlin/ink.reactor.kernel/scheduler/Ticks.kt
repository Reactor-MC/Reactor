package ink.reactor.kernel.scheduler

import java.time.Duration

@JvmInline
value class Ticks(val duration: Int) : Comparable<Ticks> {

    companion object {
        val NONE = Ticks(0)
        const val MILLIS_PER_TICK = 50L

        fun ofMillis(millis: Number) = Ticks((millis.toLong() / MILLIS_PER_TICK).toInt())
        fun ofSeconds(seconds: Number) = ofMillis(seconds.toLong() * 1000)
        fun ofMinutes(minutes: Number) = ofSeconds(minutes.toLong() * 60)
        fun ofHours(hours: Number) = ofMinutes(hours.toLong() * 60)
        fun ofDays(days: Number) = ofHours(days.toLong() * 24)
        fun ofYears(years: Number) = ofDays(years.toLong() * 365)

        fun from(duration: Duration) = ofMillis(duration.toMillis())
    }

    operator fun plus(other: Ticks) = Ticks(duration + other.duration)
    operator fun minus(other: Ticks) = Ticks(duration - other.duration)
    override fun compareTo(other: Ticks) = duration.compareTo(other.duration)
}
