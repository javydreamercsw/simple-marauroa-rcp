package simple.marauroa.client.components.userlist;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeSupport;
import java.net.MalformedURLException;
import marauroa.common.game.RPObject;
import org.openide.nodes.BeanNode;
import org.openide.util.Exceptions;
import org.openide.util.lookup.Lookups;
import simple.marauroa.application.core.tool.Tool;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class ClientRPObjectNode extends BeanNode {

    private final PropertyChangeSupport supp = new PropertyChangeSupport(this);

    public ClientRPObjectNode(RPObject object) throws IntrospectionException {
        super(object, null, Lookups.singleton(object));
        setDisplayName(object.get("name"));
    }

    @Override
    public Image getIcon(int type) {
        try {
            return Tool.createImage(
                    "simple.marauroa.application.gui",
                    "resources/images/entity.png",
                    "Entity icon");
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }
}
