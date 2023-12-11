package dev.amrv.sge.module.sells;

import dev.amrv.sge.SGE;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class SellsBillTableModel extends AbstractTableModel {

    private static final DecimalFormat ECONOMIC_FORMATTER = new DecimalFormat("#,#00.00");

    private final List<BillProduct> bills = new ArrayList<>();

    public SellsBillTableModel() {
    }

    public List<BillProduct> getBills() {
        return bills;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Producto";
            case 1:
                return "Unidades";
            case 2:
                return "Coste bruto";
            case 3:
                return "Precio venta";
            case 4:
                return "Beneficio";
            default:
                return "???";
        }
    }

    public void clear() {
        int last = bills.size();
        bills.clear();
        fireTableRowsDeleted(0, last);
    }

    public void add(BillProduct bill) {
        int index = bills.size();
        add(index, bill);
    }

    public void add(int index, BillProduct bill) {
        bills.add(index, bill);
        fireTableRowsInserted(index, index);
    }

    public void remove(int index) {
        bills.remove(index);
        fireTableRowsDeleted(index, index);
    }

    @Override
    public int getRowCount() {
        return bills.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return bills.get(rowIndex).getProductName();
            case 1:
                return bills.get(rowIndex).getAmount();
            case 2:
                return ECONOMIC_FORMATTER.format(bills.get(rowIndex).getFinalCost());
            case 3:
                return ECONOMIC_FORMATTER.format(bills.get(rowIndex).getFinalPrice());
            case 4:
                return ECONOMIC_FORMATTER.format(bills.get(rowIndex).getFinalProfit());
            default:
                return "???";
        }

    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        float numValue;
        try {
            numValue = ECONOMIC_FORMATTER.parse(aValue.toString()).floatValue();
        } catch (ParseException nfe) {
            return;
        }

        switch (columnIndex) {
            case 1:
                // Modificar cantidad
                int amount = (int) numValue;
                if (amount == 0) {
                    bills.remove(rowIndex);
                    fireTableRowsDeleted(rowIndex, rowIndex);
                } else {
                    bills.get(rowIndex).setAmount(amount);
                    fireTableRowsUpdated(rowIndex, rowIndex);
                }
                break;
            case 2:
                // Modificar coste
                bills.get(rowIndex).setFinalCost(numValue);
                fireTableRowsUpdated(rowIndex, rowIndex);
                break;
            case 3:
                // Modificar precio
                bills.get(rowIndex).setFinalPrice(numValue);
                fireTableRowsUpdated(rowIndex, rowIndex);
                break;
            default:
                break;
        }
    }

    public int getTotalUnits() {
        int total = 0;
        for (BillProduct product : bills)
            total += product.getAmount();
        return total;
    }

    public float getTotalProfit() {
        float profit = 0;
        for (BillProduct product : bills)
            profit += product.getFinalProfit();
        return profit;
    }

}
