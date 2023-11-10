package dev.amrv.sge.bbdd;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public PreparedStatement statement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public QueryResult execute(String sql, Object... params) throws SQLException {

        for (int i = 0; i < params.length; i++) {
            sql = sql.replace("{" + i + "}", params[i] == null ? "NULL" : params[i].toString());
        }
        
        final Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        stmt.closeOnCompletion();

        if (stmt.execute(sql, Statement.RETURN_GENERATED_KEYS))
            return new QueryResult(QueryResult.TYPE_RESULT, stmt.getLargeUpdateCount(), stmt.getResultSet());
        else
            return new QueryResult(QueryResult.TYPE_COUNT, stmt.getLargeUpdateCount(), stmt.getGeneratedKeys());
    }

    public long executeUpdateSafe(String sql, Object... params) {
        try {
            return executeUpdate(sql, params);
        } catch (SQLException ex) {
            return 0;
        }
    }

    public long executeUpdate(String sql, Object... params) throws SQLException {
        return execute(sql, params).getCount();
    }

}
