package simple.marauroa.client.components.userlist;

import marauroa.common.game.RPObject;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;
import simple.marauroa.application.core.EventBus;
import simple.marauroa.client.components.api.IUserListManager;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = IUserListManager.class)
public class UserListManager implements IUserListManager {

    private static UserListTopComponent instance;

    @Override
    public UserListTopComponent getComponent() {
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
    public void open() {
        getComponent().open();
    }

    @Override
    public boolean close() {
        boolean res = getComponent().close();
        instance = null;
        return res;
    }

    @Override
    public void notify(RPObject object) {
        getComponent().update();
    }

    @Override
    public void clearList() {
        for (RPObject player : EventBus.getDefault().lookupAll(RPObject.class)) {
            EventBus.getDefault().getCentralLookup().remove(player);
        }
    }
}
