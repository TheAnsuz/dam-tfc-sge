package dev.amrv.sge.module.inventory;

import dev.amrv.sge.module.inventory.model.AttributeTuple;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class ProductTableModel extends AbstractTableModel {

    private final List<AttributeTuple> attributes = new ArrayList<>();

    public void clear() {
        attributes.clear();
        fireTableDataChanged();
    }

    public List<AttributeTuple> getAll() {
        return attributes;
    }

    public synchronized void add(AttributeTuple tuple) {
        int index = attributes.indexOf(tuple);

        if (index == -1) {
            index = attributes.size();
            attributes.add(index, tuple);
            fireTableRowsInserted(index, index);
        } else {
            if (attributes.get(index).accepts(tuple.getValue())) {
                attributes.get(index).setValue(tuple.getValue());
                fireTableRowsUpdated(index, index);
            }
        }

    }

    public synchronized void add(String key, String value) {
        add(new AttributeTuple(key, value));
    }

    public synchronized void add(String key) {
        add(new AttributeTuple(key));
    }

    public synchronized void remove(InventoryAttribute attribute) {
        int index = attributes.indexOf(attribute);

        if (index != -1)
            remove(index);
    }

    public synchronized void remove(int index) {
        attributes.remove(index);
        fireTableRowsDeleted(index, index);
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Clave";
            case 1:
                return "Valor";
            default:
                return "???";
        }
    }

    @Override
    public int getRowCount() {
        return attributes.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return attributes.get(rowIndex).getKey();
            case 1:
                return attributes.get(rowIndex).getValue();
            default:
                return "";
        }
        // Necesitas sacar la key y el value de cada fila. También deberían de poder modificarse los values (las keys no, solo añadir o eliminar la key y crear otra)
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        AttributeTuple tuple = attributes.get(rowIndex);

        if (tuple == null)
            return;

        if (tuple.accepts(aValue.toString()))
            tuple.setValue(aValue.toString());

        super.setValueAt(aValue, rowIndex, columnIndex);
    }

}
