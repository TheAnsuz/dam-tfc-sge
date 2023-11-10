package dev.amrv.sge.module;

import dev.amrv.sge.SGE;
import dev.amrv.sge.module.configuration.ConfigurationPanel;
import javax.swing.JPanel;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class ConfigurationModule extends Module {

    public static final String NAME = "Configuration";

    private ConfigurationPanel panel;

    @Override
    public boolean onLoad(SGE sge) {
        panel = new ConfigurationPanel(sge);

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

    @Override
    public void onAppear() {
        panel.updateConfigFields();
    }

}
