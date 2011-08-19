package simple.marauroa.application.gui;

import java.beans.IntrospectionException;
import java.util.List;
import marauroa.common.game.IRPZone.ID;
import marauroa.common.game.RPObject;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import simple.marauroa.application.core.MarauroaApplicationRepository;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class RPObjectChildFactory extends ChildFactory<RPObject>{
    private final String appName;
    private final ID zoneId;

    public RPObjectChildFactory(String appName, ID zoneId) {
        this.appName = appName;
        this.zoneId = zoneId;
    }

    @Override
    protected boolean createKeys(List<RPObject> toPopulate) {
        toPopulate.addAll(MarauroaApplicationRepository
                .getRPObjectsForZone(getAppName(), getZoneId()));
        return true;
    } 
    
    @Override
    protected Node createNodeForKey(RPObject app) {
        try {
            return new RPObjectNode(app);
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

    /**
     * @return the zoneId
     */
    public ID getZoneId() {
        return zoneId;
    }

    /**
     * @return the appName
     */
    public String getAppName() {
        return appName;
    }
}
