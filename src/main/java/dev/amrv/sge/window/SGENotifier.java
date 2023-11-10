package dev.amrv.sge.window;

import dev.amrv.sge.auth.UserCredentials;
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

    public static boolean confirm(Component parent, String title, String message, boolean defaultAccept) {
        ConfirmDialog dialog = new ConfirmDialog(parent, title, message, defaultAccept);
        dialog.setVisible(true);
        return dialog.wasAccepted();
    }

    public static boolean elevatedAccess(Component parent, String... permissions) {
        UserCredentials elevatedAccessUser = UserCredentialsDialog.requestUser(parent);

        for (String perm : permissions) {
            if (elevatedAccessUser.hasPermission(perm))
                return true;
        }
        return false;
    }
}
