package ink.reactor.kernel.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {
    /**
     * @return if you can run the listener, ignoring whether the event is canceled or not
     */
    boolean ignoreCancelled() default false;

    /**
     * The lower the priority, the sooner it will be executed
     */
    ListenerPriority priority() default ListenerPriority.DEFAULT;
}
