package simple.marauroa.application.gui;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeSupport;
import marauroa.common.game.IRPZone;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class RPZoneNode extends BeanNode {

    private final String appName;
    private final PropertyChangeSupport supp = new PropertyChangeSupport(this);

    public RPZoneNode(String appName, IRPZone zone) throws IntrospectionException {
        super(zone, Children.create(new RPObjectChildFactory(appName,
                zone.getID()), true),
                Lookups.singleton(zone));
        setDisplayName("Zone: " + zone.getID());
        this.appName = appName;
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("simple/marauroa/application/gui/resource/zone.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    /**
     * @return the appName
     */
    public String getAppName() {
        return appName;
    }
}
