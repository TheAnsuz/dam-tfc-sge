package dev.amrv.sge.module.providers;

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
public class Provider {

    private int id;
    private String name;
    private String cif;

    public static List<Provider> getAll(Database database) throws SQLException {
        ResultSet set = database.execute(
                "SELECT * FROM PROVIDER"
        ).getSet();

        List<Provider> providers = new ArrayList<>();

        while (set.next()) {
            providers.add(new Provider(set.getInt(1), set.getString(3), set.getString(2)));
        }

        return providers;
    }

    public static Provider get(Database database, int id) throws SQLException {
        final Provider provider = new Provider(id, null, null);
        provider.rollback(database);
        return provider;
    }

    public static Provider create(Database database, String name, String cif) throws SQLException {
        final Provider provider = new Provider(-1, name, cif);
        provider.commit(database);
        return provider;
    }

    public static Provider createLocal(String name, String cif) {
        return new Provider(-1, name, cif);
    }

    private Provider(int id, String name, String cif) {
        this.id = id;
        this.name = name;
        this.cif = cif;
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

    public String CIF() {
        return cif;
    }

    public void commit(Database database) throws SQLException {
        if (isLocal())
            create(database);
        else
            update(database);
    }

    private void update(Database database) throws SQLException {
        database.execute("UPDATE PROVIDER "
                + "SET NAME = '{1}'"
                + "WHERE ID = {0}",
                id,
                name
        );

        database.commit();
    }

    private void create(Database database) throws SQLException {
        ResultSet set = database.execute("INSERT INTO PROVIDER (CIF,NAME) VALUES ('{0}','{1}')",
                cif,
                name
        ).getSet();

        database.commit();

        set.next();
        id = set.getInt(1);
    }

    public void rollback(Database database) throws SQLException {
        if (isLocal())
            return;

        ResultSet set = database.execute(
                "SELECT * FROM PROVIDER WHERE ID = {0}",
                id
        ).getSet();
        set.next();

        cif = set.getString(2);
        name = set.getString(3);
    }

    public void delete(Database database) throws SQLException {
        if (isLocal())
            return;

        database.execute(
                "DELETE FROM PROVIDER WHERE ID = {0}",
                id
        );

        database.commit();
        id = -1;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.id;
        hash = 79 * hash + Objects.hashCode(this.name);
        hash = 79 * hash + Objects.hashCode(this.cif);
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
        final Provider other = (Provider) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return name;
    }

}
