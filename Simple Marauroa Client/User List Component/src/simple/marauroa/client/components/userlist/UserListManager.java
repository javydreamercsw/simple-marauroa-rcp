package simple.marauroa.client.components.userlist;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import marauroa.common.game.RPObject;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;
import simple.marauroa.application.core.EventBus;
import simple.marauroa.client.components.api.IUserListComponent;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = IUserListComponent.class)
public class UserListManager implements IUserListComponent {

    private static UserListTopComponent instance;
    private static final Logger logger =
            Logger.getLogger(UserListManager.class.getSimpleName());

    protected static UserListTopComponent getInstance() {
        if (instance == null) {
            for (TopComponent tc : TopComponent.getRegistry().getOpened()) {
                if (tc instanceof UserListTopComponent) {
                    instance = (UserListTopComponent) tc;
                    break;
                }
            }
        }
        return instance;
    }

    @Override
    public void addPlayer(RPObject p) {
        logger.log(Level.FINE, "Adding player: {0}", p.get("name"));
        getInstance().addPlayer(p);
    }

    @Override
    public void removePlayer(RPObject p) {
        getInstance().removePlayer(p);
    }

    @Override
    public JList getPlayerList() {
        return getInstance().getPlayerList();
    }

    @Override
    public void open() {
        getInstance().open();
    }

    @Override
    public boolean close() {
        boolean res = getInstance().close();
        instance = null;
        return res;
    }

    @Override
    public void notify(RPObject object) {
        if (object != null) {
            addPlayer(object);
        } else {
            logger.fine("Some one got removed, clear the list and start over");
            getInstance().clearList();
            for (RPObject entity : EventBus.getDefault().lookupAll(RPObject.class)) {
                addPlayer(entity);
            }
        }
    }
}
