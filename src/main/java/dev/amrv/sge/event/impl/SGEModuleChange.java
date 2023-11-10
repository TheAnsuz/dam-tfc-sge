package dev.amrv.sge.event.impl;

import dev.amrv.sge.event.Event;
import dev.amrv.sge.module.Module;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class SGEModuleChange extends Event {

    private final Module oldModule;
    private final Module newModule;

    public SGEModuleChange(Module oldModule, Module newModule) {
        this.oldModule = oldModule;
        this.newModule = newModule;
    }

    public Module getOldModule() {
        return oldModule;
    }

    public Module getNewModule() {
        return newModule;
    }

}
