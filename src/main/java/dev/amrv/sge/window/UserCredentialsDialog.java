package dev.amrv.sge.window;

import dev.amrv.sge.auth.UserCredentials;
import dev.amrv.sge.io.ImageLoader;
import java.awt.Component;
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

    private static UserCredentials requestUser(Component parent) {
        final UserCredentialsDialog dialog = new UserCredentialsDialog(parent);
        dialog.setVisible(true);
        return null;
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
        inputUsername.setText(username);
        keepUser = !username.trim().isEmpty();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        inputPassword = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        inputShowPassword = new javax.swing.JCheckBox();
        inputUsername = new javax.swing.JTextField();

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

        jButton1.setText("Iniciar sesion");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        inputShowPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputShowPasswordActionPerformed(evt);
            }
        });

        inputUsername.setText("jTextField2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputUsername)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(inputPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inputShowPassword))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jButton1))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jLabel2)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(inputPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputShowPassword))
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
        super.setVisible(b);
        keepUser = false;
    }

    public UserCredentials getCredentials() {
        return UserCredentials.tryGetUser(inputUsername.getText(), new String(inputPassword.getPassword()));
    }

    public String getUsername() {
        return inputUsername.getText();
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        super.setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        canceled = false;
        super.setVisible(false);
        super.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void inputShowPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputShowPasswordActionPerformed
        setPasswordVisible(inputShowPassword.isSelected());
    }//GEN-LAST:event_inputShowPasswordActionPerformed

    public void setPasswordVisible(boolean visible) {
        if (visible) {
            inputPassword.setEchoChar(PASSWORD_VISIBLE);
        } else {
            inputPassword.setEchoChar(PASSWORD_INVISIBLE);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField inputPassword;
    private javax.swing.JCheckBox inputShowPassword;
    private javax.swing.JTextField inputUsername;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
