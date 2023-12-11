package dev.amrv.sge.module.sells;

import dev.amrv.sge.bbdd.Database;
import dev.amrv.sge.module.inventory.InventoryProduct;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class BillCreator {

    private String description;
    private BillProduct[] products;
    private final int billID;
    private final Map<String, String> attributes = new HashMap<>();
    private final long timestamp;

    public BillCreator(int billId, long timestamp) {
        this.billID = billId;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getBillID() {
        return billID;
    }

    public boolean isLocalBill() {
        return billID == -1;
    }

    public String getAttribute(String name) {
        return attributes.get(name);
    }

    public void setAttribute(String name, String value) {
        attributes.put(name, value);
    }

    public BillProduct[] getProducts() {
        return products;
    }

    public void setProducts(BillProduct... products) {
        this.products = products;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bill build(Database database) throws SQLException {
        // First updates every value
        final Bill bill = Bill.createLocal(getTimestamp(), getDescription());
        try {
            bill.create(database, false);
        } catch (SQLException ex) {
            throw new SQLException("Error uploading bill", ex);
        }

        BillAttribute[] attrs = new BillAttribute[attributes.size()];
        int index = 0;
        try {
            for (Entry<String, String> attribute : attributes.entrySet()) {
                attrs[index] = BillAttribute.createLocal(bill.getID(), attribute.getKey(), attribute.getValue());
                attrs[index].create(database, false);
                index++;
            }
        } catch (SQLException ex) {
            if (attrs[index] == null)
                throw new SQLException("Error uploading attributes", ex);
            else
                throw new SQLException("Error uploading attribute " + attrs[index].getName() + " with value " + attrs[index].getValue(), ex);
        }

        if (products.length == 0)
            throw new SQLException("No se puede crear una factura sin productos");

        InventoryProduct currentProduct;
        for (BillProduct product : products) {
            try {
                product.setBillID(bill.getID());
                currentProduct = InventoryProduct.get(database, product.getProductID());
                currentProduct.changeAmount(-product.getAmount());
                currentProduct.commit(database);
                product.commit(database, false);
            } catch (SQLException ex) {
                throw new SQLException("Error commiting product " + product.getProductName(), ex);
            }
        }

        bill.commit(database);

        for (BillAttribute attr : attrs)
            attr.commit(database);

        for (BillProduct product : products)
            product.commit(database);

        return bill;
    }
}
