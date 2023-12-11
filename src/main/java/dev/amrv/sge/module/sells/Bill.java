package dev.amrv.sge.module.sells;

import dev.amrv.sge.bbdd.Database;
import dev.amrv.sge.bbdd.DatabaseObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class Bill implements DatabaseObject {

    private long timestamp;
    private String description;
    private int id;

    public static List<Bill> getInRange(Database database, Timestamp minDate, Timestamp maxDate) throws SQLException {
        ResultSet set = database.execute("SELECT * FROM SELLS.BILL WHERE DATE >= '{0}' AND DATE <= '{1}'", minDate, maxDate).getSet();

        List<Bill> bills = new ArrayList<>();
        while (set.next())
            bills.add(new Bill(set.getInt(1), set.getTimestamp(2).getTime(), set.getString(3)));

        return bills;
    }

    public static Bill get(Database database, int id) throws SQLException {
        Bill bill = new Bill(id, 0, "");
        bill.read(database);
        return bill;
    }

    public static Bill create(Database database, long timestamp, String description) throws SQLException {
        Bill bill = new Bill(-1, timestamp, description);
        bill.create(database);
        return bill;
    }

    public static Bill createLocal(long timestamp, String description) {
        return new Bill(-1, timestamp, description);
    }

    private Bill(int id, long timestamp, String description) {
        this.id = id;
        this.timestamp = timestamp;
        this.description = description;
    }

    public int getID() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean isLocal() {
        return id == -1;
    }

    @Override
    public void create(Database database, boolean commit) throws SQLException {
        ResultSet set = database.execute("INSERT INTO SELLS.BILL (DATE,DESCRIPTION) VALUES ('{0}','{1}')",
                new Timestamp(timestamp),
                description
        ).getSet();
        if (commit)
            database.commit();
        set.next();
        id = set.getInt(1);
    }

    @Override
    public void update(Database database, boolean commit) throws SQLException {
        database.execute("UPDATE SELLS.BILL SET DESCRIPTION = '{0}' WHERE ID = {1}", description, id);
        if (commit)
            database.commit();
    }

    @Override
    public void read(Database database) throws SQLException {
        ResultSet set = database.execute("SELECT * FROM SELLS.BILL WHERE ID = {0}", id).getSet();

        set.next();
        timestamp = set.getTimestamp(2).getTime();
        description = set.getString(3);
    }

    @Override
    public void delete(Database database, boolean commit) throws SQLException {
        database.execute("DELETE FROM SELLS.BILL WHERE ID = {0}", id);
        if (commit)
            database.commit();
    }

    @Override
    public String toString() {
        return "Bill{" + "timestamp=" + timestamp + ", description=" + description + ", id=" + id + '}';
    }

}
