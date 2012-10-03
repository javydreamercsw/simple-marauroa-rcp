package simple.marauroa.client.components.userlist;

import marauroa.common.game.RPObject;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.Utilities;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;
import simple.marauroa.client.components.api.IClientFramework;
import simple.marauroa.client.components.api.IUserListManager;
import simple.server.core.entity.clientobject.ClientObject;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = IUserListManager.class)
public class UserListManager implements IUserListManager {

    private static UserListTopComponent instance;
    private Lookup.Result<ClientObject> result = Utilities.actionsGlobalContext().lookupResult(ClientObject.class);

    @Override
    public UserListTopComponent getComponent() {
        if (instance == null) {
            for (TopComponent tc : TopComponent.getRegistry().getOpened()) {
                if (tc instanceof UserListTopComponent) {
                    instance = (UserListTopComponent) tc;
                    break;
                }
            }
            //Set up the listener stuff
            result.allItems();
            result.addLookupListener(UserListManager.this);
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
    public void clearList() {
        Lookup.getDefault().lookup(IClientFramework.class).clearObjectFromLookup(ClientObject.class);
    }

    @Override
    public void resultChanged(LookupEvent le) {
        getComponent().update();
    }
}
