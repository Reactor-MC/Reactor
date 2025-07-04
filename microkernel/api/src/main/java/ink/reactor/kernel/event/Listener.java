package ink.reactor.kernel.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {
    /*
     * If the event is canceled, ignore this fact and run the listener anyway
     */
    boolean ignoreCancelled() default false;

    /*
     * The lower the priority, the sooner it will be executed
     */
    EventPriority priority() default EventPriority.NORMAL;
}
