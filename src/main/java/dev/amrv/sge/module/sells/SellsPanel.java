/*
 */
package dev.amrv.sge.module.sells;

import dev.amrv.sge.SGE;
import dev.amrv.sge.SGEFileSystem;
import dev.amrv.sge.module.inventory.InventoryProduct;
import dev.amrv.sge.window.LoadingDialog;
import dev.amrv.sge.window.SGENotifier;
import dev.amrv.sge.window.component.StrictTextField;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.event.TableModelEvent;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class SellsPanel extends javax.swing.JPanel {

    private static final DecimalFormat ECONOMIC_FORMATTER = new DecimalFormat("#,#00.00");
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("d/M/y");

    private final SGE sge;
    private final SellsBillTableModel sellsBillTableModel;
    private final SellsBillTreeModel sellsBillTreeModel;
    private final DefaultListModel<Bill> billsListModel = new DefaultListModel<>();

    public SellsPanel(SGE sge) {
        this.sge = sge;
        sellsBillTreeModel = new SellsBillTreeModel(sge);
        sellsBillTableModel = new SellsBillTableModel();
        sellsBillTableModel.addTableModelListener(this::sellsTableChanged);
        initComponents();
        jTree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        inputDescription.setText(sge.getProperties().getProperty("sells.defaultDescription", ""));
        inputDateMin.setText(DATE_FORMATTER.format(new Date()));
        inputDateMax.setText(DATE_FORMATTER.format(new Date()));

    }

    public void recreateTree() {
        try {
            sellsBillTreeModel.reloadRoot();
        } catch (SQLException ex) {
            SGENotifier.displayError(this, "Error cargando productos", "No se han podido cargar los productos", ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        sellPanel = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new SellsBillTable();
        labelTotalUnits = new javax.swing.JLabel();
        fieldTotalUnits = new javax.swing.JTextField();
        labelProfit = new javax.swing.JLabel();
        fieldProfit = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        inputDescription = new javax.swing.JTextArea();
        buttonConfirm = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        buttonLoad = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        buttonExport = new javax.swing.JButton();
        inputDateMin = new StrictTextField("","\\d{1,2}\\/\\d{1,2}\\/\\d{4}");
        inputDateMax = new StrictTextField("","\\d{1,2}\\/\\d{1,2}\\/\\d{4}");
        inputIgnoreHour = new javax.swing.JCheckBox();

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jSplitPane1.setDividerLocation(220);
        jSplitPane1.setDividerSize(6);

        jTree1.setModel(sellsBillTreeModel);
        jTree1.setRootVisible(false);
        jTree1.setShowsRootHandles(true);
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree1MouseClicked(evt);
            }
        });
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jTree1.addTreeWillExpandListener(new javax.swing.event.TreeWillExpandListener() {
            public void treeWillCollapse(javax.swing.event.TreeExpansionEvent evt)throws javax.swing.tree.ExpandVetoException {
            }
            public void treeWillExpand(javax.swing.event.TreeExpansionEvent evt)throws javax.swing.tree.ExpandVetoException {
                jTree1TreeWillExpand(evt);
            }
        });
        jScrollPane3.setViewportView(jTree1);

        jSplitPane1.setLeftComponent(jScrollPane3);

        jTable1.setModel(sellsBillTableModel);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTable1);

        labelTotalUnits.setText("Unidades totales");

        fieldTotalUnits.setEditable(false);
        fieldTotalUnits.setText("0");

        labelProfit.setText("Beneficio");

        fieldProfit.setEditable(false);
        fieldProfit.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fieldProfit.setText("0");

        jLabel1.setText("Información Adicional");

        inputDescription.setColumns(20);
        inputDescription.setRows(5);
        jScrollPane2.setViewportView(inputDescription);

        buttonConfirm.setText("Confirmar");
        buttonConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonConfirmActionPerformed(evt);
            }
        });

        buttonCancel.setText("Cancelar");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(fieldTotalUnits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(fieldProfit, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(labelTotalUnits)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelProfit))
            .addComponent(jScrollPane2)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(buttonConfirm)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonCancel)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTotalUnits)
                    .addComponent(labelProfit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldTotalUnits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldProfit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonConfirm)
                    .addComponent(buttonCancel)))
        );

        jSplitPane1.setRightComponent(jPanel3);

        javax.swing.GroupLayout sellPanelLayout = new javax.swing.GroupLayout(sellPanel);
        sellPanel.setLayout(sellPanelLayout);
        sellPanelLayout.setHorizontalGroup(
            sellPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sellPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                .addContainerGap())
        );
        sellPanelLayout.setVerticalGroup(
            sellPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sellPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Ventas", sellPanel);

        jLabel2.setText("Fecha inicio");

        jLabel3.setText("Fecha final");

        buttonLoad.setText("Cargar");
        buttonLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLoadActionPerformed(evt);
            }
        });

        jList1.setModel(billsListModel);
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setCellRenderer(new BillListCellRenderer());
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jList1);

        buttonExport.setText("Exportar");
        buttonExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExportActionPerformed(evt);
            }
        });

        inputIgnoreHour.setSelected(true);
        inputIgnoreHour.setText("Ignorar horas");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(inputDateMin, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inputDateMax, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(buttonLoad)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonExport))
                            .addComponent(inputIgnoreHour))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(inputIgnoreHour))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonLoad)
                    .addComponent(buttonExport)
                    .addComponent(inputDateMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputDateMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Historial", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        cleanBill();
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void sellsTableChanged(TableModelEvent e) {
        fieldTotalUnits.setText(sellsBillTableModel.getTotalUnits() + "");
        fieldProfit.setText(ECONOMIC_FORMATTER.format(sellsBillTableModel.getTotalProfit()));
    }

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree1ValueChanged

    }//GEN-LAST:event_jTree1ValueChanged

    private void jTree1TreeWillExpand(javax.swing.event.TreeExpansionEvent evt)throws javax.swing.tree.ExpandVetoException {//GEN-FIRST:event_jTree1TreeWillExpand
        BillTreeNode node = (BillTreeNode) evt.getPath().getLastPathComponent();

        try {
            node.reload(sge.getDatabase());
        } catch (SQLException ex) {
            sge.logger.error(ex);
            SGENotifier.displayError(this, "Error cargando productos", "No se han podido cargar los productos", ex);
        }
    }//GEN-LAST:event_jTree1TreeWillExpand

    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked
        if (evt.getClickCount() < 2)
            return;

        final TreePath path = jTree1.getPathForLocation(evt.getX(), evt.getY());

        if (path == null)
            return;

        BillTreeNode elem = (BillTreeNode) path.getLastPathComponent();

        if (!elem.isCategory()) {
            InventoryProduct product = elem.getAsProduct();
            try {
                BillProduct resultProduct = BillProduct.createLocal(sge.getDatabase(), -1, product.getID(), 1);
                if (resultProduct.getAmount() == 0)
                    SGENotifier.displayError(this, "Error cargando producto", "No hay existencias");
                else
                    sellsBillTableModel.add(resultProduct);
            } catch (SQLException ex) {
                SGENotifier.displayError(this, "Error cargando producto", "No se ha podido calcular el coste", ex);
            }

        }

        jTree1.clearSelection();
    }//GEN-LAST:event_jTree1MouseClicked

    private void buttonConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonConfirmActionPerformed
        LoadingDialog progress = SGENotifier.progress(this, "Creando factura", "Se esta procesando la factura", 10);

        BillProduct[] products = sellsBillTableModel.getBills().toArray(new BillProduct[0]);

        progress.increase(); // Increase progress: 1
        if (products.length == 0) {
            SGENotifier.displayError(this, "Error creando factura", "No se puede crear una factura sin productos");
            progress.abort();
            return;
        }

        // Check existances
        for (BillProduct product : products) {
            int productID = product.getProductID();
            int totalAmount = 0;

            for (BillProduct subproduct : products) {
                if (subproduct.getProductID() == productID)
                    totalAmount += subproduct.getAmount();
            }

            try {
                if (InventoryProduct.get(sge.getDatabase(), productID).getAmount() < totalAmount) {
                    SGENotifier.displayError(this, "Error creando factura", "No hay existencias suficientes del producto " + product.getProductName());
                    progress.abort();
                    return;
                }
            } catch (SQLException ex) {
                SGENotifier.displayError(this, "Error creando factura", "Error cargando informacion del producto " + product.getProductName(), ex);
                progress.abort();
                return;
            }
        }

        progress.increase(); // Increase progress: 2

        BillCreator billCreator = new BillCreator(-1, System.currentTimeMillis());
        sge.getProperties().setProperty("sells.defaultDescription", inputDescription.getText());
        billCreator.setDescription(inputDescription.getText());
        billCreator.setProducts(products);
        BillDataWindow window = new BillDataWindow(this, true);
        window.displayAttributes(billCreator);
        window.setUser(sge.getUser());
        window.displayProperties(sge.getProperties());

        progress.increase(); // Increase progress: 3
        window.setVisible(true);

        window.saveToProperties(sge.getProperties());
        progress.increase(); // Increase progress: 4
        if (window.getResult() == BillDataWindow.RESULT_CANCEL) {
            progress.abort();
            return;
        }

        Bill result;
        try {
            window.applyAttributes(billCreator);
            progress.increase(); // Increase progress: 5
            result = billCreator.build(sge.getDatabase());
            progress.increase(); // Increase progress: 6
        } catch (SQLException ex) {
            SGENotifier.displayError(this, "Error creando factura", "No se ha podido crear la factura", ex);
            progress.abort();
            sge.logger.error(ex);
            return;
        }

        try {
            progress.increase(); // Increase progress: 7
            if (window.getResult() == BillDataWindow.RESULT_GENERATE_SIMPLIFIED)
                BillExporter.export(
                        sge.getDatabase(),
                        new File("facturas", result.getID() + ".txt"),
                        false,
                        result.getID()
                );
            else if (window.getResult() == BillDataWindow.RESULT_GENERATE_NOMINAL)
                BillExporter.export(
                        sge.getDatabase(),
                        new File("facturas", result.getID() + "_nominal.txt"),
                        true,
                        result.getID()
                );
            progress.increase(); // Increase progress: 8
        } catch (SQLException | IOException ex) {
            SGENotifier.displayError(this, "Error creando factura", "No se ha podido exportar la factura", ex);
            sge.logger.error(ex);
            return;
        }

        progress.increase(); // Increase progress: 9
        cleanBill();
        recreateTree();
        progress.increase(); // Increase progress: 10
        SGENotifier.informate(this, "Creacion de factura", "Se ha creado la factura correctamente");
    }//GEN-LAST:event_buttonConfirmActionPerformed

    private void buttonLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLoadActionPerformed
        Date minDate;
        Date maxDate;
        try {
            minDate = DATE_FORMATTER.parse(inputDateMin.getText());
            maxDate = DATE_FORMATTER.parse(inputDateMax.getText());
        } catch (ParseException ex) {
            SGENotifier.displayError(this, "Error de carga", "Las fechas introducidas no tienen un formato valido");
            return;
        }
        List<Bill> billsToDisplay;

        Calendar minDateCalendar = Calendar.getInstance();
        minDateCalendar.setTime(minDate);

        Calendar maxDateCalendar = Calendar.getInstance();
        maxDateCalendar.setTime(maxDate);

        if (inputIgnoreHour.isSelected()) {
            minDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
            minDateCalendar.set(Calendar.MINUTE, 0);
            minDateCalendar.set(Calendar.SECOND, 0);
            minDateCalendar.set(Calendar.MILLISECOND, 0);

            maxDateCalendar.set(Calendar.HOUR_OF_DAY, 23);
            maxDateCalendar.set(Calendar.MINUTE, 59);
            maxDateCalendar.set(Calendar.SECOND, 59);
            maxDateCalendar.set(Calendar.MILLISECOND, 249);
        }

        try {
            billsToDisplay = Bill.getInRange(sge.getDatabase(), new Timestamp(minDateCalendar.getTimeInMillis()), new Timestamp(maxDateCalendar.getTimeInMillis()));
        } catch (SQLException ex) {
            SGENotifier.displayError(this, "Error de carga", "No se han podido recuperar los datos de facturacion", ex);
            return;
        }

        billsListModel.clear();
        if (billsToDisplay.isEmpty()) {
            SGENotifier.displayError(this, "Aviso", "No hay facturas para el rango de fechas especificado");
        }

        billsListModel.ensureCapacity(billsToDisplay.size());
        billsToDisplay.forEach(billsListModel::addElement);
    }//GEN-LAST:event_buttonLoadActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        int index = jList1.locationToIndex(evt.getPoint());

        if (jList1.getCellBounds(index, index) == null)
            return;

        if (!jList1.getCellBounds(index, index).contains(evt.getPoint())) {
            jList1.clearSelection();
            return;
        }

        if (index == -1)
            return;

        if (evt.getClickCount() < 2)
            return;

        if (evt.getButton() != MouseEvent.BUTTON1)
            return;

        Bill bill = billsListModel.get(index);

        BillCreator creator = new BillCreator(bill.getID(), bill.getTimestamp());
        creator.setDescription(bill.getDescription());

        Map<String, BillAttribute> attributeMap;
        try {
            attributeMap = BillAttribute.getAll(sge.getDatabase(), bill.getID());
        } catch (SQLException ex) {
            SGENotifier.displayError(this, "Error cargando factura", "No se han podido cargar los detalles de la factura", ex);
            sge.logger.error(ex);
            return;
        }

        for (Entry<String, BillAttribute> entry : attributeMap.entrySet())
            creator.setAttribute(entry.getKey(), entry.getValue().getValue());

        BillDataWindow window = new BillDataWindow(this, false);
        window.displayAttributes(creator);
        window.setVisible(true);
    }//GEN-LAST:event_jList1MouseClicked

    private void buttonExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExportActionPerformed
        // Exporta solo aquellas que se muestren en pantalla
        LoadingDialog progress = SGENotifier.progress(this, "Exportador de ventas", "Exportando historial de ventas", 5);

        String directoryPath = sge.getProperties().getProperty("sells.exportHistoryDirectory", SGEFileSystem.getSource().getAbsolutePath());

        progress.increase();
        int[] billIDs = new int[billsListModel.size()];

        if (billIDs.length == 0) {
            progress.abort();
            SGENotifier.displayError(this, "Exportador de ventas", "No hay facturas para exportar");
            return;
        }
        progress.increase();

        File directory = SGENotifier.requestFileSaveDirectory(this, "Selecciona una carpeta en la que guardar la exportacion", new File(directoryPath));

        progress.increase();

        if (directory == null) {
            progress.abort();
            return;
        }

        sge.getProperties().setProperty("sells.exportHistoryDirectory", directory.getAbsolutePath());

        File file = new File(directory, "facturas_" + inputDateMin.getText().replace('/', '-') + "_" + inputDateMax.getText().replace('/', '-') + ".txt");

        progress.increase();
        for (int i = 0; i < billIDs.length; i++) {
            billIDs[i] = billsListModel.elementAt(i).getID();
        }

        try {
            BillExporter.export(sge.getDatabase(), file, true, billIDs);
        } catch (SQLException | IOException ex) {
            SGENotifier.displayError(this, "Exportador de ventas", "Ha habido un error exportando las facturas seleccionadas", ex);
            sge.logger.error(ex);
            return;
        }

        progress.complete();
        SGENotifier.informate(this, "Exportador de ventas", "Se ha exportado la informacion al fichero:\n" + file.getAbsolutePath());
    }//GEN-LAST:event_buttonExportActionPerformed

    public void cleanBill() {
        fieldProfit.setText("0");
        fieldTotalUnits.setText("0");
        inputDescription.setText(sge.getProperties().getProperty("sells.defaultDescription", ""));
        sellsBillTableModel.clear();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonConfirm;
    private javax.swing.JButton buttonExport;
    private javax.swing.JButton buttonLoad;
    private javax.swing.JTextField fieldProfit;
    private javax.swing.JTextField fieldTotalUnits;
    private javax.swing.JTextField inputDateMax;
    private javax.swing.JTextField inputDateMin;
    private javax.swing.JTextArea inputDescription;
    private javax.swing.JCheckBox inputIgnoreHour;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<Bill> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTree jTree1;
    private javax.swing.JLabel labelProfit;
    private javax.swing.JLabel labelTotalUnits;
    private javax.swing.JPanel sellPanel;
    // End of variables declaration//GEN-END:variables
}
