package dev.amrv.sge.module;

import dev.amrv.sge.SGE;
import dev.amrv.sge.bbdd.Database;
import dev.amrv.sge.bbdd.DatabaseErrors;
import dev.amrv.sge.module.sells.SellsPanel;
import java.sql.SQLException;
import javax.swing.JPanel;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class SellModule extends Module {

    private static final String NAME = "Ventas";

    private SellsPanel panel;
    private Database database;

    @Override
    public boolean onLoad(SGE sge) {
        panel = new SellsPanel(sge);
        panel.setName(NAME);
        database = sge.getDatabase();

        try {
            database.executeUpdate("CREATE SCHEMA SELLS");
            database.commit();
        } catch (SQLException ex) {
            if (DatabaseErrors.alreadyExists(ex)) {
                sge.logger.info("Schema SELLS already exists");
            } else {
                sge.logger.error(ex);
                return false;
            }
        }
        
        try {
            database.executeUpdate("CREATE TABLE SELLS.BILL ("
                    + "ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "DATE TIMESTAMP NOT NULL,"
                    + "DESCRIPTION VARCHAR(1024)"
                    + ")");
            database.commit();
        } catch (SQLException ex) {
            if (DatabaseErrors.alreadyExists(ex)) {
                sge.logger.info("Table SELLS.BILL already exists");
            } else {
                sge.logger.error(ex);
                return false;
            }
        }
        
        try {
            database.executeUpdate("CREATE TABLE SELLS.BILL_PRODUCT ("
                    + "ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "BILL_ID INT NOT NULL CONSTRAINT BILL_PRODUCT_BILL_FK REFERENCES SELLS.BILL(ID) ON DELETE CASCADE,"
                    + "PRODUCT_ID INT CONSTRAINT BILL_PRODUCT_PRODUCT_FK REFERENCES INVENTORY.PRODUCT(ID) ON DELETE SET NULL,"
                    + "PRODUCT_NAME VARCHAR(64) NOT NULL,"
                    + "AMOUNT INT NOT NULL,"
                    + "COST FLOAT NOT NULL,"
                    + "EARN FLOAT NOT NULL"
                    + ")");
            database.commit();
        } catch (SQLException ex) {
            if (DatabaseErrors.alreadyExists(ex)) {
                sge.logger.info("Table SELLS.BILL_PRODUCT already exists");
            } else {
                sge.logger.error(ex);
                return false;
            }
        }
        
        // Tabla de atributos
        try {
            database.executeUpdate("CREATE TABLE SELLS.BILL_ATTRIBUTE ("
                    + "ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "BILL INT NOT NULL CONSTRAINT BILL_ATTRIUBTE_BILL_FK REFERENCES SELLS.BILL(ID) ON DELETE CASCADE,"
                    + "NAME VARCHAR(24) NOT NULL,"
                    + "VALUE VARCHAR(48),"
                    + "CONSTRAINT BILL_ATTRIUBTE_UNIQUE_NAME UNIQUE (BILL,NAME)"
                    + ")");
            database.commit();
        } catch (SQLException ex) {
            if (DatabaseErrors.alreadyExists(ex)) {
                sge.logger.info("Table SELLS.BILL_ATTRIBUTE already exists");
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
        return NAME;
    }

    @Override
    public String requirePermission() {
        return "sell.see";
    }

    @Override
    public void onAppear() {
        panel.recreateTree();
    }

}
