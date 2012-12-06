package simple.marauroa.client.components.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JButton;
import marauroa.common.io.Persistence;
import org.openide.DialogDisplayer;
import org.openide.LifecycleManager;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import simple.marauroa.client.components.api.ILoginManager;
import simple.marauroa.client.components.common.MCITool;
import simple.marauroa.client.components.common.ProfileList;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = ILoginManager.class)
@NbBundle.Messages({
    "window.title=Login to Server",
    "LoginForm.jLabel2.text=Server name",
    "LoginForm.savePasswordBox.text=Save password",
    "LoginForm.saveLoginBox.text=Save login profile locally",
    "LoginForm.passwordField.text=",
    "LoginForm.jLabel5.text=Type your password",
    "LoginForm.usernameField.text=",
    "LoginForm.jLabel4.text=Type your username",
    "LoginForm.jLabel1.text=Account profiles",
    "LoginForm.jLabel3.text=Server port",
    "LoginForm.loginButton.text=OK",
    "LoginForm.cancelButton.text=Cancel",
    "LoginForm.createButton.text=Create Account",
    "LoginForm.serverPortField.text=",
    "Profiles.okButton.text=OK",
    "Profiles.cancelButton.text=Cancel",
    "Profiles.noprofiles.text=No profiles were detected in your computer."
    + "\nPlease create a profile in order to continue.",
    "Profiles.noprofiles.title=No profiles found",
    "Profiles.creation.error.message=An error occurred while creating the profile file.",
    "Profiles.creation.error.title=Error creating profile file",
    "Login.loading.error.message=An error occurred while loading your login information",
    "Login.loading.error.title=Error Loading Login Information"
})
public class LoginManager implements ILoginManager {

    private LoginForm form;
    private ProfileList profiles = new ProfileList();
    private boolean login = true;

    @Override
    public void displayLoginManager() {
        form = new LoginForm();
        JButton ok = new JButton();
        ok.setText(org.openide.util.NbBundle.getMessage(LoginManager.class,
                "LoginForm.loginButton.text"));
        JButton cancel = new JButton();
        cancel.setText(org.openide.util.NbBundle.getMessage(LoginManager.class,
                "LoginForm.cancelButton.text"));

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                exit();
            }
        });

        JButton create = new JButton();
        create.setText(org.openide.util.NbBundle.getMessage(LoginManager.class,
                "LoginForm.createButton.text"));

        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                MCITool.getCAManager().setVisible(false);
                MCITool.getCAManager().displayCAManager();
                MCITool.getCAManager().waitUntilDone();
                MCITool.getCAManager().setVisible(true);
            }
        });

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //authenicate username and password
                form.loginButton_actionPerformed(e);
                //TODO: If there's an error it needs to allow to retry
            }
        });

        NotifyDescriptor nd = new NotifyDescriptor.Confirmation(form,
                org.openide.util.NbBundle.getMessage(LoginManager.class,
                "window.title"));
        nd.setOptions(new Object[]{ok, create, cancel});
        nd.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (NotifyDescriptor.CLOSED_OPTION.equals(evt.getNewValue())) {
                    exit();
                }
            }
        });

        DialogDisplayer.getDefault().notify(nd);
    }

    @Override
    public void login(String username, String password, String server, String port) {
        form.login(username, password, server, port);
        waitUntilDone();
    }

    @Override
    public void checkProfiles() {
        /*
         * Load saved profiles
         */
        profiles = loadProfiles();
        if (getProfiles().isEmpty()) {
            JButton ok = new JButton();
            ok.setText(org.openide.util.NbBundle.getMessage(LoginManager.class,
                    "Profiles.okButton.text"));
            JButton cancel = new JButton();
            cancel.setText(org.openide.util.NbBundle.getMessage(LoginManager.class,
                    "Profiles.cancelButton.text"));

            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    exit();
                }
            });

            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    MCITool.getCAManager().displayCAManager();
                    MCITool.getCAManager().waitUntilDone();
                }
            });

            NotifyDescriptor nd = new NotifyDescriptor.Confirmation(form,
                    org.openide.util.NbBundle.getMessage(LoginManager.class,
                    "Profiles.noprofiles.title"));
            nd.setOptions(new Object[]{ok, cancel});
            nd.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (NotifyDescriptor.CLOSED_OPTION.equals(evt.getNewValue())) {
                        exit();
                    }
                }
            });
            nd.setMessage(org.openide.util.NbBundle.getMessage(LoginManager.class,
                    "Profiles.noprofiles.text"));

            DialogDisplayer.getDefault().notify(nd);
        }
    }

    @Override
    public ProfileList loadProfiles() {
        ProfileList tmpProfiles = new ProfileList();
        try {
            InputStream is = Persistence.get().getInputStream(true,
                    System.getProperty("file.separator") + "."
                    + MCITool.getClient().getGameName(), ".profiles");
            try {
                tmpProfiles.load(is);
            } finally {
                is.close();
            }
        } catch (FileNotFoundException fnfe) {
            // Create it
            File profilesFile = new File(System.getProperty("user.home")
                    + System.getProperty("file.separator") + ".profiles");
            try {
                profilesFile.createNewFile();
            } catch (IOException ex) {
                DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                        org.openide.util.NbBundle.getMessage(LoginManager.class,
                        "Profiles.creation.error.message") + "\n"
                        + ex.getLocalizedMessage(),
                        org.openide.util.NbBundle.getMessage(LoginManager.class,
                        "Profiles.creation.error.title"), // title of the dialog
                        NotifyDescriptor.PLAIN_MESSAGE,
                        NotifyDescriptor.ERROR_MESSAGE,
                        null,
                        NotifyDescriptor.OK_OPTION // default option is "Cancel"
                        ));
            }
        } catch (IOException ioex) {
            DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                    org.openide.util.NbBundle.getMessage(LoginManager.class,
                    "Login.loading.error.message") + "\n"
                    + ioex.getLocalizedMessage(),
                    org.openide.util.NbBundle.getMessage(LoginManager.class,
                    "Login.loading.error.title"), // title of the dialog
                    NotifyDescriptor.PLAIN_MESSAGE,
                    NotifyDescriptor.WARNING_MESSAGE,
                    null,
                    NotifyDescriptor.OK_OPTION // default option is "Cancel"
                    ));
        }
        return tmpProfiles;
    }

    private void exit() {
        LifecycleManager.getDefault().exit();
    }

    @Override
    public ProfileList getProfiles() {
        return profiles;
    }

    @Override
    public void setVisible(boolean visible) {
        if (form != null) {
            form.setVisible(visible);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (form != null) {
            form.setEnabled(enabled);
        }
    }

    @Override
    public void waitUntilDone() {
        while (!MCITool.getClient().isLoginDone()) {
            try {
                Thread.yield();
                Thread.sleep(1000);
                if (form != null && !form.isVisible()) {
                    /*
                     * For some reason the form is not visible but were are not
                     * ready yet. Show the screen again.
                     */
                    form.setVisible(true);
                    break;
                }
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    @Override
    public void setAttemptLogin(boolean retry) {
        this.setLogin(retry);
    }

    @Override
    public boolean attemptLogin() {
        return isLogin();
    }

    /**
     * @return the login
     */
    public boolean isLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(boolean login) {
        this.login = login;
    }
}
