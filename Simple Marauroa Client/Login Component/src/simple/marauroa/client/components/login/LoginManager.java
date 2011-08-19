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
import org.openide.util.lookup.ServiceProvider;
import simple.marauroa.client.components.api.ILoginManager;
import simple.marauroa.client.components.common.MCITool;
import simple.marauroa.client.components.common.ProfileList;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = ILoginManager.class)
public class LoginManager implements ILoginManager {

    private LoginForm form;
    protected ProfileList profiles = new ProfileList();

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

        NotifyDescriptor nd = new NotifyDescriptor.Confirmation(form, "Login");
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
        if (profiles.isEmpty()) {
            //TODO: If user cancels the system hangs for ever
            DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                    "No profiles were detected in your computer.\n"
                    + "Please create a profile in order to continue.",
                    "No profiles found", // title of the dialog
                    NotifyDescriptor.WARNING_MESSAGE,
                    NotifyDescriptor.INFORMATION_MESSAGE,
                    null,
                    null));
            MCITool.getCAManager().displayCAManager();
            MCITool.getCAManager().waitUntilDone();
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
                        "An error occurred while creating the profile file.",
                        "Error creating profile file", // title of the dialog
                        NotifyDescriptor.PLAIN_MESSAGE,
                        NotifyDescriptor.ERROR_MESSAGE,
                        null,
                        NotifyDescriptor.OK_OPTION // default option is "Cancel"
                        ));
            }
        } catch (IOException ioex) {
            DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                    "An error occurred while loading your login information",
                    "Error Loading Login Information", // title of the dialog
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
                if(form != null && !form.isVisible()){
                    /*
                     * For some reason the form is not visible but were are 
                     * not ready yet. Sow the screen again.
                     */
                    form.setVisible(true);
                    break;
                }
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
