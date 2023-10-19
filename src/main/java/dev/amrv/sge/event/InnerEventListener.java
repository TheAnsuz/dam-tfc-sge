package dev.amrv.sge.event;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
abstract class InnerEventListener implements SingleEventListener {
    
    protected final EventListener source;

    public InnerEventListener(EventListener source) {
        this.source = source;
    }
    
    public EventListener getSource() {
        return source;
    }
}
