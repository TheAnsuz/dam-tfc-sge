package dev.amrv.sge.module.sells;

import dev.amrv.sge.SGE;
import dev.amrv.sge.module.inventory.InventoryCategory;
import java.sql.SQLException;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class SellsBillTreeModel extends DefaultTreeModel {

    private final BillTreeNode root;
    private final SGE sge;

    public SellsBillTreeModel(SGE sge) {
        super(new BillTreeNode(InventoryCategory.createLocal(null, null)), true);
        this.sge = sge;
        root = (BillTreeNode) super.getRoot();
    }

    public void reloadRoot() throws SQLException {
        root.removeAllChildren();

        for (InventoryCategory category : InventoryCategory.getRoot(sge.getDatabase())) {
            BillTreeNode node = new BillTreeNode(category);
            node.reload(sge.getDatabase());
            root.add(node);
        }

        reload();
    }
}
