package dev.amrv.sge.bbdd;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.derby.jdbc.EmbeddedDriver;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class Database {

    private static final String DATABASE_CREATE = "jdbc:derby:directory:{name};create=true";
    protected final Connection connection;

    public Database(String name) throws SQLException {
        DriverManager.registerDriver(new EmbeddedDriver());
        connection = DriverManager.getConnection(DATABASE_CREATE.replace("{name}", name));
        connection.setAutoCommit(false);
    }

    public DatabaseMetaData getMetadata() throws SQLException {
        return connection.getMetaData();
    }

    public void exit() throws SQLException {
        connection.close();
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    public ResultSet execute(String sql) throws SQLException {
        return connection.prepareCall(sql).executeQuery();
    }

    public long executeUpdateSafe(String sql) {
        try {
            return executeUpdate(sql);
        } catch (SQLException ex) {
            return 0;
        }
    }

    public long executeUpdate(String sql) throws SQLException {
        return connection.prepareCall(sql).executeLargeUpdate();
    }

}
