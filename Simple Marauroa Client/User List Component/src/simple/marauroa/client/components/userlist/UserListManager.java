package simple.marauroa.client.components.userlist;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.swing.JList;
import marauroa.common.game.RPObject;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;
import simple.marauroa.client.components.api.IUserListComponent;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = IUserListComponent.class)
public class UserListManager implements IUserListComponent {

    private static UserListTopComponent instance;
    private int size = 0;
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
    public void resultChanged(LookupEvent le) {
        Lookup.Result r = (Lookup.Result) le.getSource();
        Collection c = r.allInstances();
        if (c.isEmpty() || size > c.size()) {
            logger.info("Someone got removed, clear the list and start over");
            getInstance().clearList();
        }
        //Repopulate
        if (!c.isEmpty()) {
            size = c.size();
            Iterator iterator = c.iterator();
            //Reset
            while (iterator.hasNext()) {
                RPObject object = (RPObject) iterator.next();
                addPlayer(object);
            }
        }
    }
}
