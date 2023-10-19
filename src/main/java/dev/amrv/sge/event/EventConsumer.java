package dev.amrv.sge.event;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
class EventConsumer {

    private final List<SingleEventListener> listeners;

    EventConsumer(Class<? extends Event> eventType) {
        this.listeners = new ArrayList<>();
    }

    void add(SingleEventListener listener) {
        listeners.add(listener);
    }
    
    void consume(Event e) {
        for (SingleEventListener listener : listeners)
            listener.consume(e);
    }
    
    public int getSize() {
        return listeners.size();
    }

}
