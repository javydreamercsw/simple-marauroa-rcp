package simple.marauroa.application.gui;

import java.beans.IntrospectionException;
import java.util.List;
import marauroa.common.game.IRPZone;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import simple.marauroa.application.api.IMarauroaApplication;
import simple.marauroa.application.core.MarauroaApplicationRepository;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class RPZoneChildFactory extends ChildFactory<IRPZone>{
    private final IMarauroaApplication app;

    public RPZoneChildFactory(IMarauroaApplication app) {
        this.app = app;
    }

    @Override
    protected boolean createKeys(List<IRPZone> toPopulate) {
        toPopulate.addAll(MarauroaApplicationRepository
                .getZonesForApplication(app));
        return true;
    } 
    
    @Override
    protected Node createNodeForKey(IRPZone zone) {
        try {
            return new RPZoneNode(getApp().getName(),zone);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }

    @Override
    protected Node[] createNodesForKey(IRPZone key) {
        return new Node[]{createNodeForKey(key)};
    }

    public void refresh() {
        refresh(true);
    }

    /**
     * @return the appName
     */
    public IMarauroaApplication getApp() {
        return app;
    }
}
