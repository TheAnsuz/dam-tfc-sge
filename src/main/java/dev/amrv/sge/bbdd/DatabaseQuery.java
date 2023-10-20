package dev.amrv.sge.bbdd;

import dev.amrv.sge.SGE;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class DatabaseQuery {

    public final static int RESULT_EMPTY = 0;
    public final static int RESULT_COUNT = 1;
    public final static int RESULT_DATA = 2;
    public final static int RESULT_ERROR = 3;

    private final PreparedStatement statement;

    protected DatabaseQuery(PreparedStatement statement) {
        this.statement = statement;
    }

    public ResultSet executeResult() throws SQLException {
        return statement.executeQuery();
    }

    public long executeCount() throws SQLException {
        return statement.executeLargeUpdate();
    }

    public int execute() throws SQLException {
        if (statement.execute())
            return RESULT_DATA;
        return statement.getUpdateCount() == -1 ? RESULT_EMPTY : RESULT_COUNT;
    }

    public int executeSafe() {
        try {
            return execute();
        } catch (SQLException ex) {
//            SGE.logger().error(ex.toString());
            return RESULT_ERROR;
        }
    }

    public int getResultTypeSafe() {
        try {
            return getResultType();
        } catch (SQLException ex) {
//            SGE.logger().error(ex.toString());
            return RESULT_ERROR;
        }
    }

    public int getResultType() throws SQLException {
        if (statement.getLargeUpdateCount() != -1)
            return RESULT_COUNT;

        if (statement.getResultSet() == null)
            return RESULT_EMPTY;
        return RESULT_DATA;
    }

    public ResultSet getResultSet() throws SQLException {
        return statement.getResultSet();
    }

    public long getResultCount() throws SQLException {
        return statement.getLargeUpdateCount();
    }

    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        statement.setBoolean(parameterIndex, x);
    }

    public void setByte(int parameterIndex, byte x) throws SQLException {
        statement.setByte(parameterIndex, x);
    }

    public void setShort(int parameterIndex, short x) throws SQLException {
        statement.setShort(parameterIndex, x);
    }

    public void setInt(int parameterIndex, int x) throws SQLException {
        statement.setInt(parameterIndex, x);
    }

    public void setLong(int parameterIndex, long x) throws SQLException {
        statement.setLong(parameterIndex, x);
    }

    public void setFloat(int parameterIndex, float x) throws SQLException {
        statement.setFloat(parameterIndex, x);
    }

    public void setDouble(int parameterIndex, double x) throws SQLException {
        statement.setDouble(parameterIndex, x);
    }

    public void setString(int parameterIndex, String x) throws SQLException {
        statement.setString(parameterIndex, x);
    }

    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        statement.setBytes(parameterIndex, x);
    }

    public void setDate(int parameterIndex, Date x) throws SQLException {
        statement.setDate(parameterIndex, x);
    }

    public void setURL(int parameterIndex, URL x) throws SQLException {
        statement.setURL(parameterIndex, x);
    }

}
