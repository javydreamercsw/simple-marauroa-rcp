package simple.marauroa.application.gui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeSupport;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.actions.DeleteAction;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import simple.marauroa.application.api.ApplicationStatusChangeListener;
import simple.marauroa.application.api.IDiagramManager;
import simple.marauroa.application.api.IMarauroaApplication;
import simple.marauroa.application.core.MarauroaApplication;
import simple.marauroa.application.core.MarauroaApplicationRepository;
import simple.marauroa.application.core.MonitorService;
import simple.marauroa.application.api.STATUS;
import simple.marauroa.application.gui.dialog.AddRPZoneDialog;

/**
 * Represents a MarauroaApplication element within the system
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class MarauroaApplicationNode extends BeanNode implements ApplicationStatusChangeListener {

    private AddRPZoneDialog dialog = null;
    private final PropertyChangeSupport supp = new PropertyChangeSupport(this);
    private IMarauroaApplication application;
    private Action[] actions;

    public MarauroaApplicationNode(IMarauroaApplication application) throws IntrospectionException {
        super(application, Children.create(new RPZoneChildFactory(application), true),
                Lookups.singleton(application));
        setDisplayName(application.toStringForDisplay());
        this.application = application;
        application.setStatus(STATUS.STOPPED);
        application.addStatusListener(this);
    }

    @Override
    public Image getIcon(int type) {
        Image icon = getMarauroaApplication().getIcon(type);
        if (icon == null) {
            icon = ImageUtilities.loadImage(
                    "simple/marauroa/application/gui/resource/app.png");
        }
        return icon;
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    @Override
    public Cookie getCookie(Class clazz) {
        Children ch = getChildren();

        if (clazz.isInstance(ch)) {
            return (Cookie) ch;
        }

        return super.getCookie(clazz);
    }

    @Override
    public Action[] getActions(boolean popup) {
        actions = new Action[]{
            SystemAction.get(DeleteAction.class),
            null,
            new AddRPZoneAction(),
            new GenerateINIAction(),
            new StartServerAction(),
            new StopServerAction(),
            new ConnectAction(),
            new DisconnectAction(),
            new DeleteServerAction()};
        updateActionsState(actions);
        return actions;
    }

    /**
     * @return the app
     */
    public IMarauroaApplication getMarauroaApplication() {
        return getLookup().lookup(IMarauroaApplication.class);
    }

    private void updateActionsState(Action[] actions) {
        for (int i = 0; i < actions.length; i++) {
            if (actions[i] != null && actions[i] instanceof AddRPZoneAction) {
                actions[i].setEnabled(application.getStatus().contains(STATUS.CONNECTED));
            }
            if (actions[i] != null && actions[i] instanceof GenerateINIAction) {
                actions[i].setEnabled(application != null && !application.hasINIFile());
            }
            if (actions[i] != null && actions[i] instanceof StartServerAction) {
                actions[i].setEnabled(application.getStatus().contains(STATUS.STOPPED));
            }
            if (actions[i] != null && actions[i] instanceof StopServerAction) {
                actions[i].setEnabled(application.getStatus().contains(STATUS.STARTED));
            }
            if (actions[i] != null && actions[i] instanceof ConnectAction) {
                actions[i].setEnabled(application.getStatus().contains(STATUS.DISCONNECTED)
                        && application.getStatus().contains(STATUS.STARTED));
            }
            if (actions[i] != null && actions[i] instanceof DisconnectAction) {
                actions[i].setEnabled(application.getStatus().contains(STATUS.CONNECTED)
                        && application.getStatus().contains(STATUS.STARTED));
            }
            if (actions[i] != null && actions[i] instanceof DeleteServerAction) {
                actions[i].setEnabled(!application.getStatus().contains(STATUS.STARTED));
            }
        }
    }

    @Override
    public void statusChanged() {
        //Update the actions in case something external changed the app status
        Logger.getLogger(MarauroaApplicationNode.class.getSimpleName()).info(
                    "Updating node actions due to status change...");
        actions = getActions(true);
        MonitorService service = Lookup.getDefault().lookup(MonitorService.class);
        if (service != null && application.getStatus().contains(STATUS.DISCONNECTED)) {
            Logger.getLogger(MarauroaApplicationNode.class.getSimpleName()).info(
                    "Removing monitor...");
            //Stop any monitors we have on this application
            service.removeMonitor(application.getName());
        }
        if (application.getStatus().contains(STATUS.UPDATED)) {
            //The contents of the application changed, time to update the visual component
            Logger.getLogger(MarauroaApplicationNode.class.getSimpleName()).info("Need to update!");
            Lookup.getDefault().lookup(IDiagramManager.class).update(application);
            application.getStatus().remove(STATUS.UPDATED);
        }
    }

    private class AddRPZoneAction extends AbstractAction {

        public AddRPZoneAction() {
            putValue(NAME, NbBundle.getMessage(MarauroaApplicationNode.class, "add.zone"));
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (dialog == null) {
                dialog = new AddRPZoneDialog(new JFrame(),
                        getLookup().lookup(IMarauroaApplication.class));
            }
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }
    }

    private class ConnectAction extends AbstractAction {

        public ConnectAction() {
            putValue(NAME, NbBundle.getMessage(MarauroaApplicationNode.class, "connect"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            MonitorService service = Lookup.getDefault().lookup(MonitorService.class);
            if (service != null && service.getMonitor(application.getName()) == null) {
                try {
                    service.addMonitor(application.getName());
                    if (service.getMonitor(application.getName()).isRunning()) {
                        application.setStatus(STATUS.CONNECTED);
                    }
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    private class DisconnectAction extends AbstractAction {

        public DisconnectAction() {
            putValue(NAME, NbBundle.getMessage(MarauroaApplicationNode.class, "disconnect"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            MonitorService service = Lookup.getDefault().lookup(MonitorService.class);
            if (service != null && service.getMonitor(application.getName()) != null) {
                try {
                    service.removeMonitor(application.getName());
                    application.setStatus(STATUS.DISCONNECTED);
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    private class GenerateINIAction extends AbstractAction {

        public GenerateINIAction() {
            putValue(NAME, NbBundle.getMessage(MarauroaApplicationNode.class, "generate.ini"));
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            IMarauroaApplication app = getLookup().lookup(IMarauroaApplication.class);
            app.generateINI();
        }
    }

    private class StartServerAction extends AbstractAction {

        public StartServerAction() {
            putValue(NAME, NbBundle.getMessage(MarauroaApplicationNode.class, "start.server"));
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            IMarauroaApplication app = getLookup().lookup(IMarauroaApplication.class);
            try {
                app.start();
                application.setStatus(STATUS.STARTED);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    private class StopServerAction extends AbstractAction {

        public StopServerAction() {
            putValue(NAME, NbBundle.getMessage(MarauroaApplicationNode.class, "stop.server"));
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            IMarauroaApplication app = getLookup().lookup(IMarauroaApplication.class);
            try {
                app.shutdown();
                application.setStatus(STATUS.STOPPED);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    private class DeleteServerAction extends AbstractAction {

        public DeleteServerAction() {
            putValue(NAME, NbBundle.getMessage(MarauroaApplicationNode.class, "delete.server"));
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            NotifyDescriptor nd = new NotifyDescriptor.Confirmation(
                    "<html><font size=+1 color=red>"
                    + NbBundle.getMessage(
                    MarauroaApplication.class,
                    "acknowledge")
                    + "</font><br>"
                    + "<br>"
                    + NbBundle.getMessage(
                    MarauroaApplicationNode.class,
                    "delete.server.confirmation").replaceAll("%a",
                    getMarauroaApplication().getName())
                    + "<b><b> " + NbBundle.getMessage(
                    MarauroaApplication.class,
                    "yes") + "</b> - to Delete<br>"
                    + "&nbsp;&nbsp;<b>" + NbBundle.getMessage(
                    MarauroaApplication.class,
                    "no") + "</b> - to Cancel<br>",
                    NotifyDescriptor.YES_NO_OPTION);
            Object result = DialogDisplayer.getDefault().notify(nd);
            if (result == NotifyDescriptor.YES_OPTION) {
                try {
                    MarauroaApplicationRepository.deleteApplication(
                            getLookup().lookup(IMarauroaApplication.class));
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else if (result == NotifyDescriptor.NO_OPTION) {
                //Do nothing
            }
        }
    }
}
