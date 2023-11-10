package dev.amrv.sge.module;

import dev.amrv.sge.SGE;
import javax.swing.JPanel;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public abstract class Module {

    private boolean loaded = false;

    public boolean isLoaded() {
        return loaded;
    }

    public final boolean load(SGE sge) {
        if (onLoad(sge)) {
            loaded = true;
            return true;
        }
        return false;
    }

    protected abstract boolean onLoad(SGE sge);

    public abstract void unload();

    public abstract JPanel getPanel();

    public abstract String getName();

    public abstract String requirePermission();

    /**
     * Called when the user clicks to see the module
     */
    public abstract void onAppear();

}
