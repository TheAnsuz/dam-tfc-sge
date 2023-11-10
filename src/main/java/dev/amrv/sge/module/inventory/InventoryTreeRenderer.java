package dev.amrv.sge.module.inventory;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class InventoryTreeRenderer extends DefaultTreeCellRenderer {

    private final InventoryTreeModel model;

    public InventoryTreeRenderer(InventoryTreeModel model) {
        this.model = model;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        InventoryTreeNode node = model.getPopupItem();

        Component comp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        if (node != null && model.getPopupItem().equals(node))
            comp.setFont(comp.getFont().deriveFont(Font.BOLD));
        else
            comp.setFont(comp.getFont().deriveFont(Font.PLAIN));

        return comp;
    }

}
