package simple.marauroa.event.manager.zone;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeSupport;
import java.net.MalformedURLException;
import java.util.Iterator;
import javax.swing.Action;
import marauroa.common.game.IRPZone;
import org.openide.nodes.BeanNode;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import simple.marauroa.application.core.tool.Tool;
import simple.marauroa.client.components.api.IZoneListActionProvider;
import simple.marauroa.client.components.api.IZoneListIconProvider;
import simple.server.core.engine.SimpleRPZone;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class ClientRPZoneNode extends BeanNode {

    private final PropertyChangeSupport supp = new PropertyChangeSupport(this);

    public ClientRPZoneNode(IRPZone zone) throws IntrospectionException {
        super(zone, null, Lookups.singleton(zone));
        String desc = null;
        if (zone instanceof SimpleRPZone) {
            SimpleRPZone simpleRPZone = (SimpleRPZone) zone;
            desc = simpleRPZone.getDescription().trim();
        }
        setDisplayName(Tool.getZoneName(zone.getID()) + 
                (desc == null || desc.trim().isEmpty() ? "" : ": " + desc));
    }

    @Override
    public Image getIcon(int type) {
        Image icon = null;
        try {
            //Check for modifiers in the Lookup
            for (Iterator<? extends IZoneListIconProvider> it = Lookup.getDefault().lookupAll(IZoneListIconProvider.class).iterator(); it.hasNext();) {
                IZoneListIconProvider provider = it.next();
                Image newIcon = provider.getIcon(getLookup().lookup(IRPZone.class));
                //Right now it takes the first non-null return. 
                //Makes no sense to have multiple providers.
                if (newIcon != null) {
                    icon = newIcon;
                    break;
                }
            }
            if (icon == null) {
                //Use default
                icon = Tool.createImage("simple.marauroa.application.gui",
                        "resources/images/zone.png", "Zone icon");
            }
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        return icon;
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    public Action[] getActions(boolean popup) {
        return Tool.getActions(IZoneListActionProvider.class);
    }
}
