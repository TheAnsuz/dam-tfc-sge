package dev.amrv.sge.module;

import dev.amrv.sge.SGE;
import javax.swing.JPanel;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class ConfigurationModule extends Module {

    public static final String NAME = "Configuration";

    private JPanel panel;

    @Override
    public boolean load(SGE sge) {
        panel = new JPanel();

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
        return "configuration.see";
    }

}
