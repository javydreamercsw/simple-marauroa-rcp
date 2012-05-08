package simple.marauroa.client.components.userlist;

import java.beans.IntrospectionException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import marauroa.common.game.RPObject;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class ClientRPObjectChildFactory extends ChildFactory<RPObject> {

    static final Comparator<RPObject> NAME_ORDER =
            new Comparator<RPObject>() {
                @Override
                public int compare(RPObject e1, RPObject e2) {
                    return e1.get("name").compareTo(e2.get("name"));
                }
            };

    @Override
    protected boolean createKeys(List<RPObject> list) {
        for (Iterator<? extends RPObject> it =
                Lookup.getDefault().lookupAll(RPObject.class).iterator(); it.hasNext();) {
            RPObject object = it.next();
            list.add(object);
            Collections.sort(list, NAME_ORDER);
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
