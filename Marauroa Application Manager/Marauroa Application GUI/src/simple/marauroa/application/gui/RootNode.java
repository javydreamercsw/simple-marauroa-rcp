package simple.marauroa.application.gui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import simple.marauroa.application.api.IAddApplicationDialogProvider;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class RootNode extends AbstractNode {

    private JDialog dialog = null;
    private final MarauroaAppChildFactory childFactory;

    /** Creates a new instance of RootNode */
    public RootNode(MarauroaAppChildFactory childFactory) {
        super(Children.create(childFactory, true));
        this.childFactory = childFactory;
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("simple/marauroa/application/gui/resource/right.png");
    }

    @Override
    public Image getOpenedIcon(int type) {
        return ImageUtilities.loadImage("simple/marauroa/application/gui/resource/down.png");
    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    @Override
    public Action[] getActions(boolean popup) {
        return new Action[]{new RootNodeAction()};
    }

    private class RootNodeAction extends AbstractAction {

        public RootNodeAction() {
            putValue(NAME, "Add Marauroa Application");
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (dialog == null) {
                dialog = Lookup.getDefault().lookup(
                        IAddApplicationDialogProvider.class).getDialog();
            }
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }
    }

    public void refresh() {
        this.childFactory.refresh();
    }
}
