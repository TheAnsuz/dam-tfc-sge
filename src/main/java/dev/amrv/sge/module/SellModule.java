package dev.amrv.sge.module;

import dev.amrv.sge.SGE;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class SellModule extends Module {

    private static final String NAME = "Venta";

    private JPanel panel;

    @Override
    public boolean onLoad(SGE sge) {
        panel = new JPanel();
        panel.setName(NAME);
        panel.setBackground(Color.blue);
        panel.setMinimumSize(new Dimension(1100, 500));
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
        return "sell.see";
    }

    @Override
    public void onAppear() {
    }

}
