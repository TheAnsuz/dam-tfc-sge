/*
 */
package dev.amrv.sge.module.inventory;

import dev.amrv.sge.SGE;
import dev.amrv.sge.window.SGENotifier;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public final class InventoryPanel extends javax.swing.JPanel implements PopupMenuListener {

    private final InventoryTreeModel treeModel;
    private final SGE sge;
    private final InventoryUtils utils;
    private final JPopupMenu popup;

    public InventoryPanel(SGE sge, InventoryUtils utils) {
        this.sge = sge;
        this.utils = utils;
        initComponents();
        treeModel = new InventoryTreeModel(sge, jTree1);
        //jTree1.setCellRenderer(new InventoryTreeRenderer(treeModel));
        this.popup = generatePopupMenu();
        jTree1.setComponentPopupMenu(popup);
        popup.addPopupMenuListener(this);
    }

    protected JPopupMenu generatePopupMenu() {
        final JPopupMenu menu = new JPopupMenu();

        menu.add(menuCreateCategory);
        menu.add(menuCreateProduct);
        menu.addSeparator();
        menu.add(menuDelete);
        menu.addSeparator();
        menu.add(menuUpdate);

        return menu;
    }

    public void recreateTree() {
        treeModel.clear();

        try {
            for (InventoryCategory category : InventoryCategory.getRoot(sge.getDatabase())) {
                final InventoryTreeNode node = new InventoryTreeNode(category);
                node.reload(sge.getDatabase());
                treeModel.add(node);
            }
        } catch (SQLException ex) {
            SGENotifier.displayError(this, "Error", "Error loading categories", ex);
        }

        treeModel.reload();
        jTree1.setModel(treeModel.getModel());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuCreateCategory = new javax.swing.JMenuItem();
        menuCreateProduct = new javax.swing.JMenuItem();
        menuDelete = new javax.swing.JMenuItem();
        menuUpdate = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();

        menuCreateCategory.setText("Crear categoria");
        menuCreateCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCreateCategoryActionPerformed(evt);
            }
        });

        menuCreateProduct.setText("Crear producto");

        menuDelete.setText("Eliminar");
        menuDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDeleteActionPerformed(evt);
            }
        });

        menuUpdate.setText("Actualizar");
        menuUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuUpdateActionPerformed(evt);
            }
        });

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree1.setRootVisible(false);
        jTree1.setScrollsOnExpand(false);
        jTree1.setShowsRootHandles(true);
        jScrollPane1.setViewportView(jTree1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void menuCreateCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCreateCategoryActionPerformed
        if (treeModel.getPopupItem() == null)
            return;

        final InventoryTreeNode node = treeModel.getPopupItem();

        if (!node.isCategory())
            return;

        final InventoryCategory parent = node.getAsCategory();
        try {
            InventoryCategory created = InventoryCategory.create(sge.getDatabase(), JOptionPane.showInputDialog(this, "Nombre de categoria"), parent);

            treeModel.add(treeModel.getPopupItem(), new InventoryTreeNode(created));
        } catch (SQLException ex) {
            SGENotifier.displayError(this, "Error", "Could not create category", ex);
            sge.logger.error(ex);
        }
        
        treeModel.clearPopupItem();
    }//GEN-LAST:event_menuCreateCategoryActionPerformed

    private void menuUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuUpdateActionPerformed
        if (treeModel.getPopupItem() == null)
            recreateTree();
        else
            treeModel.reload(treeModel.getPopupItem());

        treeModel.clearPopupItem();
    }//GEN-LAST:event_menuUpdateActionPerformed

    private void menuDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDeleteActionPerformed
        if (treeModel.getPopupItem() == null)
            return;

        final InventoryTreeNode node = treeModel.getPopupItem();

        if (node.isCategory()) {
            try {
                node.getAsCategory().delete(sge.getDatabase());
                InventoryTreeNode parent = (InventoryTreeNode) node.getParent();
                node.removeFromParent();
                treeModel.reload(parent);
            } catch (SQLException ex) {
                sge.logger.error(ex);
                SGENotifier.displayError(this, "Error", "No se puede eliminar la categoria " + node.getAsCategory().getName(), ex);
            }
        } else {
            try {
                node.getAsProduct().delete(sge.getDatabase());
                InventoryTreeNode parent = (InventoryTreeNode) node.getParent();
                node.removeFromParent();
                treeModel.reload(parent);
            } catch (SQLException ex) {
                sge.logger.error(ex);
                SGENotifier.displayError(this, "Error", "No se puede eliminar el producto " + node.getAsProduct().getName(), ex);
            }
        }

        treeModel.clearPopupItem();
    }//GEN-LAST:event_menuDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree jTree1;
    private javax.swing.JMenuItem menuCreateCategory;
    private javax.swing.JMenuItem menuCreateProduct;
    private javax.swing.JMenuItem menuDelete;
    private javax.swing.JMenuItem menuUpdate;
    // End of variables declaration//GEN-END:variables

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        final InventoryTreeNode node = treeModel.getPopupItem();

        if (node == null) {
            menuCreateProduct.setEnabled(false);
            menuCreateCategory.setEnabled(true);
            menuDelete.setEnabled(false);

        } else if (node.isCategory()) {
            menuCreateCategory.setEnabled(true);
            menuCreateProduct.setEnabled(true);
            menuDelete.setEnabled(node.getChildCount() == 0);

        } else {
            menuCreateProduct.setEnabled(false);
            menuCreateCategory.setEnabled(false);
            menuDelete.setEnabled(true);
        }
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
    }
}
