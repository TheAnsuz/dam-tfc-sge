package dev.amrv.sge.module;

import dev.amrv.sge.SGE;
import dev.amrv.sge.module.users.UsersPanel;
import javax.swing.JPanel;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class UsersModule extends Module {

    public static final String NAME = "Usuarios";

    private UsersPanel panel;

    @Override
    public boolean onLoad(SGE sge) {
        panel = new UsersPanel(sge);
        return true;
    }

    @Override
    public void unload() {
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String requirePermission() {
        return null;
    }

    @Override
    public void onAppear() {
    }

}
