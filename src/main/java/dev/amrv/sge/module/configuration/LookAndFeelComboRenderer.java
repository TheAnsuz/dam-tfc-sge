package dev.amrv.sge.module.configuration;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class LookAndFeelComboRenderer implements ListCellRenderer<LookAndFeelInfo> {

    private final JLabel normal;
    private final JLabel selected;
    private final JLabel focused;

    public LookAndFeelComboRenderer() {
        normal = new JLabel();
        selected = new JLabel();
        selected.setFont(selected.getFont().deriveFont(Font.BOLD));
        focused = new JLabel();
        focused.setFont(selected.getFont().deriveFont(Font.ITALIC));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends LookAndFeelInfo> list, LookAndFeelInfo value, int index, boolean isSelected, boolean cellHasFocus) {
        normal.setText(value.getName());
        return normal;
    }

}
