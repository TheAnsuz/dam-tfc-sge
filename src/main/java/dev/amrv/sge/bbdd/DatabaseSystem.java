package dev.amrv.sge.bbdd;

import dev.amrv.sge.SGE;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.derby.jdbc.EmbeddedDriver;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class DatabaseSystem {

    private static DatabaseSystem instance;

    public static void create() throws SQLException {
        DriverManager.registerDriver(new EmbeddedDriver());

        instance = new DatabaseSystem();
    }

    public static void dispose() throws SQLException {
        instance.connection.close();
        instance = null;
    }

    public static DatabaseSystem get() {
        return instance;
    }

    protected final Connection connection;

    private DatabaseSystem() throws SQLException {
        String url = "jdbc:derby:directory:database;create=true";
        connection = DriverManager.getConnection(url);
        connection.setAutoCommit(false);
    }

    public DatabaseQuery createQuery(String query) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException ex) {
//            SGE.logger().error(ex.toString());
        }
        return new DatabaseQuery(ps);
    }

    public boolean safeCommit() {
        try {
            commit();
            return true;
        } catch (SQLException ex) {
//            SGE.logger().error(ex.toString());
            return false;
        }
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public boolean safeRollback() {
        try {
            rollback();
            return true;
        } catch (SQLException ex) {
//            SGE.logger().error(ex.toString());
            return false;
        }
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }
}
