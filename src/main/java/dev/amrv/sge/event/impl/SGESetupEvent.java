package dev.amrv.sge.event.impl;

import dev.amrv.sge.SGE;
import dev.amrv.sge.event.Event;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class SGESetupEvent extends Event {

    private final SGE instance;

    public SGESetupEvent(SGE sge) {
        this.instance = sge;
    }

    public SGE getInstance() {
        return instance;
    }

}
