package dev.amrv.sge.module.sells;

import dev.amrv.sge.bbdd.Database;
import dev.amrv.sge.bbdd.DatabaseObject;
import dev.amrv.sge.module.inventory.InventoryAttribute;
import dev.amrv.sge.module.inventory.InventoryProduct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class BillProduct implements DatabaseObject {

    private int id;
    private int billID;
    private int productID;
    private int amount;
    private float cost;
    private float price;
    private String productName;

    public static List<BillProduct> getAll(Database database, int billID) throws SQLException {
        ResultSet set = database.execute("SELECT * FROM SELLS.BILL_PRODUCT WHERE BILL_ID = {0}", billID).getSet();

        List<BillProduct> products = new ArrayList<>();

        while (set.next()) {
            products.add(new BillProduct(set.getInt(1), billID, set.getInt(3), set.getString(4), set.getInt(5), set.getFloat(6), set.getFloat(7)));
        }

        return products;
    }

    public static BillProduct get(Database database, int id) throws SQLException {
        BillProduct billProduct = new BillProduct(id, 0, 0, "", 0, 0, 0);
        billProduct.read(database);
        return billProduct;
    }

    public static BillProduct createLocal(Database database, Bill bill, InventoryProduct product, int amount) throws SQLException {
        return createLocal(database, bill.getID(), product.getID(), amount);
    }

    public static BillProduct createLocal(Database database, int billID, int productID, int amount) throws SQLException {
        BillProduct billProduct = new BillProduct(-1, billID, productID, "", amount, 0, 0);
        billProduct.calculateBill(database);
        return billProduct;
    }

    private BillProduct(int id, int billID, int productID, String productName, int amount, float cost, float price) {
        this.id = id;
        this.billID = billID;
        this.productID = productID;
        this.productName = productName;
        this.amount = amount;
        this.cost = cost;
        this.price = price;
    }

    public void calculateBill(Database database) throws SQLException {
        InventoryProduct product = InventoryProduct.get(database, productID);
        productName = product.getName();
        if (amount > product.getAmount())
            amount = product.getAmount();

        InventoryAttribute costAttribute = InventoryAttribute.get(database, productID, "coste_compra");
        if (costAttribute != null) {
            try {
                cost = Float.parseFloat(costAttribute.getValue());
            } catch (NumberFormatException nfe) {
                cost = 0;
            }
        }

        InventoryAttribute priceAttribute = InventoryAttribute.get(database, productID, "coste_venta");
        if (priceAttribute != null) {
            try {
                price = Float.parseFloat(priceAttribute.getValue());
            } catch (NumberFormatException nfe) {
                price = 0;
            }
        }
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }

    public int getID() {
        return id;
    }

    public int getProductID() {
        return productID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public void setFinalCost(float cost) {
        this.cost = cost / amount;
    }

    public float getCost() {
        return cost;
    }

    public float getFinalCost() {
        return cost * amount;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setFinalPrice(float price) {
        this.price = price / amount;
    }

    public float getPrice() {
        return price;
    }

    public float getFinalPrice() {
        return price * amount;
    }

    public float getProfit() {
        return price - cost;
    }

    public float getFinalProfit() {
        return getFinalPrice() - getFinalCost();
    }

    public String getProductName() {
        return productName;
    }

    @Override
    public boolean isLocal() {
        return id == -1;
    }

    @Override
    public void create(Database database, boolean commit) throws SQLException {
        ResultSet set = database.execute(
                "INSERT INTO SELLS.BILL_PRODUCT (BILL_ID,PRODUCT_ID,PRODUCT_NAME,AMOUNT,COST,EARN) VALUES ({0},{1},'{2}',{3},{4},{5})",
                billID,
                productID,
                productName,
                amount,
                cost,
                price).getSet();

        if (commit)
            database.commit();

        set.next();
        id = set.getInt(1);
    }

    @Override
    public void update(Database database, boolean commit) throws SQLException {
        database.execute("UPDATE SELLS.BILL_PRODUCT SET AMOUNT = {0}, COST = {1}, EARN = {2} WHERE ID = {3}",
                amount,
                cost,
                price,
                id);

        if (commit)
            database.commit();
    }

    @Override
    public void read(Database database) throws SQLException {
        ResultSet set = database.execute("SELECT * FROM SELLS.BILL_PRODUCT WHERE ID = {0}", id).getSet();

        if (!set.next())
            return;

        billID = set.getInt(2);
        productID = set.getInt(3);
        amount = set.getInt(4);
        cost = set.getFloat(5);
        price = set.getFloat(6);
    }

    @Override
    public void delete(Database database, boolean commit) throws SQLException {
        database.execute("DELETE FROM SELLS.BILL_PRODUCT WHERE ID = {0}", id);

        if (commit)
            database.commit();
    }
}
