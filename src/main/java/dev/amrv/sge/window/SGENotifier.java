package dev.amrv.sge.window;

import java.awt.Component;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

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

    public static File requestFileSaveDirectory(Component parent, String title, File originalPath) {
        JFileChooser saveChooser = new JFileChooser(originalPath);
        saveChooser.setDialogTitle(title);
        saveChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        saveChooser.showSaveDialog(parent);
        return saveChooser.getSelectedFile();
    }

    public static File requestFileSave(Component parent, String title, File originalPath) {
        JFileChooser saveChooser = new JFileChooser(originalPath);
        saveChooser.setDialogTitle(title);
        saveChooser.showSaveDialog(parent);
        return saveChooser.getSelectedFile();
    }

    public static void informate(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static LoadingDialog progress(Component parent, String title, String message, int max) {
        return progress(parent, title, message, max, null);
    }

    public static LoadingDialog progress(Component parent, String title, String message, int max, Runnable onComplete) {
        LoadingDialog dialog = new LoadingDialog(parent, title, message, max, onComplete);
        dialog.setVisible(true);
        return dialog;
    }
}
