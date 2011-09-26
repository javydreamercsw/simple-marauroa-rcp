package simple.marauroa.client.components.userlist;

import java.util.logging.Logger;
import marauroa.common.game.RPObject;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;
import simple.marauroa.client.components.api.IUserListManager;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = IUserListManager.class)
public class UserListManager implements IUserListManager {

    private static UserListTopComponent instance;

    protected UserListTopComponent getInstance() {
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
        getInstance().update();
    }
}
