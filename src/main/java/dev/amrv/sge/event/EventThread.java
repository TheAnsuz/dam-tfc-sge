package dev.amrv.sge.event;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class EventThread extends Thread {

    private boolean running = false;
    private final EventPool pool;

    EventThread(EventPool pool, ThreadGroup group, String name) {
        super(group, name);
        this.pool = pool;
        super.setDaemon(true);
    }

    @Override
    public void start() {
        if (running)
            return;

        running = true;
        super.start();
    }

    public void end() throws InterruptedException {
        if (!running)
            return;
        
        running = false;
        super.join();
    }

    @Override
    public void run() {
        while (running) {
            pool.pop();
        }
    }

}
