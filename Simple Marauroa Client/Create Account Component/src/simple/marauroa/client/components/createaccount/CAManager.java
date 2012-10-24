package simple.marauroa.client.components.createaccount;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import org.openide.DialogDisplayer;
import org.openide.LifecycleManager;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;
import simple.marauroa.client.components.api.ICAManager;
import simple.marauroa.client.components.common.MCITool;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = ICAManager.class)
public class CAManager implements ICAManager {

    private CreateAccountComponent form = new CreateAccountComponent();

    @Override
    public void displayCAManager() {
        JButton ok = new JButton();
        ok.setText(org.openide.util.NbBundle.getMessage(CreateAccountComponent.class,
                "CreateAccountComponent.createButton.text"));
        JButton cancel = new JButton();
        cancel.setText("Cancel");

        cancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                exit();
            }
        });

        ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //authenicate username and password
                form.createAccountButton_actionPerformed(e, false);
                //TODO: If there's an error it needs to allow to retry
                waitUntilDone();
            }
        });

        NotifyDescriptor nd = new NotifyDescriptor.Confirmation(form, "Create Account");
        nd.setOptions(new Object[]{ok, cancel});
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

    private void exit() {
        LifecycleManager.getDefault().exit();
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
        while (!MCITool.getClient().isProfileReady()) {
            try {
                Thread.yield();
                Thread.sleep(10);
                if(form != null && !form.isVisible()){
                    /*
                     * For some reason the form is not visible but were are 
                     * not ready yet. Show the screen again.
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
