package dev.amrv.sge.event;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class EventTestSub extends Event {

    public final String message;
    public final String cc;

    public EventTestSub(String message, String cc) {
        this.message = message;
        this.cc = cc;
    }
}
