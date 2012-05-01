package simple.marauroa.application.gui;

import java.beans.IntrospectionException;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import simple.marauroa.application.api.IDataBase;
import simple.marauroa.application.api.IMarauroaApplication;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class MarauroaAppChildFactory extends ChildFactory<IMarauroaApplication> {

    @Override
    protected boolean createKeys(List<IMarauroaApplication> toPopulate) {
        toPopulate.addAll(Lookup.getDefault().lookup(IDataBase.class).getIMarauroaApplications());
        return true;
    }

    @Override
    protected Node createNodeForKey(IMarauroaApplication app) {
        try {
            return new MarauroaApplicationNode(app);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }

    @Override
    protected Node[] createNodesForKey(IMarauroaApplication key) {
        return new Node[]{createNodeForKey(key)};
    }

    public void refresh() {
        refresh(true);
    }
}
