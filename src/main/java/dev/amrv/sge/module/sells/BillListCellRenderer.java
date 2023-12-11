package dev.amrv.sge.module.sells;

import java.awt.Component;
import java.awt.Font;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class BillListCellRenderer extends DefaultListCellRenderer {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    private static final String ID_FORMAT = "%010d";

    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof Bill) {
            Bill bill = (Bill) value;
            this.setText(
                    "Factura: " + String.format(ID_FORMAT, bill.getID())
                    + " Fecha: " + DATE_FORMATTER.format(new Timestamp(bill.getTimestamp()))
                    + " ( " + bill.getDescription() + " )");
        }

        if (isSelected)
            this.setFont(this.getFont().deriveFont(Font.BOLD));
        else
            this.setFont(this.getFont().deriveFont(Font.PLAIN));

        return this;
    }

}
