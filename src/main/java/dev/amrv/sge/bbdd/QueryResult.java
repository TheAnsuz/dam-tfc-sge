package dev.amrv.sge.bbdd;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class QueryResult {

    /**
     * The type of result for the queries that generated a ResultSet as their
     * result
     */
    public static final int TYPE_RESULT = 1;
    /**
     * The type of result for the queries that generated an update amount as
     * their result
     */
    public static final int TYPE_COUNT = 2;

    private final int type;
    private final long count;
    private final ResultSet result;

    public QueryResult(int type, long count, ResultSet result) {
        this.type = type;
        this.count = count;
        this.result = result;
    }

    public int getType() {
        return type;
    }

    public long getCount() {
        return count;
    }

    public ResultSet getSet() {
        return result;
    }

    @Override
    public String toString() {
        return format();
    }

    public String format() {
        if (type == TYPE_COUNT) {
            return count + (count > 1 ? " Rows have " : " Row has ") + "been updated";
        }

        StringBuilder builder = new StringBuilder();

        try {
            final ResultSetMetaData meta = result.getMetaData();
            final int columns = meta.getColumnCount();

            // Print column header
            builder.append("| ");
            for (int i = 1; i <= columns; i++) {
                int columnWidth = meta.getColumnDisplaySize(i);
                builder.append(
                        String.format("%-" + columnWidth + "s", meta.getColumnLabel(i))
                ).append(" | ");
            }
            builder.append(System.lineSeparator());

            while (result.next()) {
                builder.append("| ");
                for (int col = 1; col <= columns; col++) {
                    int columnWidth = meta.getColumnDisplaySize(col);
                    Object entry = result.getObject(col);
                    builder.append(
                            String.format("%-" + columnWidth + "s", entry == null ? null : entry.toString())
                    ).append(" | ");
                }
                builder.append(System.lineSeparator());
            }

        } catch (SQLException ex) {
            return ex.getSQLState() + ":" + ex.toString();
        }

        return builder.toString();
    }

}
