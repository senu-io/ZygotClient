package com.zygot.event;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * High performance priority-based Event Bus for Zygot Client.
 */
public class EventBus {

    private final Map<Class<? extends Event>, List<Data>> registry = new HashMap<>();

    public void register(Object listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventTarget.class) && method.getParameterTypes().length == 1) {
                method.setAccessible(true);
                @SuppressWarnings("unchecked")
                Class<? extends Event> eventClass = (Class<? extends Event>) method.getParameterTypes()[0];
                EventTarget annotation = method.getAnnotation(EventTarget.class);
                Data data = new Data(listener, method, annotation.value());

                registry.computeIfAbsent(eventClass, k -> new CopyOnWriteArrayList<>()).add(data);
                registry.get(eventClass).sort(Comparator.comparingInt(d -> d.priority));
            }
        }
    }

    public void unregister(Object listener) {
        for (List<Data> list : registry.values()) {
            list.removeIf(data -> data.source == listener);
        }
    }

    public Event post(Event event) {
        List<Data> list = registry.get(event.getClass());
        if (list != null && !list.isEmpty()) {
            for (Data data : list) {
                try {
                    data.target.invoke(data.source, event);
                } catch (Exception e) {
                    System.err.println("[Zygot EventBus] Error dispatching event " + event.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        }
        return event;
    }

    private static class Data {
        final Object source;
        final Method target;
        final byte priority;

        Data(Object source, Method target, byte priority) {
            this.source = source;
            this.target = target;
            this.priority = priority;
        }
    }
}
