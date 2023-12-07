package dev.amrv.sge.module.inventory;

import dev.amrv.sge.bbdd.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class InventoryCategory implements Comparable<InventoryCategory> {

    private static final String QUERY_GET_CATEGORY = "SELECT * FROM INVENTORY.CATEGORY WHERE "
            + "ID = {0}";
    private static final String QUERY_CREATE_CATEGORY = "INSERT INTO INVENTORY.CATEGORY (PARENT, NAME) VALUES ("
            + "{0},"
            + "'{1}'"
            + ")";
    private static final String QUERY_MODIFY_CATEGORY = "UPDATE INVENTORY.CATEGORY SET "
            + "PARENT = {1}, "
            + "NAME = '{2}' "
            + "WHERE ID = {0}";

    private static final String QUERY_DELETE_CATEGORY = "DELETE FROM INVENTORY.CATEGORY WHERE ID = {0}";
    private static final String QUERY_GET_SUBCATEGORIES = "SELECT * FROM INVENTORY.CATEGORY WHERE "
            + "PARENT {0}";

    public static List<InventoryCategory> getRoot(Database database) throws SQLException {
        return new InventoryCategory(-1, -1, null).getSubcategories(database);
    }

    public static InventoryCategory get(Database database, int id) throws SQLException {
        final InventoryCategory category = new InventoryCategory(id, -1, null);
        category.get(database);
        return category;
    }

    public static InventoryCategory create(Database database, String name, InventoryCategory parentCategory) throws SQLException {
        return create(database, name, parentCategory == null ? -1 : parentCategory.getID());
    }

    public static InventoryCategory create(Database database, String name, int parentId) throws SQLException {
        final InventoryCategory category = new InventoryCategory(-1, parentId, name);
        category.upload(database);

        return category;
    }

    public static InventoryCategory createLocal(String name, InventoryCategory parent) {
        return new InventoryCategory(-1, parent == null ? -1 : parent.getID(), name);
    }

    public static InventoryCategory createLocal(String name, int parentId) {
        return new InventoryCategory(-1, parentId, name);
    }

    private int id;
    private int parentId;
    private String name;

    private InventoryCategory(int id, int parentId, String name) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }

    public boolean isLocal() {
        return id == -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return id;
    }

    public int getParentID() {
        return parentId;
    }

    public void setParentID(int parentId) {
        this.parentId = parentId;
    }

    public List<InventoryCategory> getSubcategories(Database database) throws SQLException {
        final List<InventoryCategory> subcategories = new ArrayList<>();

        ResultSet set = database.execute(QUERY_GET_SUBCATEGORIES, id == -1 ? " is null" : " = " + id).getSet();

        InventoryCategory cat;
        while (set.next()) {
            cat = new InventoryCategory(set.getInt(1), set.getInt(2), set.getString(3));
            subcategories.add(cat);
        }

        return subcategories;
    }

    public List<InventoryProduct> getProducts(Database database) throws SQLException {
        return InventoryProduct.getProductsInCategory(database, id);
    }

    public void commit(Database database) throws SQLException {
        if (isLocal())
            upload(database);
        else
            modify(database);
    }

    public void rollback(Database database) throws SQLException {
        if (isLocal())
            return;

        get(database);
    }

    public void delete(Database database) throws SQLException {
        if (isLocal())
            return;

        database.execute(QUERY_DELETE_CATEGORY, id);
        database.commit();

        id = -1;
    }

    private void modify(Database database) throws SQLException {
        database.execute(
                QUERY_MODIFY_CATEGORY,
                id,
                parentId == -1 ? null : parentId,
                name
        );
        database.commit();
    }

    private void upload(Database database) throws SQLException {
        final ResultSet set = database.execute(
                QUERY_CREATE_CATEGORY,
                parentId == -1 ? null : parentId,
                name
        ).getSet();

        set.next();
        database.commit();

        id = set.getInt(1);
    }

    private void get(Database database) throws SQLException {
        final ResultSet set = database.execute(
                QUERY_GET_CATEGORY,
                id
        ).getSet();

        set.next();
        database.commit();

        parentId = set.getInt(2);
        name = set.getString(3);
    }

    @Override
    public int compareTo(InventoryCategory o) {
        return Integer.compare(this.getID(), o.getID());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InventoryCategory) {
            InventoryCategory category = (InventoryCategory) obj;
            return this.getID() == category.getID();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 24 * hash + this.id;
        hash = 24 * hash + this.parentId;
        hash = 24 * hash + Objects.hashCode(this.name);
        return hash;
    }

}
