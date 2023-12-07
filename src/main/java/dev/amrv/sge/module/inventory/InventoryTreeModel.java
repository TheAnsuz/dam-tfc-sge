package dev.amrv.sge.module.inventory;

import dev.amrv.sge.SGE;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public final class InventoryTreeModel implements TreeSelectionListener, MouseListener, TreeWillExpandListener {

    private final InventoryTreeNode dummyRootCategory = new InventoryTreeNode(InventoryCategory.createLocal(null, null));
    private final DefaultTreeModel facade = new DefaultTreeModel(dummyRootCategory);
    private InventoryTreeNode rightClickNode;
    private SGE sge;
    private final TreeSelectionModel selectionModel;
    private final JTree tree;

    public InventoryTreeModel(SGE sge, JTree tree) {
        this.sge = sge;
        tree.setModel(facade);
        this.tree = tree;
        selectionModel = tree.getSelectionModel();
        selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setToggleClickCount(0);
        tree.addTreeSelectionListener(this);
        tree.addMouseListener(this);
        tree.addTreeWillExpandListener(this);
    }

    public synchronized void clear() {
        dummyRootCategory.removeAllChildren();
    }

    public synchronized void add(InventoryTreeNode parent, InventoryTreeNode node) {
        if (parent == null)
            add(node);
        else {
            facade.insertNodeInto(node, parent, parent.getChildCount());
        }
    }

    public synchronized void add(InventoryTreeNode node) {
        facade.insertNodeInto(node, dummyRootCategory, dummyRootCategory.getChildCount());
    }

    public synchronized void reload() {
        facade.reload();
    }

    public synchronized void reload(TreeNode node) {
        facade.reload(node);
    }
    
    public synchronized void reload(InventoryTreeNode node) {
        facade.reload(node);
    }

    public DefaultTreeModel getModel() {
        return facade;
    }

    @Override
    public String toString() {
        return "InventoryTreeModel{" + "dummyRootCategory=" + dummyRootCategory + ", facade=" + facade + '}';
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        final InventoryTreeNode node = (InventoryTreeNode) e.getPath().getLastPathComponent();

        if (!node.equals(rightClickNode))
            selectionModel.clearSelection();
    }

    public void clearPopupItem() {
        rightClickNode = null;
        selectionModel.clearSelection();
    }

    public InventoryTreeNode getPopupItem() {
        return rightClickNode;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        final TreePath path = tree.getPathForLocation(e.getX(), e.getY());

        rightClickNode = null;
        selectionModel.clearSelection();

        if (SwingUtilities.isRightMouseButton(e)) {
            if (path == null)
                rightClickNode = null;
            else {
                rightClickNode = (InventoryTreeNode) path.getLastPathComponent();
                tree.setSelectionPath(path);
            }

        } else if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
            if (path == null)
                return;

            if (tree.isExpanded(path)) {
                tree.collapsePath(path);
            } else {
                try {
                    final InventoryTreeNode node = (InventoryTreeNode) path.getLastPathComponent();

                    node.reload(sge.getDatabase());
                } catch (SQLException ex) {
                    sge.logger.error(ex);
                }
                tree.expandPath(path);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        final InventoryTreeNode node = (InventoryTreeNode) event.getPath().getLastPathComponent();

        if (node.isCategory()) {
            InventoryTreeNode child;
            try {
                for (int i = 0; i < node.getChildCount(); i++) {
                    child = (InventoryTreeNode) node.getChildAt(i);
                    child.reload(sge.getDatabase());
                }
            } catch (SQLException ex) {
                sge.logger.error(ex);
            }

        }
    }

    @Override
    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
    }

}
