package dev.amrv.sge.bbdd;

import java.sql.SQLException;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class DatabaseErrors {

    public static final String ERROR_ALREADY_EXISTS_DATA = "X0Y32";
    public static final String ERROR_ALREADY_EXISTS_CONTAINER = "X0Y68";

    public static boolean alreadyExists(SQLException exception) {
        return exception.getSQLState().equals(ERROR_ALREADY_EXISTS_DATA) || exception.getSQLState().equals(ERROR_ALREADY_EXISTS_CONTAINER);
    }

    private DatabaseErrors() {
    }
}
