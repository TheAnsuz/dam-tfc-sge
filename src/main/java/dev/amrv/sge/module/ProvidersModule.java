package dev.amrv.sge.module;

import dev.amrv.sge.SGE;
import dev.amrv.sge.bbdd.DatabaseErrors;
import dev.amrv.sge.module.providers.ProvidersPanel;
import java.sql.SQLException;
import javax.swing.JPanel;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class ProvidersModule extends Module {

    private SGE sge;
    private ProvidersPanel panel;

    @Override
    protected boolean onLoad(SGE sge) {
        this.sge = sge;
        this.panel = new ProvidersPanel(sge);

        try {
            sge.getDatabase().execute("CREATE TABLE PROVIDER ("
                    + "ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "CIF VARCHAR(32) NOT NULL UNIQUE,"
                    + "NAME VARCHAR(64) NOT NULL"
                    + ")");
            sge.getDatabase().commit();
        } catch (SQLException ex) {
            if (DatabaseErrors.alreadyExists(ex)) {
                sge.logger.info("Table PROVIDER already exists");
            } else {
                sge.logger.error(ex);
                return false;
            }
        }
        try {
            sge.getDatabase().execute("CREATE TABLE PROVIDER_ATTRIBUTE ("
                    + "ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "PROVIDER INT NOT NULL CONSTRAINT PROVIDER_FK REFERENCES PROVIDER(ID) ON DELETE CASCADE,"
                    + "NAME VARCHAR(24) NOT NULL,"
                    + "VALUE VARCHAR(48)"
                    + ")");
            sge.getDatabase().commit();
        } catch (SQLException ex) {
            if (DatabaseErrors.alreadyExists(ex)) {
                sge.logger.info("Table PROVIDER_ATTRIBUTE already exists");
            } else {
                sge.logger.error(ex);
                return false;
            }
        }

        return true;
    }

    @Override
    public void unload() {
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public String getName() {
        return "Proveedores";
    }

    @Override
    public String requirePermission() {
        return "providers.see";
    }

    @Override
    public void onAppear() {
        panel.reloadProviders();
    }

}
