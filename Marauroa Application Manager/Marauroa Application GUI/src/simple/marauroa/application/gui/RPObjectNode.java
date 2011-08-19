package simple.marauroa.application.gui;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeSupport;
import marauroa.common.game.RPObject;
import org.openide.nodes.BeanNode;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class RPObjectNode extends BeanNode {

    private final PropertyChangeSupport supp = new PropertyChangeSupport(this);

    public RPObjectNode(RPObject object) throws IntrospectionException {
        super(object, null, Lookups.singleton(object));
        setDisplayName("Object: " + object.get("name"));
    }

    @Override
    public Image getIcon(int type) {
        //TODO: get object from server (web service?)
        return ImageUtilities.loadImage("simple/marauroa/application/gui/resource/entity.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }
}
