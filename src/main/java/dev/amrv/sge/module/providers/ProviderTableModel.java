package dev.amrv.sge.module.providers;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public final class ProviderTableModel extends AbstractTableModel {

    private final List<Provider> rows = new ArrayList<>();
    private final List<Map<String, ProviderAttribute>> rowAttributes = new ArrayList<>();
    private final String[] columns;
    private final JTable table;
    private int selectedRow;

    public ProviderTableModel(JTable table) {
        this.columns = new String[]{"ID", "CIF", "Nombre"};
        this.table = table;
        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onMousePressed(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());

                if (row == -1 || row != selectedRow)
                    table.clearSelection();
            }

        });
        table.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                return super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
            }

        });
    }

    public int getPopupSelection() {
        return selectedRow;
    }

    public synchronized void addProvider(Provider provider, Map<String, ProviderAttribute> attributes) {
        int index = rows.size();

        for (int i = 0; i < rows.size(); i++) {
            if (provider.getID() == rows.get(i).getID()) {
                removeProvider(i);
                index = i;
                break;
            }
        }

        rows.add(index, provider);
        rowAttributes.add(index, attributes);

        fireTableRowsInserted(index, index);
    }

    public synchronized void removeProvider(Provider provider) {
        int index = rows.indexOf(rows);

        if (index == -1)
            return;

        removeProvider(index);
    }

    public synchronized void removeProvider(int index) {
        rows.remove(index);
        rowAttributes.remove(index);

        fireTableRowsDeleted(index, index);
    }

    public Provider getProviderAt(int index) {
        return rows.get(index);
    }

    public Map<String, ProviderAttribute> getAttributesAt(int index) {
        return rowAttributes.get(index);
    }

    public void clear() {
        rows.clear();
        rowAttributes.clear();
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return getValueForColumn(rows.get(rowIndex), columnIndex);
    }

    protected String getValueForColumn(Provider provider, int column) {
        switch (column) {
            case 0:
                return provider.getID() + "";
            case 1:
                return provider.CIF();
            case 2:
                return provider.getName();
            default:
                return "";
        }
    }

    public void onMousePressed(MouseEvent e) {
        int row = table.rowAtPoint(e.getPoint());

        selectedRow = -1;
        table.clearSelection();

        if (!SwingUtilities.isRightMouseButton(e))
            return;

        selectedRow = row;

        table.addRowSelectionInterval(row, row);
    }

}
