package dev.amrv.sge.event;

import dev.amrv.sge.SGE;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class EventSystem {

    private final EventThread[] threads;
    private final EventPool pool;
    private final SGE sge;

    public EventSystem(SGE sge, int dedicatedThreads) {
        this.sge = sge;
        this.threads = new EventThread[dedicatedThreads];
        this.pool = new EventPool();

        ThreadGroup group = new ThreadGroup("EventSystem");

        for (int i = 0; i < dedicatedThreads; i++) {
            threads[i] = new EventThread(pool, group, "runner_" + i);
        }
    }

    public int getListeners(Class<? extends Event> eventType) {
        EventConsumer consumer = pool.getMapping().get(eventType);

        if (consumer == null)
            return 0;

        return consumer.getSize();
    }

    public int getQueueSize() {
        return pool.getStack().size();
    }

    public void start() {
        for (EventThread thread : threads) {
            thread.start();
        }
    }

    public void clearQueue() {
        pool.getStack().clear();
    }
    
    public void stop() {
        for (EventThread thread : threads) {
            try {
                thread.end();
            } catch (InterruptedException ex) {
                sge.logger.error(ex.toString());
            }
        }
    }

    public void addListener(EventListener listener) {
        pool.addListener(listener);
    }

    public <E extends Event> void addListener(Class<E> clazz, SingleEventListener<E> consumer) {
        pool.addListener(clazz, consumer);
    }

    public void queueEvent(Event event) {
        pool.queue(event);
    }
}
