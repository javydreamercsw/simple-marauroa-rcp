package simple.marauroa.client.components.userlist;

import java.beans.IntrospectionException;
import java.util.Iterator;
import java.util.List;
import marauroa.common.game.RPObject;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import simple.marauroa.application.core.EventBus;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class ClientRPObjectChildFactory extends ChildFactory<RPObject> {

    @Override
    protected boolean createKeys(List<RPObject> list) {
        for (Iterator<? extends RPObject> it = 
                EventBus.getDefault().lookupAll(RPObject.class).iterator(); it.hasNext();) {
            RPObject object = it.next();
            list.add(object);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(RPObject object) {
        try {
            return new ClientRPObjectNode(object);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }

    @Override
    protected Node[] createNodesForKey(RPObject key) {
        return new Node[]{createNodeForKey(key)};
    }

    public void refresh() {
        refresh(true);
    }
}
