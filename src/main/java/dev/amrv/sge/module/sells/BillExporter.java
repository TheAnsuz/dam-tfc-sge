package dev.amrv.sge.module.sells;

import dev.amrv.sge.bbdd.Database;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class BillExporter {

    public static final int MAX_WIDTH_CHARS = 55;
    public static final int MAX_SPACE_DISPLACEMENT = 12;
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");
    public static final DecimalFormat INTEGER_FORMAT = new DecimalFormat("#,##0");
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final String PRODUCT_FORMAT = "%-8s %-25s %-4s %-7s %7s";
    public static final String PAYMENT_FORMAT = "%-30s %24s";
    public static final String BILL_FORMAT = "FRA SIMP: %010d                  FECHA: %10s";
    public static final String ATTENDED_FORMAT = "ATENDIDO POR: %41s";
    public static final String LEFT_JUSTIFIED = "%-" + MAX_WIDTH_CHARS + "s";
    public static final String RIGHT_JUSTIFIED = "%" + MAX_WIDTH_CHARS + "s";
    public static final String BOTH_JUSTIFIED = "%-" + (MAX_WIDTH_CHARS / 2) + "s" + (MAX_WIDTH_CHARS % 2 == 0 ? "" : " ") + "%" + (MAX_WIDTH_CHARS / 2) + "s";

    public static void export(Database database, File file, boolean detailed, int... billIDs) throws SQLException, IOException {
        for (int billID : billIDs) {
            BillExporter exporter = new BillExporter(billID, detailed);
            exporter.gatherData(database);
            exporter.createContent();
            exporter.writeToFile(file);
        }
    }

    private final int billID;
    private Bill bill;
    private Map<String, BillAttribute> attributes;
    private List<BillProduct> products;
    private final boolean detailed;
    private List<String> lines = new ArrayList<>();

    private BillExporter(int billID, boolean detailed) {
        this.billID = billID;
        this.detailed = detailed;
    }

    public void gatherData(Database database) throws SQLException {
        bill = Bill.get(database, billID);
        attributes = BillAttribute.getAll(database, billID);
        products = BillProduct.getAll(database, billID);
    }

    public void createContent() {
        lines.clear();

        // Informacion de emisor
        lines.add(StringUtils.center(getAttr("emitter_street"), MAX_WIDTH_CHARS));
        lines.add(StringUtils.center(getAttr("emitter_province") + " CP: " + getAttr("emitter_postal_code"), MAX_WIDTH_CHARS));
        lines.add(StringUtils.center("TELEFONO: " + getAttr("emitter_phone"), MAX_WIDTH_CHARS));
        lines.add(StringUtils.center("CIF: " + getAttr("emitter_cif"), MAX_WIDTH_CHARS));
        lines.add(String.format(ATTENDED_FORMAT, getAttr("emitter_name")));
        // Informacion factura
        lines.add(StringUtils.repeat('-', MAX_WIDTH_CHARS));
        lines.add(String.format(BILL_FORMAT, bill.getID(), DATE_FORMAT.format(new Timestamp(bill.getTimestamp()))));

        // Encabezado de productos
        lines.add(StringUtils.repeat('-', MAX_WIDTH_CHARS));
        lines.add(String.format(PRODUCT_FORMAT, "UNID.", "DESCRIPCION", "DTO", "PRECIO", "IMPORTE"));

        // Productos
        lines.add(StringUtils.repeat('-', MAX_WIDTH_CHARS));
        float total = 0;
        for (BillProduct product : products) {
            lines.add(String.format(PRODUCT_FORMAT,
                    INTEGER_FORMAT.format(product.getAmount()),
                    product.getProductName(),
                    "",
                    DECIMAL_FORMAT.format(product.getFinalPrice()),
                    DECIMAL_FORMAT.format(product.getFinalPrice())
            ));
            total += product.getFinalPrice();
        }

        // Forma de pago
        lines.add(StringUtils.repeat('-', MAX_WIDTH_CHARS));
        lines.add(String.format(PAYMENT_FORMAT, getAttr("payment_method"), DECIMAL_FORMAT.format(total)));

        // Informacion del receptor
        if (detailed) {
            lines.add(String.format(BOTH_JUSTIFIED, "DESTINATARIO: " + getAttr("receiver_name"), "CIF: " + getAttr("receiver_cif")));
            lines.add(String.format(LEFT_JUSTIFIED, getAttr("receiver_street") + " " + getAttr("receiver_province") + " CP: " + getAttr("receiver_postal_code")));
            lines.add(String.format(LEFT_JUSTIFIED, "TELEFONO: " + getAttr("receiver_phone")));
            lines.add(String.format(LEFT_JUSTIFIED, "CORREO: " + getAttr("receiver_mail")));
            lines.add(StringUtils.repeat('-', MAX_WIDTH_CHARS));
        }

        // Informacion adicional
        lines.add(StringUtils.repeat('-', MAX_WIDTH_CHARS));
        String[] descLines = bill.getDescription().split("\n");

        for (String descLine : descLines) {
            lines.add(descLine.substring(0, Math.min(descLine.length(), MAX_WIDTH_CHARS)));
        }

        lines.add("");
        lines.add(StringUtils.center("-GRACIAS POR CONFIAR EN NOSOTROS-", MAX_WIDTH_CHARS));
        lines.add(StringUtils.center("www.costurart.es", MAX_WIDTH_CHARS));
        lines.add(StringUtils.center("facebook.com/COSTURART-MAKURUIZ", MAX_WIDTH_CHARS));

    }

    public static int findNextPossibleSplitIndex(int offset, String description) {
        int closestSpace = description.substring(
                Math.min(offset, description.length()),
                Math.min(description.length(), offset + MAX_WIDTH_CHARS)
        ).lastIndexOf(' ');

        if (closestSpace != -1 && closestSpace < MAX_SPACE_DISPLACEMENT)
            return closestSpace;

        return -1;
    }

    private String getAttr(String name) {
        BillAttribute attribute = attributes.get(name);
        if (attribute == null)
            return "";
        return attributes.get(name).getValue();
    }

    public void writeToFile(File file) throws IOException {
        if (!file.exists()) {
            File parent = file.getParentFile();

            if (parent != null)
                parent.mkdirs();

            file.createNewFile();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {

            writer.newLine();

            for (String line : lines) {
                writer.append(line);
                writer.newLine();
            }

            writer.newLine();
        }
    }
}
