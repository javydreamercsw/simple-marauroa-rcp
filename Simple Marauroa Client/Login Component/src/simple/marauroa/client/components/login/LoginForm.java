/*
 * LoginForm.java
 *
 * Created on Mar 14, 2011, 10:21:06 AM
 */
package simple.marauroa.client.components.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import marauroa.common.io.Persistence;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import simple.client.action.update.ClientGameConfiguration;
import simple.marauroa.client.components.api.ILoginComponent;
import simple.marauroa.client.components.common.MCITool;
import simple.marauroa.client.components.common.Profile;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public final class LoginForm extends javax.swing.JPanel implements ILoginComponent {

    /** Creates new form LoginForm */
    public LoginForm() {
        initComponents();

        profilesComboBox.addActionListener(new ProfilesCB());

        populateProfiles();

        /*
         * Add this callback after everything is initialized
         */
        saveLoginBox.addChangeListener(new SaveProfileStateCB());
        usernameField.requestFocusInWindow();
        setVisible(true);
    }

    /*
     * Author: Da_MusH Description: Methods for saving and loading login
     * information to disk. These should probably make a separate class in the
     * future, but it will work for now. comment: Thegeneral has added encoding
     * for password and username. Changed for multiple profiles.
     */
    private void saveProfiles() {
        File profileDir = new File(System.getProperty("user.home")
                + System.getProperty("file.separator")
                + "." + MCITool.getClient().getGameName());
        profileDir.mkdirs();
        try {
            OutputStream os = Persistence.get().getOutputStream(true,
                    System.getProperty("file.separator") + "."
                    + MCITool.getClient().getGameName(), ".profiles");
            try {
                MCITool.getLoginManager().getProfiles().save(os);
            } finally {
                os.close();
            }
        } catch (IOException ioex) {
            DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                    "An error occurred while saving your login "
                    + "information. " + ioex.getLocalizedMessage(),
                    "Error Saving Login Information", // title of the dialog
                    NotifyDescriptor.PLAIN_MESSAGE,
                    NotifyDescriptor.WARNING_MESSAGE,
                    null,
                    NotifyDescriptor.OK_OPTION // default option is "Cancel"
                    ));
        }
    }

    @Override
    public void open() {
        setVisible(true);
    }

    @Override
    public boolean close() {
        setVisible(false);
        return true;
    }

    /**
     * Profiles combo box selection change listener.
     */
    protected class ProfilesCB implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            profilesCB();
        }
    }

    /**
     * Called when a profile selection is changed.
     */
    protected void profilesCB() {
        Profile profile;
        String host;

        profile = (Profile) profilesComboBox.getSelectedItem();

        if (profile == null) {

            serverPortField.setText(String.valueOf(Profile.DEFAULT_SERVER_PORT));

            usernameField.setText("");
            passwordField.setText("");
        } else {
            host = profile.getHost();
            serverField.setText(host);

            serverPortField.setText(String.valueOf(profile.getPort()));

            usernameField.setText(profile.getUser());
            passwordField.setText(profile.getPassword());
        }
    }

    @Override
    public void populateProfiles() {
        profilesComboBox.removeAllItems();
        Iterator<Profile> iter = MCITool.getLoginManager().getProfiles().iterator();

        while (iter.hasNext()) {
            profilesComboBox.addItem(iter.next());
        }

        /*
         * The last profile (if any) is the default.
         */
        int count = profilesComboBox.getItemCount();
        if (count != 0) {
            profilesComboBox.setSelectedIndex(count - 1);
        }
    }

    protected void login(String username, String password, String server,
            String port) {
        Profile profile;

        profile = new Profile();
        profile.setHost(server.trim());
        try {
            profile.setPort(Integer.parseInt(port.trim()));
            // Support for saving port number. Only save when input is a number
        } catch (NumberFormatException ex) {
            Logger.getLogger(LoginForm.class.getSimpleName()).log(
                    Level.SEVERE, null, ex);
            DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                    "That is not a valid port number. Please try again.",
                    "Invalid port", // title of the dialog
                    NotifyDescriptor.PLAIN_MESSAGE,
                    NotifyDescriptor.WARNING_MESSAGE,
                    null,
                    NotifyDescriptor.OK_OPTION // default option is "Cancel"
                    ));
            return;
        }

        profile.setUser(username.trim());
        profile.setPassword(password.trim());

        /*
         * Save profile?
         */
        if (saveLoginBox.isSelected()) {
            MCITool.getLoginManager().getProfiles().add(profile);
            populateProfiles();

            if (savePasswordBox.isSelected()) {
                saveProfiles();
            } else {
                String pw = profile.getPassword();
                profile.setPassword("");
                saveProfiles();
                profile.setPassword(pw);
            }
        }
        //Provide credentials
        MCITool.getClient().provideCredentials(server, username, password, username, port);
    }

    protected void loginButton_actionPerformed(ActionEvent e) {
        // If this window isn't enabled, we shouldn't act.
        if (!isEnabled()) {
            return;
        }

        login(usernameField.getText().trim(),
                new String(passwordField.getPassword()),
                serverField.getText().trim(),
                serverPortField.getText().trim());
    }

    /**
     * Save profile selection change.
     */
    protected class SaveProfileStateCB implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent ev) {
            saveProfileStateCB();
        }
    }

    /**
     * Called when save profile selection change.
     */
    protected void saveProfileStateCB() {
        savePasswordBox.setEnabled(saveLoginBox.isSelected());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        profilesComboBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        serverField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        saveLoginBox = new javax.swing.JCheckBox();
        savePasswordBox = new javax.swing.JCheckBox();
        serverPortField = new javax.swing.JTextField();

        setName(org.openide.util.NbBundle.getMessage(LoginForm.class, "window.title")); // NOI18N

        jLabel1.setText(org.openide.util.NbBundle.getMessage(LoginForm.class, "LoginForm.jLabel1.text")); // NOI18N

        profilesComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText(org.openide.util.NbBundle.getMessage(LoginForm.class, "LoginForm.jLabel2.text")); // NOI18N

        serverField.setText(ClientGameConfiguration.get("DEFAULT_SERVER"));

        jLabel3.setText(org.openide.util.NbBundle.getMessage(LoginForm.class, "LoginForm.jLabel3.text")); // NOI18N

        jLabel4.setText(org.openide.util.NbBundle.getMessage(LoginForm.class, "LoginForm.jLabel4.text")); // NOI18N

        usernameField.setText(org.openide.util.NbBundle.getMessage(LoginForm.class, "LoginForm.usernameField.text")); // NOI18N

        jLabel5.setText(org.openide.util.NbBundle.getMessage(LoginForm.class, "LoginForm.jLabel5.text")); // NOI18N

        passwordField.setText(org.openide.util.NbBundle.getMessage(LoginForm.class, "LoginForm.passwordField.text")); // NOI18N

        saveLoginBox.setText(org.openide.util.NbBundle.getMessage(LoginForm.class, "LoginForm.saveLoginBox.text")); // NOI18N

        savePasswordBox.setSelected(true);
        savePasswordBox.setText(org.openide.util.NbBundle.getMessage(LoginForm.class, "LoginForm.savePasswordBox.text")); // NOI18N
        savePasswordBox.setEnabled(false);

        serverPortField.setText(org.openide.util.NbBundle.getMessage(LoginForm.class, "LoginForm.serverPortField.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(savePasswordBox))
                    .addComponent(saveLoginBox)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(passwordField, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(usernameField, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                            .addComponent(serverPortField, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                            .addComponent(serverField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                            .addComponent(profilesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(profilesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(serverField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(serverPortField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saveLoginBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(savePasswordBox)
                .addGap(25, 25, 25))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JComboBox profilesComboBox;
    private javax.swing.JCheckBox saveLoginBox;
    private javax.swing.JCheckBox savePasswordBox;
    private javax.swing.JTextField serverField;
    private javax.swing.JTextField serverPortField;
    private javax.swing.JTextField usernameField;
    // End of variables declaration//GEN-END:variables
}
