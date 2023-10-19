/*
 */

package dev.amrv.sge.event;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public interface SingleEventListener<E extends Event> {

    void consume(E event);
}
