package dev.amrv.sge.module.sells;

import javax.swing.JTable;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class SellsBillTable extends JTable {

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 1 || column == 2 || column == 3;
    }
}
