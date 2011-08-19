package simple.marauroa.application.gui.dialog;

import javax.swing.JDialog;
import javax.swing.JFrame;
import org.openide.util.lookup.ServiceProvider;
import simple.marauroa.application.api.IAddApplicationDialogProvider;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = IAddApplicationDialogProvider.class)
public class AddApplicationDialogProvider implements IAddApplicationDialogProvider {

    private AddApplicationDialog dialog = null;
    boolean ignoreFolderCreation = false;

    @Override
    public void setApplicationName(String name) {
        if (dialog != null) {
            dialog.appName.setText(name);
        }
    }

    @Override
    public void setEditableApplicationName(boolean enabled) {
        if (dialog != null) {
            dialog.appName.setEditable(enabled);
        }
    }

    @Override
    public JDialog getDialog() {
        if (dialog == null) {
            dialog = new AddApplicationDialog(new JFrame());
        }
        return dialog;
    }

    @Override
    public void setIgnoreFolderCreation(boolean ignore) {
        ignoreFolderCreation = ignore;
    }

    @Override
    public boolean isFolderCreationIgnored() {
        return ignoreFolderCreation;
    }
}
