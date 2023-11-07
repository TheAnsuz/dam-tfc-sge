package dev.amrv.sge.module;

import dev.amrv.sge.SGE;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class InventoryModule extends Module {

    private static final String NAME = "Inventario";

    private JPanel panel;

    @Override
    public boolean load(SGE sge) {
        panel = new JPanel();
        panel.setName(NAME);
        panel.setBackground(Color.red);
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
        return "inventory.see";
    }

}
