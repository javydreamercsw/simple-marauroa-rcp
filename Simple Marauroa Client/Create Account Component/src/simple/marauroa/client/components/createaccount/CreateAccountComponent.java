/*
 * CreateAccountComponent.java
 *
 * Created on Mar 14, 2011, 4:55:47 PM
 */
package simple.marauroa.client.components.createaccount;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import marauroa.client.BannedAddressException;
import marauroa.client.TimeoutException;
import marauroa.common.game.AccountResult;
import marauroa.common.net.InvalidVersionException;
import org.apache.log4j.Logger;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.RequestProcessor;
import org.openide.util.TaskListener;
import simple.client.action.update.ClientGameConfiguration;
import simple.marauroa.client.components.common.MCITool;
import simple.marauroa.client.components.common.Profile;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class CreateAccountComponent extends javax.swing.JPanel {

    private String badPasswordReason;
    private RequestProcessor.Task theTask = null;
    private final static RequestProcessor RP = new RequestProcessor(
            "Create Account Process", 1, true);
    final ProgressHandle ph = ProgressHandleFactory.createHandle(
            "Creating account and login in...");

    /** Creates new form CreateAccountComponent */
    public CreateAccountComponent() {
        initComponents();
    }

    private static class LowerCaseLetterDocument extends PlainDocument {

        private static final long serialVersionUID = -5123268875802709841L;

        @Override
        public void insertString(int offs, String str, AttributeSet a)
                throws BadLocationException {
            String lower = str.toLowerCase();
            boolean ok = true;
            for (int i = lower.length() - 1; i >= 0; i--) {
                char chr = lower.charAt(i);
                if ((chr < 'a') || (chr > 'z')) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                super.insertString(offs, lower, a);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    /**
     *
     * @param username
     * @param password
     * @return
     */
    public boolean validatePassword(String username, String password) {
        if (password.length() > 5) {

            // check for all numbers
            boolean allNumbers = true;
            try {
                Integer.parseInt(password);
            } catch (Exception e) {
                allNumbers = false;
            }
            if (allNumbers) {
                badPasswordReason = "You have used only numbers in your password. This is not a good security practice.\n" + " Are you sure that you want to use this password?";
            }

            // check for username
            boolean hasUsername = false;
            if (password.contains(username)) {
                hasUsername = true;
            }

            if (!hasUsername) {
                // now we'll do some more checks to see if the password
                // contains more than three letters of the username
                Logger.getLogger(CreateAccountComponent.class.getSimpleName()).debug(
                        "Checking is password contains a derivitive of the username, trimming from the back...");
                int min_user_length = 3;
                for (int i = 1; i < username.length(); i++) {
                    String subuser = username.substring(0, username.length() - i);
                    Logger.getLogger(CreateAccountComponent.class.getSimpleName()).debug("\tchecking for \"" + subuser + "\"...");
                    if (subuser.length() <= min_user_length) {
                        break;
                    }

                    if (password.contains(subuser)) {
                        hasUsername = true;
                        Logger.getLogger(CreateAccountComponent.class.getSimpleName()).debug("Password contians username!");
                        break;
                    }
                }

                if (!hasUsername) {
                    // now from the end of the password..
                    Logger.getLogger(CreateAccountComponent.class.getSimpleName()).debug("Checking is password contains a derivitive of the username, trimming from the front...");
                    for (int i = 0; i < username.length(); i++) {
                        String subuser = username.substring(i);
                        Logger.getLogger(CreateAccountComponent.class.getSimpleName()).debug("\tchecking for \"" + subuser + "\"...");
                        if (subuser.length() <= min_user_length) {
                            break;
                        }
                        if (password.contains(subuser)) {
                            hasUsername = true;
                            Logger.getLogger(CreateAccountComponent.class.getSimpleName()).debug("Password contains username!");
                            break;
                        }
                    }
                }
            }

            if (hasUsername) {
                badPasswordReason = "You have used your username or a derivitive of your username in your password. This is a bad security practice.\n" + " Are you sure that you want to use this password?";
                return false;
            }

        } else {
            String text = "The password you provided is too short. It must be at least 6 characters long.";
            if (isVisible()) {
                DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                        text,
                        "Password error", // title of the dialog
                        NotifyDescriptor.PLAIN_MESSAGE,
                        NotifyDescriptor.WARNING_MESSAGE,
                        null,
                        NotifyDescriptor.OK_OPTION // default option is "Cancel"
                        ));
            } else {
                Logger.getLogger(CreateAccountComponent.class.getSimpleName()).warn(text);
            }
            return false;
        }

        return true;
    }

    /**
     * Runs field checks, to, ex. confirm the passwords correct, etc.
     * @return 
     */
    private boolean checkFields() {
        //
        // Check the password
        //
        final String password = new String(passwordField.getPassword());
        final String passwordretype = new String(
                passwordretypeField.getPassword());
        if (!password.equals(passwordretype)) {
            DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                    "The passwords do not match. Please retype both.",
                    "Password Mismatch", // title of the dialog
                    NotifyDescriptor.PLAIN_MESSAGE,
                    NotifyDescriptor.WARNING_MESSAGE,
                    null,
                    NotifyDescriptor.OK_OPTION // default option is "Cancel"
                    ));
            return false;
        }

        //
        // Password strength
        //
        boolean valPass = validatePassword(usernameField.getText(), password);
        if (!valPass) {
            if (badPasswordReason != null) {
                // didn't like the password for some reason, show a dialog and
                // try again
                Object notify = DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                        badPasswordReason,
                        "Bad Password", // title of the dialog
                        NotifyDescriptor.YES_NO_OPTION,
                        NotifyDescriptor.WARNING_MESSAGE,
                        null,
                        NotifyDescriptor.OK_OPTION // default option is "Cancel"
                        ));

                if (notify == NotifyDescriptor.NO_OPTION) {
                    return false;
                }
            } else {
                return false;
            }
        }

        //
        // Check the email
        //
        String email = emailField.getText();
        if (isInvalid(email)) {
            String text = "The email you entered appears to be invalid.\n"
                    + "You must provide a recover a lost password. "
                    + "Are you sure this email is correct? ";
            Object notify = DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                    text,
                    "Invalid Email", // title of the dialog
                    NotifyDescriptor.YES_NO_OPTION,
                    NotifyDescriptor.WARNING_MESSAGE,
                    null,
                    NotifyDescriptor.OK_OPTION // default option is "Cancel"
                    ));
            if (notify == NotifyDescriptor.NO_OPTION) {
                // no
                return false;
            }
        }

        return true;
    }

    private boolean isInvalid(String email) {
        return !email.contains("@") || !email.contains(".") || (email.length() <= 5);
    }

    protected void createAccountButton_actionPerformed(ActionEvent e,
            boolean saveLoginBoxStatus) {
        /*
         * Run the connection procces in separate thread. added by TheGeneral
         */
        theTask = RP.create(new ConnectRunnable()); //the task is not started yet
        theTask.addTaskListener(new TaskListener() {

            @Override
            public void taskFinished(org.openide.util.Task task) {
                ph.finish();
            }
        });
        theTask.schedule(0); //start the task
    }

    /**
     * Server connect thread runnable.
     */
    protected class ConnectRunnable implements Runnable {

        @Override
        public void run() {
            checkFields();
            final String accountUsername = usernameField.getText();
            final String password = new String(passwordField.getPassword());
            final String email = emailField.getText();
            final String server = serverField.getText();
            int port;

            // standalone check
            if (MCITool.getClient() == null) {
                DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                        "Account not created (running standalone)!",
                        "Warning", // title of the dialog
                        NotifyDescriptor.PLAIN_MESSAGE,
                        NotifyDescriptor.INFORMATION_MESSAGE,
                        null,
                        NotifyDescriptor.OK_OPTION // default option is "Cancel"
                        ));
                return;
            }

            // Get port from form
            final int finalPort;

            try {
                port = Integer.parseInt(serverPortField.getText());
            } catch (Exception ex) {
                DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                        "That is not a valid port number. Please try again.",
                        "Invalid Port", // title of the dialog
                        NotifyDescriptor.PLAIN_MESSAGE,
                        NotifyDescriptor.WARNING_MESSAGE,
                        null,
                        NotifyDescriptor.OK_OPTION // default option is "Cancel"
                        ));
                return;
            }
            finalPort = port;
            // intialize progress bar
            ph.start(4);
            // disable this screen when attempting to connect
            setEnabled(false);
            try {
                MCITool.getClient().connect(server, finalPort);
                // for each major connection milestone call step()
                ph.progress(1);
            } catch (Exception ex) {
                // if something goes horribly just cancel the progressbar
                ph.finish();
                setEnabled(true);
                DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                        "Unable to connect to the server. "
                        + "Please check that your connection is set up and active, "
                        + "then try again.",
                        "Invalid Port", // title of the dialog
                        NotifyDescriptor.PLAIN_MESSAGE,
                        NotifyDescriptor.ERROR_MESSAGE,
                        null,
                        NotifyDescriptor.OK_OPTION));
                Logger.getLogger(CreateAccountComponent.class.getSimpleName()).error(ex, ex);
                return;
            }

            try {
                AccountResult result = MCITool.getClient().createAccount(
                        accountUsername, password, email);
                if (result.failed()) {
                    /*
                     * If the account can't be created, show an error
                     * message and don't continue.
                     */
                    ph.finish();
                    setEnabled(true);
                    DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                            result.getResult().getText(),
                            "Create account failed", // title of the dialog
                            NotifyDescriptor.PLAIN_MESSAGE,
                            NotifyDescriptor.ERROR_MESSAGE,
                            null,
                            NotifyDescriptor.OK_OPTION // default option is "Cancel"
                            ));
                    //TODO: Handle results like account already exists better
                } else {
                    /*
                     * Print username returned by server, as server can
                     * modify it at will to match account names rules.
                     */
                    // Be sure to fix all the variable names.
                    MCITool.getClient().setAccountUsername(accountUsername);
                    ph.progress(2);
                    ph.progress(3);
                    setEnabled(false);
                    ph.progress(4);
                    ph.finish();
                    MCITool.getLoginManager().getProfiles().add(
                            new Profile(server, finalPort,
                            accountUsername, password));
                    MCITool.getClient().setProfileReady(true);
                }
            } catch (InvalidVersionException e) {
                setEnabled(true);
                ph.finish();
                DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                        "You are running an incompatible version of "
                        + ClientGameConfiguration.get("DEFAULT_SERVER")
                        + ". Please update",
                        "Invalid version", // title of the dialog
                        NotifyDescriptor.PLAIN_MESSAGE,
                        NotifyDescriptor.ERROR_MESSAGE,
                        null,
                        NotifyDescriptor.OK_OPTION // default option is "Cancel"
                        ));
            } catch (TimeoutException e) {
                setEnabled(true);
                ph.finish();
                DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                        "Server is not available right now. The server may be "
                        + "down or, if you are using a custom server, you may "
                        + "have entered its name and port number incorrectly.",
                        "Error Logging In", // title of the dialog
                        NotifyDescriptor.PLAIN_MESSAGE,
                        NotifyDescriptor.ERROR_MESSAGE,
                        null,
                        NotifyDescriptor.OK_OPTION // default option is "Cancel"
                        ));
            } catch (BannedAddressException e) {
                setEnabled(true);
                ph.finish();
                DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                        "You IP is banned. If you think this is not right. "
                        + "Please send a Support request to "
                        + "http://sourceforge.net/tracker/?group_id=193525&atid=945763",
                        "IP Banned", // title of the dialog
                        NotifyDescriptor.PLAIN_MESSAGE,
                        NotifyDescriptor.ERROR_MESSAGE,
                        null,
                        NotifyDescriptor.OK_OPTION // default option is "Cancel"
                        ));
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        serverLabel = new javax.swing.JLabel();
        serverField = new javax.swing.JTextField();
        serverPortLabel = new javax.swing.JLabel();
        serverPortField = new javax.swing.JTextField();
        usernameLabel = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        passwordretypeLabel = new javax.swing.JLabel();
        passwordretypeField = new javax.swing.JPasswordField();
        emailLabel = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();

        serverLabel.setText(org.openide.util.NbBundle.getMessage(CreateAccountComponent.class, "CreateAccountComponent.serverLabel.text")); // NOI18N

        serverField.setText(ClientGameConfiguration.get("DEFAULT_SERVER"));

        serverPortLabel.setText(org.openide.util.NbBundle.getMessage(CreateAccountComponent.class, "CreateAccountComponent.serverPortLabel.text")); // NOI18N

        serverPortField.setText(ClientGameConfiguration.get("DEFAULT_PORT"));

        usernameLabel.setText(org.openide.util.NbBundle.getMessage(CreateAccountComponent.class, "CreateAccountComponent.usernameLabel.text")); // NOI18N

        usernameField.setDocument(new LowerCaseLetterDocument());
        usernameField.setText(org.openide.util.NbBundle.getMessage(CreateAccountComponent.class, "CreateAccountComponent.usernameField.text")); // NOI18N

        passwordLabel.setText(org.openide.util.NbBundle.getMessage(CreateAccountComponent.class, "CreateAccountComponent.passwordLabel.text")); // NOI18N

        passwordField.setText(org.openide.util.NbBundle.getMessage(CreateAccountComponent.class, "CreateAccountComponent.passwordField.text")); // NOI18N

        passwordretypeLabel.setText(org.openide.util.NbBundle.getMessage(CreateAccountComponent.class, "CreateAccountComponent.passwordretypeLabel.text")); // NOI18N

        passwordretypeField.setText(org.openide.util.NbBundle.getMessage(CreateAccountComponent.class, "CreateAccountComponent.passwordretypeField.text")); // NOI18N

        emailLabel.setText(org.openide.util.NbBundle.getMessage(CreateAccountComponent.class, "CreateAccountComponent.emailLabel.text")); // NOI18N

        emailField.setText(org.openide.util.NbBundle.getMessage(CreateAccountComponent.class, "CreateAccountComponent.emailField.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(serverLabel)
                    .addComponent(serverPortLabel)
                    .addComponent(usernameLabel)
                    .addComponent(passwordLabel)
                    .addComponent(passwordretypeLabel)
                    .addComponent(emailLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(serverField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(serverPortField, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordretypeField, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {emailField, passwordField, passwordretypeField, serverField, serverPortField, usernameField});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(serverLabel)
                    .addComponent(serverField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(serverPortLabel)
                    .addComponent(serverPortField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameLabel)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordretypeLabel)
                    .addComponent(passwordretypeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailLabel)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {emailField, passwordField, passwordretypeField, serverField, serverPortField, usernameField});

    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField emailField;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JPasswordField passwordretypeField;
    private javax.swing.JLabel passwordretypeLabel;
    private javax.swing.JTextField serverField;
    private javax.swing.JLabel serverLabel;
    private javax.swing.JTextField serverPortField;
    private javax.swing.JLabel serverPortLabel;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
