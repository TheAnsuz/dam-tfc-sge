package dev.amrv.sge.module.inventory;

import dev.amrv.sge.bbdd.Database;
import dev.amrv.sge.bbdd.QueryResult;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class InventoryProduct implements Comparable<InventoryProduct> {

    private static final String QUERY_CREATE_PRODUCT = "INSERT INTO INVENTORY.PRODUCT (CATEGORY, NAME) VALUES ("
            + "{0},"
            + "'{1}'"
            + ")";
    private static final String QUERY_MODIFY_PRODUCT = "UPDATE INVENTORY.PRODUCT SET"
            + "CATEGORY = {1},"
            + "NAME = '{2}'"
            + "WHERE ID = {0}";
    private static final String QUERY_GET_PRODUCT = "SELECT * FROM INVENTORY.PRODUCT WHERE "
            + "ID = {0}";
    private static final String QUERY_CHECK_EXISTS = "SELECT COUNT(ID) FROM INVENTORY.PRODUCT WHERE"
            + "ID = {0}";
    private static final String QUERY_DELETE_PRODUCT = "DELETE FROM INVENTORY.PRODUCT WHERE ID = {0}";
    private static final String QUERY_GET_PRODUCTS_IN_CATEGORY = "SELECT * FROM INVENTORY.PRODUCT WHERE "
            + "CATEGORY = {0}";

    public static List<InventoryProduct> getProductsInCategory(Database database, InventoryCategory category) throws SQLException {
        return getProductsInCategory(database, category.getID());
    }

    public static List<InventoryProduct> getProductsInCategory(Database database, int categoryid) throws SQLException {
        ResultSet set = database.execute(QUERY_GET_PRODUCTS_IN_CATEGORY, categoryid).getSet();

        List<InventoryProduct> products = new ArrayList<>();

        while (set.next()) {
            products.add(new InventoryProduct(set.getInt(1), set.getString(4), categoryid));
        }

        return products;
    }

    public static InventoryProduct getOrCreate(Database database, int id, String name, InventoryCategory category) throws SQLException {
        return getOrCreate(database, id, name, category.getID());
    }

    public static InventoryProduct getOrCreate(Database database, int id, String name, int categoryId) throws SQLException {
        if (database.execute(QUERY_CHECK_EXISTS, id).getCount() > 0) {
            // exists
            return get(database, id);
        } else {
            // does not exists
            return create(database, name, categoryId);
        }
    }

    public static InventoryProduct get(Database database, int id) throws SQLException {
        InventoryProduct product = new InventoryProduct(id, null, -1);
        product.get(database);
        return product;
    }

    public static InventoryProduct create(Database database, String name, InventoryCategory category) throws SQLException {
        return create(database, name, category.getID());
    }

    public static InventoryProduct create(Database database, String name, int categoryId) throws SQLException {
        final ResultSet set = database.execute(
                QUERY_CREATE_PRODUCT,
                categoryId,
                name
        ).getSet();

        return new InventoryProduct(set.getInt(1), set.getString(4), set.getInt(2));
    }

    public static InventoryProduct createLocal(String name, InventoryCategory category) {
        return new InventoryProduct(-1, name, category.getID());
    }

    public static InventoryProduct createLocal(String name, int categoryId) {
        return new InventoryProduct(-1, name, categoryId);
    }

    private String name;
    private int categoryId;
    private int id;

    private InventoryProduct(int id, String name, int categoryId) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
    }

    public boolean isLocal() {
        return id == -1;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryID() {
        return categoryId;
    }

    public void setCategoryID(int id) {
        this.categoryId = id;
    }

    public void rollback(Database database) throws SQLException {
        if (id == -1)
            return;

        get(database);
    }

    public void commit(Database database) throws SQLException {
        if (isLocal())
            upload(database);
        else
            modify(database);
    }

    public void delete(Database database) throws SQLException {
        if (isLocal())
            return;

        database.execute(QUERY_DELETE_PRODUCT, id);
        database.commit();

        id = -1;
    }

    private void modify(Database database) throws SQLException {
        database.execute(
                QUERY_MODIFY_PRODUCT,
                id,
                categoryId,
                name
        );
        database.commit();
    }

    private void upload(Database database) throws SQLException {
        QueryResult result = database.execute(
                QUERY_CREATE_PRODUCT,
                categoryId,
                name
        );
        result.getSet().next();
        database.commit();

        id = result.getSet().getInt(1);
    }

    private void get(Database database) throws SQLException {
        QueryResult result = database.execute(
                QUERY_GET_PRODUCT,
                id
        );
        final ResultSet set = result.getSet();
        set.next();
        database.commit();

        name = set.getString(4);
        categoryId = set.getInt(2);
    }

    @Override
    public int compareTo(InventoryProduct o) {
        return Integer.compare(this.getID(), o.getID());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InventoryProduct) {
            InventoryProduct product = (InventoryProduct) obj;
            return this.getID() == product.getID();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.name);
        hash = 31 * hash + this.categoryId;
        hash = 31 * hash + this.id;
        return hash;
    }

}
