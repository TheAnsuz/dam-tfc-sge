package dev.amrv.sge.module;

import dev.amrv.sge.SGE;
import dev.amrv.sge.bbdd.Database;
import dev.amrv.sge.bbdd.DatabaseErrors;
import dev.amrv.sge.module.inventory.InventoryPanel;
import java.sql.SQLException;
import javax.swing.JPanel;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class InventoryModule extends Module {

    private static final String NAME = "Inventario";

    private InventoryPanel panel;
    private SGE sge;
    private Database database;

    @Override
    public boolean onLoad(SGE sge) {
        this.sge = sge;
        this.database = sge.getDatabase();
        panel = new InventoryPanel(sge);
        panel.setName(NAME);

        // Esquema
        try {
            database.executeUpdate("CREATE SCHEMA INVENTORY");
            database.commit();
        } catch (SQLException ex) {
            if (DatabaseErrors.alreadyExists(ex)) {
                sge.logger.info("Schema INVENTORY already exists");
            } else {
                sge.logger.error(ex);
                return false;
            }
        }

        // Tabla de categorias
        try {
            database.executeUpdate("CREATE TABLE INVENTORY.CATEGORY ("
                    + "ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "PARENT INT REFERENCES INVENTORY.CATEGORY(ID),"
                    + "NAME VARCHAR(32) NOT NULL"
                    + ")");
            database.commit();
        } catch (SQLException ex) {
            if (DatabaseErrors.alreadyExists(ex)) {
                sge.logger.info("Table INVENTORY.CATEGORY already exists");
            } else {
                sge.logger.error(ex);
                return false;
            }
        }

        // Tabla de productos
        try {
            database.executeUpdate("CREATE TABLE INVENTORY.PRODUCT ("
                    + "ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "CATEGORY INT REFERENCES INVENTORY.CATEGORY(ID) NOT NULL,"
                    + "NAME VARCHAR(64) NOT NULL,"
                    + "PROVIDER INT REFERENCES PROVIDER(ID) NOT NULL,"
                    + "AMOUNT INT NOT NULL"
                    + ")");
            database.commit();
        } catch (SQLException ex) {
            if (DatabaseErrors.alreadyExists(ex)) {
                sge.logger.info("Table INVENTORY.PRODUCT already exists");
            } else {
                sge.logger.error(ex);
                return false;
            }
        }

        // Tabla de atributos
        try {
            database.executeUpdate("CREATE TABLE INVENTORY.PRODUCT_ATTRIBUTE ("
                    + "ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "PRODUCT INT NOT NULL CONSTRAINT PRODUCT_FK REFERENCES INVENTORY.PRODUCT(ID) ON DELETE CASCADE,"
                    + "NAME VARCHAR(24) NOT NULL,"
                    + "VALUE VARCHAR(48),"
                    + "CONSTRAINT UNIQUE_PRODUCT_ATTRIBUTE UNIQUE (PRODUCT,NAME)"
                    + ")");
            database.commit();
        } catch (SQLException ex) {
            if (DatabaseErrors.alreadyExists(ex)) {
                sge.logger.info("Table INVENTORY.PRODUCT_ATTRIBUTE already exists");
            } else {
                sge.logger.error(ex);
                return false;
            }
        }

        return true;
    }

    public SGE getSge() {
        return sge;
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
        return NAME;
    }

    public Database getDatabase() {
        return database;
    }

    @Override
    public String requirePermission() {
        return "inventory.see";
    }

    @Override
    public void onAppear() {
        panel.recreateTree();
    }

}
