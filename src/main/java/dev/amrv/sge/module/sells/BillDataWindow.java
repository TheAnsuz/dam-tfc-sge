/*
 */
package dev.amrv.sge.module.sells;

import dev.amrv.sge.auth.UserCredentials;
import dev.amrv.sge.io.PropertiesFile;
import dev.amrv.sge.window.SGENotifier;
import java.awt.Component;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class BillDataWindow extends javax.swing.JDialog {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    private static final String ID_FORMAT = "%010d";

    public static final int RESULT_CANCEL = 0;
    public static final int RESULT_GENERATE_SIMPLIFIED = 1;
    public static final int RESULT_GENERATE_NOMINAL = 2;

    private final ComboBoxModel<String> paymentMethodModel = new DefaultComboBoxModel<>(new String[]{"Tarjeta", "Efectivo", "Transferencia"});

    private int result = 0;

    /**
     * Creates new form BillDataWindow
     *
     * @param parent
     * @param editable
     */
    public BillDataWindow(Component parent, boolean editable) {
        super();
        super.setModal(true);
        initComponents();
        if (parent != null) {
            super.setLocationRelativeTo(parent);
        }
        inputDescription.setColumns(BillExporter.MAX_WIDTH_CHARS);
        setEditable(editable);
    }

    public void setUser(UserCredentials credentials) {
        if (credentials == null)
            return;

        inputEmitterName.setText(credentials.getUsername());
    }

    public void setEditable(boolean editable) {
        inputPaymentMethod.setEnabled(editable);
        inputDescription.setEditable(editable);

        inputEmitterCIF.setEditable(editable);
        inputEmitterCompany.setEditable(editable);
        inputEmitterMail.setEditable(editable);
        inputEmitterName.setEditable(editable);
        inputEmitterPhone.setEditable(editable);
        inputEmitterPostalCode.setEditable(editable);
        inputEmitterProvince.setEditable(editable);
        inputEmitterStreet.setEditable(editable);

        inputReceiverCIF.setEditable(editable);
        inputReceiverMail.setEditable(editable);
        inputReceiverName.setEditable(editable);
        inputReceiverPhone.setEditable(editable);
        inputReceiverPostalCode.setEditable(editable);
        inputReceiverProvince.setEditable(editable);
        inputReceiverStreet.setEditable(editable);

        buttonCreateNominative.setEnabled(editable);
        buttonCreateSimplified.setEnabled(editable);
    }

    public void displayProperties(PropertiesFile properties) {
        inputEmitterCIF.setText(properties.getProperty("sells.defaultCIF", inputEmitterCIF.getText()));
        inputEmitterCompany.setText(properties.getProperty("sells.defaultCompany", inputEmitterCompany.getText()));
        inputEmitterMail.setText(properties.getProperty("sells.defaultMail", inputEmitterMail.getText()));
        inputEmitterPhone.setText(properties.getProperty("sells.defaultPhone", inputEmitterPhone.getText()));
        inputEmitterPostalCode.setText(properties.getProperty("sells.defaultPostalCode", inputEmitterPostalCode.getText()));
        inputEmitterProvince.setText(properties.getProperty("sells.defaultProvince", inputEmitterProvince.getText()));
        inputEmitterStreet.setText(properties.getProperty("sells.defaultStreet", inputEmitterStreet.getText()));
    }

    public void saveToProperties(PropertiesFile properties) {
        if (!inputEmitterCIF.getText().isEmpty())
            properties.setProperty("sells.defaultCIF", inputEmitterCIF.getText());

        if (!inputEmitterCompany.getText().isEmpty())
            properties.setProperty("sells.defaultCompany", inputEmitterCompany.getText());

        if (!inputEmitterMail.getText().isEmpty())
            properties.setProperty("sells.defaultMail", inputEmitterMail.getText());

        if (!inputEmitterPhone.getText().isEmpty())
            properties.setProperty("sells.defaultPhone", inputEmitterPhone.getText());

        if (!inputEmitterPostalCode.getText().isEmpty())
            properties.setProperty("sells.defaultPostalCode", inputEmitterPostalCode.getText());

        if (!inputEmitterProvince.getText().isEmpty())
            properties.setProperty("sells.defaultProvince", inputEmitterProvince.getText());

        if (!inputEmitterStreet.getText().isEmpty())
            properties.setProperty("sells.defaultStreet", inputEmitterStreet.getText());
    }

    public void displayAttributes(BillCreator creator) {
        // Bill information
        inputBillID.setText(String.format(ID_FORMAT, creator.getBillID() == -1 ? 0 : creator.getBillID()));
        inputTimestamp.setText(DATE_FORMATTER.format(new Timestamp(creator.getTimestamp())));

        inputPaymentMethod.setSelectedItem(-1);
        
        if (creator.getAttribute("payment_method") != null) {
            String attr = creator.getAttribute("payment_method");

            for (int i = 0; i < paymentMethodModel.getSize(); i++) {
                if (attr.equals(paymentMethodModel.getElementAt(i))) {
                    inputPaymentMethod.setSelectedIndex(i);
                    break;
                }
            }
        }

        // Emitter information
        inputEmitterCIF.setText(creator.getAttribute("emitter_cif"));
        inputEmitterCompany.setText(creator.getAttribute("emitter_company"));
        inputEmitterMail.setText(creator.getAttribute("emitter_mail"));
        inputEmitterName.setText(creator.getAttribute("emitter_name"));
        inputEmitterPhone.setText(creator.getAttribute("emitter_phone"));
        inputEmitterPostalCode.setText(creator.getAttribute("emitter_postal_code"));
        inputEmitterProvince.setText(creator.getAttribute("emitter_province"));
        inputEmitterStreet.setText(creator.getAttribute("emitter_street"));

        // Receiver information
        inputReceiverCIF.setText(creator.getAttribute("receiver_cif"));
        inputReceiverMail.setText(creator.getAttribute("receiver_mail"));
        inputReceiverName.setText(creator.getAttribute("receiver_name"));
        inputReceiverPhone.setText(creator.getAttribute("receiver_phone"));
        inputReceiverPostalCode.setText(creator.getAttribute("receiver_postal_code"));
        inputReceiverProvince.setText(creator.getAttribute("receiver_province"));
        inputReceiverStreet.setText(creator.getAttribute("receiver_street"));

        // Aditional info
        inputDescription.setText(creator.getDescription());
    }

    public void applyAttributes(BillCreator creator) {
        // Billing information
        creator.setAttribute("payment_method", paymentMethodModel.getSelectedItem().toString());

        // Emitter information
        creator.setAttribute("emitter_cif", inputEmitterCIF.getText());
        creator.setAttribute("emitter_company", inputEmitterCompany.getText());
        creator.setAttribute("emitter_mail", inputEmitterMail.getText());
        creator.setAttribute("emitter_name", inputEmitterName.getText());
        creator.setAttribute("emitter_phone", inputEmitterPhone.getText());
        creator.setAttribute("emitter_postal_code", inputEmitterPostalCode.getText());
        creator.setAttribute("emitter_province", inputEmitterProvince.getText());
        creator.setAttribute("emitter_street", inputEmitterStreet.getText());

        // Receiver information
        creator.setAttribute("receiver_cif", inputReceiverCIF.getText());
        creator.setAttribute("receiver_mail", inputReceiverMail.getText());
        creator.setAttribute("receiver_name", inputReceiverName.getText());
        creator.setAttribute("receiver_phone", inputReceiverPhone.getText());
        creator.setAttribute("receiver_postal_code", inputReceiverPostalCode.getText());
        creator.setAttribute("receiver_province", inputReceiverProvince.getText());
        creator.setAttribute("receiver_street", inputReceiverStreet.getText());

        creator.setDescription(inputDescription.getText());
    }

    public boolean isValidInput() {
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelBill = new javax.swing.JLabel();
        inputBillID = new javax.swing.JTextField();
        inputTimestamp = new javax.swing.JTextField();
        labelEmitter = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator1 = new javax.swing.JSeparator();
        labelEmitterName = new javax.swing.JLabel();
        inputEmitterName = new javax.swing.JTextField();
        labelEmitterCompany = new javax.swing.JLabel();
        inputEmitterCompany = new javax.swing.JTextField();
        inputEmitterStreet = new javax.swing.JTextField();
        inputEmitterProvince = new javax.swing.JTextField();
        labelEmitterStreet = new javax.swing.JLabel();
        labelEmitterProvince = new javax.swing.JLabel();
        labelEmitterPostalCode = new javax.swing.JLabel();
        inputEmitterPostalCode = new javax.swing.JTextField();
        labelEmitterCIF = new javax.swing.JLabel();
        inputEmitterCIF = new javax.swing.JTextField();
        labelEmitterPhone = new javax.swing.JLabel();
        inputEmitterPhone = new javax.swing.JTextField();
        inputEmitterMail = new javax.swing.JTextField();
        labelEmitterMail = new javax.swing.JLabel();
        labelReceiver = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        labelReceiverName = new javax.swing.JLabel();
        inputReceiverName = new javax.swing.JTextField();
        inputReceiverPhone = new javax.swing.JTextField();
        inputReceiverMail = new javax.swing.JTextField();
        labelReceiverPhone = new javax.swing.JLabel();
        labelReceiverMail = new javax.swing.JLabel();
        labelReceiverStreet = new javax.swing.JLabel();
        inputReceiverStreet = new javax.swing.JTextField();
        inputReceiverProvince = new javax.swing.JTextField();
        inputReceiverPostalCode = new javax.swing.JTextField();
        inputReceiverCIF = new javax.swing.JTextField();
        labelReceiverProvince = new javax.swing.JLabel();
        labelReceiverPostalCode = new javax.swing.JLabel();
        labelReceiverCIF = new javax.swing.JLabel();
        labelPaymentMethod = new javax.swing.JLabel();
        inputPaymentMethod = new javax.swing.JComboBox<>();
        labelReceiverAdditionalInfo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        inputDescription = new javax.swing.JTextArea();
        buttonCreateNominative = new javax.swing.JButton();
        buttonCreateSimplified = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(473, 579));

        labelBill.setText("Factura");

        inputBillID.setEditable(false);
        inputBillID.setText("0000000000");

        inputTimestamp.setEditable(false);
        inputTimestamp.setText("dd/MM/yyyy");

        labelEmitter.setText("Emisor");

        labelEmitterName.setText("Persona");

        labelEmitterCompany.setText("Empresa");

        inputEmitterStreet.setText("jTextField1");

        inputEmitterProvince.setText("jTextField2");

        labelEmitterStreet.setText("Calle");

        labelEmitterProvince.setText("Provincia");

        labelEmitterPostalCode.setText("Código postal");

        inputEmitterPostalCode.setText("jTextField1");

        labelEmitterCIF.setText("CIF");

        inputEmitterCIF.setText("jTextField1");

        labelEmitterPhone.setText("Telefono");

        inputEmitterPhone.setText("jTextField1");

        inputEmitterMail.setText("jTextField1");

        labelEmitterMail.setText("Correo");

        labelReceiver.setText("Receptor");

        labelReceiverName.setText("Nombre");

        inputReceiverName.setText("jTextField1");

        inputReceiverPhone.setText("jTextField2");

        inputReceiverMail.setText("jTextField3");

        labelReceiverPhone.setText("Telefono");

        labelReceiverMail.setText("Correo");

        labelReceiverStreet.setText("Calle");

        inputReceiverStreet.setText("jTextField1");

        inputReceiverProvince.setText("jTextField2");

        inputReceiverPostalCode.setText("jTextField3");

        inputReceiverCIF.setText("jTextField4");

        labelReceiverProvince.setText("Provincia");

        labelReceiverPostalCode.setText("Código postal");

        labelReceiverCIF.setText("CIF");

        labelPaymentMethod.setText("Forma de pago");

        inputPaymentMethod.setModel(paymentMethodModel);

        labelReceiverAdditionalInfo.setText("Informacion adicional");

        inputDescription.setColumns(20);
        inputDescription.setRows(5);
        jScrollPane1.setViewportView(inputDescription);

        buttonCreateNominative.setText("Crear factura nominativa");
        buttonCreateNominative.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCreateNominativeActionPerformed(evt);
            }
        });

        buttonCreateSimplified.setText("Crear factura simplificada");
        buttonCreateSimplified.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCreateSimplifiedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelEmitterStreet)
                            .addComponent(inputEmitterStreet, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelEmitterProvince)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(inputEmitterProvince)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(inputEmitterPostalCode, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelEmitterPostalCode))
                                .addGap(71, 71, 71))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelBill)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator2))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(inputBillID, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(inputTimestamp, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(labelEmitter)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelReceiver)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator3))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelReceiverName)
                                    .addComponent(inputReceiverName, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelReceiverPhone)
                                    .addComponent(inputReceiverPhone))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(labelReceiverMail)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(inputReceiverMail)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(inputReceiverStreet)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(labelReceiverStreet)
                                        .addGap(166, 166, 166)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelReceiverProvince)
                                    .addComponent(inputReceiverProvince))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelReceiverPostalCode)
                                    .addComponent(inputReceiverPostalCode))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelReceiverCIF)
                                    .addComponent(inputReceiverCIF)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(inputEmitterPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelEmitterMail)
                                    .addComponent(inputEmitterMail, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelPaymentMethod)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(inputEmitterName, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelEmitterName))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelEmitterCompany)
                                    .addComponent(inputEmitterCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelEmitterCIF)
                                    .addComponent(inputEmitterCIF, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelEmitterPhone)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelReceiverAdditionalInfo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator4)))
                        .addGap(6, 6, 6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(buttonCreateSimplified)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonCreateNominative)
                        .addGap(135, 135, 135))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelBill)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputBillID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputTimestamp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPaymentMethod)
                    .addComponent(inputPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelEmitter)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelEmitterName)
                    .addComponent(labelEmitterCompany)
                    .addComponent(labelEmitterCIF))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputEmitterName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputEmitterCompany, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputEmitterCIF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelEmitterPhone)
                    .addComponent(labelEmitterMail))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputEmitterMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputEmitterPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelEmitterStreet)
                    .addComponent(labelEmitterProvince)
                    .addComponent(labelEmitterPostalCode))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputEmitterStreet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputEmitterProvince, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputEmitterPostalCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelReceiver)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelReceiverName)
                    .addComponent(labelReceiverPhone)
                    .addComponent(labelReceiverMail))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputReceiverName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputReceiverPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputReceiverMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelReceiverStreet)
                    .addComponent(labelReceiverProvince)
                    .addComponent(labelReceiverPostalCode)
                    .addComponent(labelReceiverCIF))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inputReceiverStreet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inputReceiverProvince, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inputReceiverPostalCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inputReceiverCIF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labelReceiverAdditionalInfo))
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonCreateNominative)
                    .addComponent(buttonCreateSimplified))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public int getResult() {
        return result;
    }

    private void buttonCreateSimplifiedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCreateSimplifiedActionPerformed
        if (!isValidInput()) {
            SGENotifier.displayError(this, "Error de datos", "La informacion de la catura no es correcta");
            return;
        }
        result = RESULT_GENERATE_SIMPLIFIED;
        this.setVisible(false);
    }//GEN-LAST:event_buttonCreateSimplifiedActionPerformed

    private void buttonCreateNominativeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCreateNominativeActionPerformed
        if (!isValidInput()) {
            SGENotifier.displayError(this, "Error de datos", "La informacion de la catura no es correcta");
            return;
        }
        result = RESULT_GENERATE_NOMINAL;
        this.setVisible(false);
    }//GEN-LAST:event_buttonCreateNominativeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCreateNominative;
    private javax.swing.JButton buttonCreateSimplified;
    private javax.swing.JTextField inputBillID;
    private javax.swing.JTextArea inputDescription;
    private javax.swing.JTextField inputEmitterCIF;
    private javax.swing.JTextField inputEmitterCompany;
    private javax.swing.JTextField inputEmitterMail;
    private javax.swing.JTextField inputEmitterName;
    private javax.swing.JTextField inputEmitterPhone;
    private javax.swing.JTextField inputEmitterPostalCode;
    private javax.swing.JTextField inputEmitterProvince;
    private javax.swing.JTextField inputEmitterStreet;
    private javax.swing.JComboBox<String> inputPaymentMethod;
    private javax.swing.JTextField inputReceiverCIF;
    private javax.swing.JTextField inputReceiverMail;
    private javax.swing.JTextField inputReceiverName;
    private javax.swing.JTextField inputReceiverPhone;
    private javax.swing.JTextField inputReceiverPostalCode;
    private javax.swing.JTextField inputReceiverProvince;
    private javax.swing.JTextField inputReceiverStreet;
    private javax.swing.JTextField inputTimestamp;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel labelBill;
    private javax.swing.JLabel labelEmitter;
    private javax.swing.JLabel labelEmitterCIF;
    private javax.swing.JLabel labelEmitterCompany;
    private javax.swing.JLabel labelEmitterMail;
    private javax.swing.JLabel labelEmitterName;
    private javax.swing.JLabel labelEmitterPhone;
    private javax.swing.JLabel labelEmitterPostalCode;
    private javax.swing.JLabel labelEmitterProvince;
    private javax.swing.JLabel labelEmitterStreet;
    private javax.swing.JLabel labelPaymentMethod;
    private javax.swing.JLabel labelReceiver;
    private javax.swing.JLabel labelReceiverAdditionalInfo;
    private javax.swing.JLabel labelReceiverCIF;
    private javax.swing.JLabel labelReceiverMail;
    private javax.swing.JLabel labelReceiverName;
    private javax.swing.JLabel labelReceiverPhone;
    private javax.swing.JLabel labelReceiverPostalCode;
    private javax.swing.JLabel labelReceiverProvince;
    private javax.swing.JLabel labelReceiverStreet;
    // End of variables declaration//GEN-END:variables
}
