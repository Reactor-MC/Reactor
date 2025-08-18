package ink.reactor.microkernel.event.loader;

import ink.reactor.kernel.event.EventExecutor;
import ink.reactor.kernel.event.Listener;
import ink.reactor.kernel.event.ListenerPhase;
import ink.reactor.kernel.logger.Logger;
import ink.reactor.microkernel.event.executor.ListenerMethodHandleExecutor;
import lombok.RequiredArgsConstructor;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public final class MethodListenerLoader {

    private final Logger logger;

    public List<MethodListener> load(final Object object) {
        final Class<?> sourceClass = object.getClass();
        final Method[] methods = sourceClass.getDeclaredMethods();
        if (methods.length == 0) {
            logger.info("The class %s don't contains any method", sourceClass);
            return List.of();
        }

        final List<MethodListener> listeners = new ArrayList<>(methods.length);

        for (final Method method : methods) {
            final Listener listener = method.getAnnotation(Listener.class);
            if (listener == null) {
                continue;
            }

            if (method.getParameterCount() != 1) {
                logger.warn("Error trying to load the listener %s in the class %s. The method need be exactly 1 parameter", method.getName(), sourceClass);
                continue;
            }

            final Class<?> firstParameter = method.getParameterTypes()[0];
            final MethodHandle methodHandle;
            try {
                methodHandle = MethodHandles.publicLookup().unreflect(method);
            } catch (final IllegalAccessException e) {
                logger.error("Error trying to load the listener %s in the class %s", e, method.getName(), sourceClass);
                continue;
            }

            listeners.add(new MethodListener(
                firstParameter,
                listener.phase(),
                listener.priority(),
                new ListenerMethodHandleExecutor(logger, object, listener.ignoreCancelled(), methodHandle)));
        }

        return listeners;
    }

    public record MethodListener(
        Class<?> eventClass,
        ListenerPhase phase,
        int priority,
        EventExecutor executor
    ){}
}
