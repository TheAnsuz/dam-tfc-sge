package dev.amrv.sge.module.inventory;

import dev.amrv.sge.bbdd.Database;
import java.sql.SQLException;
import java.util.Collection;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class InventoryTreeNode extends DefaultMutableTreeNode implements Comparable<InventoryTreeNode> {

    private final boolean isCategory;

    private String attributeString;

    public InventoryTreeNode(InventoryProduct product, Collection<InventoryAttribute> attributes) {
        super(product, false);
        buildAttributes(attributes);
        isCategory = false;
    }

    public InventoryTreeNode(InventoryCategory category) {
        super(category, true);
        isCategory = true;
    }

    public final void buildAttributes(Collection<InventoryAttribute> attributes) {
        final StringBuilder builder = new StringBuilder();

        if (attributes.isEmpty()) {
            attributeString = "";
            return;
        }

        builder.append("(");
        for (InventoryAttribute attribute : attributes)
            builder.append(attribute.getKey()).append(" ").append(attribute.getValue()).append(", ");

        if (builder.lastIndexOf(",") > -1)
            builder.deleteCharAt(builder.length() - 1).deleteCharAt(builder.length() - 1);

        builder.append(")");

        attributeString = builder.toString();
    }

    public boolean isCategory() {
        return isCategory;
    }

    public InventoryCategory getAsCategory() {
        return (InventoryCategory) getUserObject();
    }

    public InventoryProduct getAsProduct() {
        return (InventoryProduct) getUserObject();
    }

    public void reload(Database database) throws SQLException {
        if (isCategory) {
            reloadCategories(database);

            reloadProducts(database);
        } else {
            getAsProduct().rollback(database);
            buildAttributes(InventoryAttribute.getAll(database, getAsProduct()).values());
            super.setUserObject(getAsProduct());
        }
    }

    private void reloadCategories(Database database) throws SQLException {
        category:
        for (InventoryCategory category : getAsCategory().getSubcategories(database)) {

            InventoryTreeNode node;
            for (int i = 0; i < super.getChildCount(); i++) {
                node = (InventoryTreeNode) super.getChildAt(i);

                if (!node.isCategory)
                    break;

                if (node.getAsCategory().getID() == category.getID())
                    break category;
            }

            super.add(new InventoryTreeNode(category));
        }
    }

    private void reloadProducts(Database database) throws SQLException {
        product:
        for (InventoryProduct product : InventoryProduct.getProductsInCategory(database, getAsCategory())) {

            InventoryTreeNode node;
            for (int i = 0; i < super.getChildCount(); i++) {
                node = (InventoryTreeNode) super.getChildAt(i);

                if (!node.isCategory)
                    break;

                if (node.getAsCategory().getID() == product.getID())
                    break product;
            }

            super.add(new InventoryTreeNode(product, InventoryAttribute.getAll(database, product).values()));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InventoryTreeNode)
            return this.getUserObject().equals(((DefaultMutableTreeNode) obj).getUserObject());
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.isCategory ? 1 : 0);
        return hash;
    }

    @Override
    public int compareTo(InventoryTreeNode o) {
        if (this.isCategory() && o.isCategory()) {
            return Integer.compare(this.getAsCategory().getID(), o.getAsCategory().getID());
        }
        return Integer.compare(this.getAsProduct().getID(), o.getAsProduct().getID());
    }

    @Override
    public String toString() {
        if (isCategory())
            return getAsCategory().getName();
        else {
            InventoryProduct product = getAsProduct();
            return product.getName() + " [" + product.getAmount() + "] " + attributeString;
        }
    }
}
