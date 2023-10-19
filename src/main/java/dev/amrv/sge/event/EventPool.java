package dev.amrv.sge.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
class EventPool {

    private final Map<Class<? extends Event>, EventConsumer> mapping = new HashMap<>();
    private final List<Event> stack = new ArrayList<>();

    EventPool() {

    }

    Map<Class<? extends Event>, EventConsumer> getMapping() {
        return mapping;
    }

    List<Event> getStack() {
        return stack;
    }

    <E extends Event> void addListener(Class<E> event, SingleEventListener<E> listener) {
        addPrivateListener(listener, (Class<Event>) event);
    }

    private void addPrivateListener(SingleEventListener listener, Class<Event> event) {
        EventConsumer consumer = mapping.get(event);

        if (consumer == null) {
            consumer = new EventConsumer(event);
            mapping.put(event, consumer);
        }

        consumer.add(listener);
    }

    void addListener(EventListener listener) {

        for (Method m : listener.getClass().getMethods()) {
            Listen l = m.getAnnotation(Listen.class);

            if (l == null)
                continue;

            Class<? extends Event> eventClass = l.value();

            if (m.getParameterCount() != 1)
                continue;

            if (!m.getParameters()[0].getType().equals(eventClass))
                continue;

            addPrivateListener(new InnerEventListener(listener) {
                @Override
                public void consume(Event event) {
                    try {
                        m.invoke(listener, event);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        Logger.getLogger(EventPool.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }, (Class<Event>) eventClass);
        }
    }

    void queue(Event event) {
        synchronized (stack) {
            stack.add(event);
        }
    }

    void pop() {
        Event e;
        synchronized (stack) {
            if (stack.isEmpty())
                return;

            e = stack.remove(0);
        }
        EventConsumer consumer = mapping.get(e.getClass());

        if (consumer == null)
            return;

        consumer.consume(e);
    }
}
