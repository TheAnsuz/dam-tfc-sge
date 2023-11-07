package dev.amrv.sge.window.component;

import dev.amrv.sge.module.Module;
import javax.swing.JToggleButton;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class ModuleButton extends JToggleButton {

    private final Module module;

    public ModuleButton(Module module) {
        super(module.getName());
        this.module = module;
    }

    public Module getModule() {
        return module;
    }

}
