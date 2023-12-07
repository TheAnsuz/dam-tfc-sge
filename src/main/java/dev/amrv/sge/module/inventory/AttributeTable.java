package dev.amrv.sge.module.inventory;

import javax.swing.JTable;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class AttributeTable extends JTable {

    public AttributeTable() {
        super();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 0;
    }

}
