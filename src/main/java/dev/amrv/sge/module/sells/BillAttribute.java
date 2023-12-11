package dev.amrv.sge.module.sells;

import dev.amrv.sge.bbdd.Database;
import dev.amrv.sge.bbdd.DatabaseObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class BillAttribute implements DatabaseObject {

    private int ID;
    private int billID;
    private String name;
    private String value;

    public static Map<String, BillAttribute> getAll(Database database, int billID) throws SQLException {
        ResultSet set = database.execute("SELECT * FROM SELLS.BILL_ATTRIBUTE WHERE BILL = {0}", billID).getSet();

        Map<String, BillAttribute> attributes = new HashMap<>();

        while (set.next()) {
            attributes.put(set.getString(3), new BillAttribute(set.getInt(1), billID, set.getString(3), set.getString(4)));
        }

        return attributes;
    }

    public static BillAttribute get(Database database, int billId, String name) throws SQLException {
        ResultSet set = database.execute("SELECT * FROM SELLS.BILL_ATTRIBUTE WHERE BILL = {0} AND NAME = '{1}'", billId, name).getSet();

        if (set.next()) {
            return new BillAttribute(set.getInt(1), set.getInt(2), set.getString(3), set.getString(4));
        } else
            return null;
    }

    public static BillAttribute get(Database database, int id) throws SQLException {
        BillAttribute attribute = new BillAttribute(id, 0, "", "");
        attribute.read(database);
        return attribute;
    }

    public static BillAttribute getOrCreate(Database database, int billID, String name) throws SQLException {
        BillAttribute existing = get(database, billID, name);
        if (existing == null)
            return create(database, billID, name, "");
        return existing;
    }

    public static BillAttribute create(Database database, int billID, String name, String value) throws SQLException {
        BillAttribute attribute = new BillAttribute(-1, billID, name, value);
        attribute.commit(database);
        return attribute;
    }

    public static BillAttribute createLocal(int billID, String name, String value) {
        return new BillAttribute(-1, billID, name, value);
    }

    private BillAttribute(int ID, int billID, String name, String value) {
        this.ID = ID;
        this.billID = billID;
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getID() {
        return ID;
    }

    public int getBillID() {
        return billID;
    }

    @Override
    public boolean isLocal() {
        return ID == -1;
    }

    @Override
    public void create(Database database, boolean commit) throws SQLException {
        ResultSet set = database.execute("INSERT INTO SELLS.BILL_ATTRIBUTE (BILL,NAME,VALUE) VALUES ({0},'{1}','{2}')", billID, name, value).getSet();
        if (commit)
            database.commit();

        if (!set.next())
            return;

        ID = set.getInt(1);
    }

    @Override
    public void update(Database database, boolean commit) throws SQLException {
        database.execute("UPDATE SELLS.BILL_ATTRIBUTE SET NAME = '{0}', VALUE = '{1}' WHERE ID = {2}", name, value, ID);
        if (commit)
            database.commit();
    }

    @Override
    public void read(Database database) throws SQLException {
        ResultSet set = database.execute("SELECT * FROM SELLS.BILL_ATTRIBUTE WHERE ID = {0}", ID).getSet();

        if (!set.next())
            return;

        billID = set.getInt(2);
        name = set.getString(3);
        value = set.getString(4);
    }

    @Override
    public void delete(Database database, boolean commit) throws SQLException {
        if (isLocal())
            return;

        database.execute("DELETE FROM SELLS.BILL_ATTRIBUTE WHERE ID = {0}", ID);
        if (commit)
            database.commit();
    }

}
