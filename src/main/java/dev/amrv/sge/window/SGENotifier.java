package dev.amrv.sge.window;

import java.awt.Component;
import javax.swing.JDialog;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class SGENotifier {

    public static void displayError(Component parent, String title, String message) {
        JDialog dialog = new ErrorDialog(parent, title, message);
        dialog.setVisible(true);
    }

    public static void displayError(Component parent, String title, String message, Throwable exception) {
        JDialog dialog = new ExceptionDialog(parent, title, message, exception);
        dialog.setVisible(true);
    }
}
