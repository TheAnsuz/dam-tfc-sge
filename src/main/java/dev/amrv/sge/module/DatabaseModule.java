package dev.amrv.sge.module;

import dev.amrv.sge.SGE;
import dev.amrv.sge.bbdd.QueryResult;
import dev.amrv.sge.module.database.DatabasePanel;
import java.sql.SQLException;
import javax.swing.JPanel;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class DatabaseModule extends Module {

    private static final String PROPERTY_COMMAND = "module.database.command";

    private DatabasePanel panel;
    private SGE sge;

    @Override
    public boolean onLoad(SGE sge) {
        this.sge = sge;
        panel = new DatabasePanel(this);
        panel.setCommand(sge.getProperties().getProperty(PROPERTY_COMMAND, ""));
        return true;
    }

    public SGE getSge() {
        return sge;
    }

    @Override
    public void unload() {
        if (panel == null)
            return;

        String command = panel.getCommand();

        if (!command.trim().isEmpty())
            sge.getProperties().setProperty(PROPERTY_COMMAND, command.trim());
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public String getName() {
        return "Database";
    }

    @Override
    public String requirePermission() {
        return "database.see";
    }

    public QueryResult executeQuery(String query) throws SQLException {
        return sge.getDatabase().execute(query);
    }

    public void commit() throws SQLException {
        sge.getDatabase().commit();
    }

    public void rollback() throws SQLException {
        sge.getDatabase().rollback();
    }

    @Override
    public void onAppear() {
    }
}
