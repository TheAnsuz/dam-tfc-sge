package dev.amrv.sge.window;

import dev.amrv.sge.auth.UserCredentials;
import dev.amrv.sge.io.ImageLoader;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class UserCredentialsDialog extends JDialog {

    private static final char PASSWORD_VISIBLE = 0;
    private static final char PASSWORD_INVISIBLE = '*';
    private static final int ICON_SIZE = 24;
    private static final ImageIcon VISIBLE = ImageLoader.getResouceIconSafe("assets/yes-visible.png", ICON_SIZE, ICON_SIZE);
    private static final ImageIcon INVISIBLE = ImageLoader.getResouceIconSafe("assets/not-visible.png", ICON_SIZE, ICON_SIZE);

    public static UserCredentials requestUser(Component parent) {
        final UserCredentialsDialog dialog = new UserCredentialsDialog(parent);
        dialog.setVisible(true);
        return dialog.generateCredentials();
    }

    private boolean canceled = true;
    private boolean keepUser = false;

    public boolean wasCanceled() {
        return canceled;
    }

    public UserCredentialsDialog(Component parent) {
        super.setModal(true);
        initComponents();
        super.setLocationRelativeTo(parent);
    }

    public void setUsername(String username) {
        jTextField1.setText(username);
        keepUser = !username.trim().isEmpty();
    }

    private boolean passwordVisible = false;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabelImage = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Iniciar sesion");
        setAlwaysOnTop(true);
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Nombre de usuario");

        jLabel2.setText("Clave de usuario");

        jLabelImage.setIcon(INVISIBLE);
        jLabelImage.setToolTipText("Visibilidad de clave");
        jLabelImage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabelImage.setIconTextGap(0);
        jLabelImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabelImageMouseReleased(evt);
            }
        });

        jButton1.setText("Iniciar sesion");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jButton1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPasswordField1, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelImage, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelImage, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(jButton1)
                .addContainerGap())
        );

        JRootPane rootPane = SwingUtilities.getRootPane(jButton1); 
        rootPane.setDefaultButton(jButton1);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    @Override
    public void setVisible(boolean b) {
        setPasswordVisible(false);

        if (keepUser)
            jTextField1.setFocusable(false);

        super.setVisible(b);

        jTextField1.setFocusable(true);
        keepUser = false;
    }

    public UserCredentials generateCredentials() {
//        if (jTextField1.getText().trim().isEmpty())
//            return UserCredentials.anonymous();

        return UserCredentials.getUserCredentials(jTextField1.getText(), new String(jPasswordField1.getPassword()));
    }

    private void jLabelImageMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelImageMouseReleased
        setPasswordVisible(!passwordVisible);
    }//GEN-LAST:event_jLabelImageMouseReleased

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        super.setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        canceled = false;
        super.setVisible(false);
        super.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    public void setPasswordVisible(boolean visible) {
        if (visible) {
            passwordVisible = true;
            jPasswordField1.setEchoChar(PASSWORD_VISIBLE);
            jLabelImage.setIcon(VISIBLE);
        } else {
            passwordVisible = false;
            jPasswordField1.setEchoChar(PASSWORD_INVISIBLE);
            jLabelImage.setIcon(INVISIBLE);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelImage;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
