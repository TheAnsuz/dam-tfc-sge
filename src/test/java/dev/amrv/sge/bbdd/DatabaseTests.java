package dev.amrv.sge.bbdd;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class DatabaseTests {

    public static void main(String[] args) throws SQLException {
        Database database = new Database("bbdd_test");

        long droped = database.executeUpdateSafe("DROP TABLE TEST.DATA");
        database.commit();

        System.out.println(droped);

        long amount = database.executeUpdate(
                "CREATE TABLE TEST.DATA ("
                + " data_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                + " data_name VARCHAR(32) not null,"
                + " data_value VARCHAR(32)"
                + ")"
        );

        database.commit();
        System.out.println(amount);

        database.executeUpdate("INSERT INTO TEST.DATA VALUES (DEFAULT,'sample_name','sample_value')");
        database.executeUpdate("INSERT INTO TEST.DATA VALUES (DEFAULT,'sample_name_2','sample_value_2')");
        database.commit();

        QueryResult result = database.execute("SELECT * FROM TEST.DATA");

        System.out.println(result);

        database.commit();

        database.exit();
    }

}
