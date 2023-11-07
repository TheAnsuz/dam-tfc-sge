package dev.amrv.sge.module;

import dev.amrv.sge.SGE;
import javax.swing.JPanel;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public abstract class Module {

    public abstract boolean load(SGE sge);

    public abstract void unload();

    public abstract JPanel getPanel();

    public abstract String getName();
    
    public abstract String requirePermission();

}
