/*
 */
package dev.amrv.sge.bbdd;

import java.sql.SQLException;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public interface DatabaseObject {

    boolean isLocal();

    default void commit(Database database) throws SQLException {
        commit(database, true);
    }

    default void commit(Database database, boolean autoAccept) throws SQLException {
        if (isLocal())
            create(database, autoAccept);
        else
            update(database, autoAccept);
    }

    default void rollback(Database database) throws SQLException {
        if (isLocal())
            return;

        read(database);
    }

    default void create(Database database) throws SQLException {
        create(database, true);
    }

    default void update(Database database) throws SQLException {
        update(database, true);
    }

    default void delete(Database database) throws SQLException {
        delete(database, true);
    }

    void create(Database database, boolean commit) throws SQLException;

    void update(Database database, boolean commit) throws SQLException;

    void read(Database database) throws SQLException;

    void delete(Database database, boolean commit) throws SQLException;
}
