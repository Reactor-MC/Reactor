package ink.reactor.kernel.scheduler;

import java.time.Duration;

public record Ticks(int duration) {

    private static final Ticks NONE = new Ticks(0);

    public static Ticks none() {
        return NONE;
    }

    public static Ticks ofMillis(final int millis) {
        return new Ticks(TickUnit.MILLIS.toTicks(millis));
    }

    public static Ticks ofSeconds(final int seconds) {
        return new Ticks(TickUnit.SECONDS.toTicks(seconds));
    }

    public static Ticks ofMinutes(final int minutes) {
        return new Ticks(TickUnit.MINUTES.toTicks(minutes));
    }

    public static Ticks ofHours(final int hours) {
        return new Ticks(TickUnit.HOURS.toTicks(hours));
    }

    public static Ticks ofDays(final int days) {
        return new Ticks(TickUnit.DAYS.toTicks(days));
    }

    public static Ticks from(final Duration duration) {
        return new Ticks((int) (duration.toMillis() / TickUnit.MILLIS_PER_TICK));
    }
}