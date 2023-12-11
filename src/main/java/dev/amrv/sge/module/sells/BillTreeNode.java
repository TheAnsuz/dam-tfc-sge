package dev.amrv.sge.module.sells;

import dev.amrv.sge.bbdd.Database;
import dev.amrv.sge.module.inventory.InventoryAttribute;
import dev.amrv.sge.module.inventory.InventoryCategory;
import dev.amrv.sge.module.inventory.InventoryProduct;
import dev.amrv.sge.module.inventory.InventoryTreeNode;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class BillTreeNode extends InventoryTreeNode {

    public BillTreeNode(InventoryProduct product, Collection<InventoryAttribute> attributes) {
        super(product, attributes);
    }

    public BillTreeNode(InventoryCategory category) {
        super(category);
    }

    @Override
    protected void reloadCategories(Database database) throws SQLException {
        category:
        for (InventoryCategory category : getAsCategory().getSubcategories(database)) {

            BillTreeNode node;
            for (int i = 0; i < super.getChildCount(); i++) {
                node = (BillTreeNode) super.getChildAt(i);

                if (!node.isCategory())
                    break;

                if (node.getAsCategory().getID() == category.getID())
                    break category;
            }

            super.add(new BillTreeNode(category));
        }
    }

    @Override
    protected void reloadProducts(Database database) throws SQLException {
        product:
        for (InventoryProduct product : InventoryProduct.getProductsInCategory(database, getAsCategory())) {

            BillTreeNode node;
            for (int i = 0; i < super.getChildCount(); i++) {
                node = (BillTreeNode) super.getChildAt(i);

                if (!node.isCategory())
                    break;

                if (node.getAsCategory().getID() == product.getID())
                    break product;
            }

            super.add(new BillTreeNode(product, InventoryAttribute.getAll(database, product).values()));
        }
    }
}
