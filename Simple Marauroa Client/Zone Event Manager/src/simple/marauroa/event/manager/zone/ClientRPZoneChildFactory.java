package simple.marauroa.event.manager.zone;

import java.beans.IntrospectionException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import marauroa.common.game.IRPZone;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import simple.marauroa.application.core.Zone;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class ClientRPZoneChildFactory extends ChildFactory<Zone> {

    static final Comparator<IRPZone> NAME_ORDER =
            new Comparator<IRPZone>() {
                @Override
                public int compare(IRPZone z1, IRPZone z2) {
                    return z1.getID().toString().compareTo(z2.getID().toString());
                }
            };

    @Override
    protected boolean createKeys(List<Zone> list) {
        for (Iterator<? extends Zone> it =
                Lookup.getDefault().lookupAll(Zone.class).iterator(); it.hasNext();) {
            Zone zone = it.next();
            list.add(zone);
            Collections.sort(list, NAME_ORDER);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(Zone object) {
        try {
            return new ClientRPZoneNode(object);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }

    @Override
    protected Node[] createNodesForKey(Zone key) {
        return new Node[]{createNodeForKey(key)};
    }

    public void refresh() {
        refresh(true);
    }
}
