package dev.amrv.sge.module.providers;

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
public class ProviderAttribute {

    private int id;
    private final int providerId;
    private final String key;
    private String value;

    public static Map<String, ProviderAttribute> getAll(Database database, Provider provider) throws SQLException {
        return getAll(database, provider.getID());
    }

    public static Map<String, ProviderAttribute> getAll(Database database, int providerId) throws SQLException {
        Map<String, ProviderAttribute> map = new HashMap<>();

        ResultSet set = database.execute(
                "SELECT * FROM PROVIDER_ATTRIBUTE WHERE PROVIDER = {0}",
                providerId
        ).getSet();

        while (set.next()) {
            map.put(set.getString(3), new ProviderAttribute(set.getInt(1), providerId, set.getString(3), set.getString(4)));
        }

        return map;
    }

    public static ProviderAttribute get(Database database, int providerId, String name) throws SQLException {
        ResultSet set = database.execute(
                "SELECT * FROM PROVIDER_ATTRIBUTE WHERE PROVIDER = {0} AND NAME = '{1}'",
                providerId,
                name
        ).getSet();

        set.next();

        return new ProviderAttribute(set.getInt(1), providerId, name, set.getString(4));
    }

    public static ProviderAttribute get(Database database, int id) throws SQLException {
        final ProviderAttribute attribute = new ProviderAttribute(id, -1, null, null);
        attribute.rollback(database);
        return attribute;
    }

    public static ProviderAttribute create(Database database, Provider provider, String key, String value) throws SQLException {
        final ProviderAttribute attribute = new ProviderAttribute(-1, provider.getID(), key, value);
        attribute.commit(database);
        return attribute;
    }

    public static ProviderAttribute create(Database database, int providerId, String key, String value) throws SQLException {
        final ProviderAttribute attribute = new ProviderAttribute(-1, providerId, key, value);
        attribute.commit(database);
        return attribute;
    }

    public static ProviderAttribute createLocal(Provider provider, String key, String value) {
        return new ProviderAttribute(-1, provider.getID(), key, value);
    }

    public static ProviderAttribute createLocal(int providerId, String key, String value) {
        return new ProviderAttribute(-1, providerId, key, value);
    }

    private ProviderAttribute(int id, int providerId, String key, String value) {
        this.id = id;
        this.providerId = providerId;
        this.key = key;
        this.value = value;
    }

    public int getProviderID() {
        return providerId;
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
                "INSERT INTO PROVIDER_ATTRIBUTE (PROVIDER,NAME,VALUE) VALUES ({0},'{1}','{2}')",
                providerId,
                key,
                value
        ).getSet();

        database.commit();
        set.next();

        id = set.getInt(1);
    }

    private void modify(Database database) throws SQLException {
        database.execute(
                "UPDATE PROVIDER_ATTRIBUTE SET VALUE = '{1}' WHERE ID = {0}",
                id,
                value
        );

        database.commit();
    }

    public void rollback(Database database) throws SQLException {
        if (isLocal())
            return;

        ResultSet set = database.execute(
                "SELECT VALUE FROM PROVIDER_ATTRIBUTE"
        ).getSet();

        set.next();

        value = set.getString(4);
    }

    public void delete(Database database) throws SQLException {
        if (isLocal())
            return;

        database.execute(
                "DELETE FROM PROVIDER_ATTRIBUTE WHERE ID = {0}",
                id
        );
        database.commit();

        id = -1;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.id;
        hash = 61 * hash + this.providerId;
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
        final ProviderAttribute other = (ProviderAttribute) obj;
        return this.id == other.id;
    }

}
