package dev.amrv.sge.module.inventory;

import dev.amrv.sge.bbdd.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class InventoryAttribute {

    private int id;
    private final int productId;
    private final String key;
    private String value;

    public static Map<String, InventoryAttribute> getAll(Database database, InventoryProduct product) throws SQLException {
        return getAll(database, product.getID());
    }

    public static Map<String, InventoryAttribute> getAll(Database database, int productId) throws SQLException {
        Map<String, InventoryAttribute> map = new HashMap<>();

        ResultSet set = database.execute(
                "SELECT * FROM INVENTORY.PRODUCT_ATTRIBUTE WHERE PRODUCT = {0}",
                productId
        ).getSet();

        while (set.next()) {
            map.put(set.getString(3), new InventoryAttribute(set.getInt(1), productId, set.getString(3), set.getString(4)));
        }

        return map;
    }

    public static InventoryAttribute get(Database database, int productId, String name) throws SQLException {
        ResultSet set = database.execute(
                "SELECT * FROM INVENTORY.PRODUCT_ATTRIBUTE WHERE PRODUCT = {0} AND NAME = '{1}'",
                productId,
                name
        ).getSet();

        if (!set.next())
            return null;

        return new InventoryAttribute(set.getInt(1), productId, name, set.getString(4));
    }

    public static InventoryAttribute get(Database database, int id) throws SQLException {
        final InventoryAttribute attribute = new InventoryAttribute(id, -1, null, null);
        attribute.rollback(database);
        return attribute;
    }

    public static InventoryAttribute create(Database database, InventoryProduct product, String key, String value) throws SQLException {
        final InventoryAttribute attribute = new InventoryAttribute(-1, product.getID(), key, value);
        attribute.commit(database);
        return attribute;
    }

    public static InventoryAttribute create(Database database, int productId, String key, String value) throws SQLException {
        final InventoryAttribute attribute = new InventoryAttribute(-1, productId, key, value);
        attribute.commit(database);
        return attribute;
    }

    public static InventoryAttribute createLocal(InventoryProduct product, String key, String value) {
        return new InventoryAttribute(-1, product.getID(), key, value);
    }

    public static InventoryAttribute createLocal(int productId, String key, String value) {
        return new InventoryAttribute(-1, productId, key, value);
    }

    private InventoryAttribute(int id, int productId, String key, String value) {
        this.id = id;
        this.productId = productId;
        this.key = key;
        this.value = value;
    }

    public int getProductID() {
        return productId;
    }

    public boolean isLocal() {
        return id == -1;
    }

    public int getID() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void commit(Database database) throws SQLException {
        if (isLocal())
            create(database);
        else
            modify(database);
    }

    private void create(Database database) throws SQLException {
        ResultSet set = database.execute(
                "INSERT INTO INVENTORY.PRODUCT_ATTRIBUTE (PRODUCT,NAME,VALUE) VALUES ({0},'{1}','{2}')",
                productId,
                key,
                value
        ).getSet();

        database.commit();
        set.next();

        id = set.getInt(1);
    }

    private void modify(Database database) throws SQLException {
        database.execute(
                "UPDATE INVENTORY.PRODUCT_ATTRIBUTE SET VALUE = '{1}' WHERE ID = {0}",
                id,
                value
        );

        database.commit();
    }

    public void rollback(Database database) throws SQLException {
        if (isLocal())
            return;

        ResultSet set = database.execute(
                "SELECT VALUE FROM INVENTORY.PRODUCT_ATTRIBUTE"
        ).getSet();

        set.next();

        value = set.getString(4);
    }

    public void delete(Database database) throws SQLException {
        if (isLocal())
            return;

        database.execute(
                "DELETE FROM INVENTORY.PRODUCT_ATTRIBUTE WHERE ID = {0}",
                id
        );
        database.commit();

        id = -1;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.id;
        hash = 61 * hash + this.productId;
        hash = 61 * hash + Objects.hashCode(this.key);
        hash = 61 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final InventoryAttribute other = (InventoryAttribute) obj;
        return this.id == other.id;
    }
}
