package simple.marauroa.event.manager.zone;

import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;
import simple.marauroa.application.core.EventBusListener;
import simple.marauroa.client.components.api.IZoneListManager;
import simple.marauroa.client.components.common.MCITool;
import simple.server.core.event.ZoneEvent;
import simple.server.core.event.api.IZoneEvent;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = IZoneListManager.class)
public class ZoneEventManager implements EventBusListener<IZoneEvent>, IZoneListManager {

    private static final Logger logger =
            Logger.getLogger(ZoneEventManager.class.getSimpleName());
    private static ZoneListTopComponent instance;

    private void processAdd(IZoneEvent event) {
        if (MCITool.getZoneListManager() != null) {
            logger.log(Level.INFO, "Adding zone: {0}", event.get(ZoneEvent.FIELD));
            MCITool.getZoneListManager().addZone(event.get(ZoneEvent.FIELD)
                    + (event.get(ZoneEvent.DESC) == null ? "" : ": " + event.get(ZoneEvent.DESC)));
        }
    }

    private void processUpdate(IZoneEvent event) {
        if (MCITool.getZoneListManager() != null) {
            logger.log(Level.INFO, "Updating zone: {0}", event.get(ZoneEvent.FIELD));
            MCITool.getZoneListManager().updateZone(event.get(ZoneEvent.FIELD),
                    event.get(ZoneEvent.DESC));
        }
    }

    private void processRemove(IZoneEvent event) {
        if (MCITool.getZoneListManager() != null) {
            logger.log(Level.INFO, "Removing zone: {0}", event.get(ZoneEvent.FIELD));
            MCITool.getZoneListManager().removeZone(event.get(ZoneEvent.FIELD));
        }
    }

    private void processListZones(IZoneEvent event) {
        if (MCITool.getZoneListManager() != null) {
            MCITool.getZoneListManager().clearList();
            StringTokenizer st = new StringTokenizer(event.get(ZoneEvent.FIELD), "#");
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                logger.log(Level.INFO, "Adding zone: {0}", token);
                MCITool.getZoneListManager().addZone(token);
            }
        }
    }

    private void processNeedPass(IZoneEvent event) {
        //TODO: Fully implement
        if (MCITool.getZoneListManager() != null) {
            MCITool.getZoneListManager().requestPassword();
        }
    }

    @Override
    public void notify(IZoneEvent event) {
        if (event != null && event.getName().equals(ZoneEvent.getRPClassName())) {
            logger.log(Level.INFO, "Got Zone Event: {0}", event);
            //Default Zone Event
            switch (event.getInt(ZoneEvent.ACTION)) {
                case ZoneEvent.ADD:
                    processAdd(event);
                    break;
                case ZoneEvent.UPDATE:
                    processUpdate(event);
                    break;
                case ZoneEvent.REMOVE:
                    processRemove(event);
                    break;
                case ZoneEvent.LISTZONES:
                    processListZones(event);
                    break;
                case ZoneEvent.NEEDPASS:
                    processNeedPass(event);
                    break;
                default:
            }
        } else if (event != null) {
            logger.log(Level.WARNING,
                    "Received the following event, "
                    + "but don't know how to handle it. \n{0}", event);
        }
    }

    protected static ZoneListTopComponent getInstance() {
        if (instance == null) {
            for (TopComponent tc : TopComponent.getRegistry().getOpened()) {
                if (tc instanceof ZoneListTopComponent) {
                    instance = (ZoneListTopComponent) tc;
                    break;
                }
            }
        }
        return instance;
    }

    @Override
    public void addZone(String zone) {
        getInstance().addZone(zone);
    }

    @Override
    public void removeZone(String zone) {
        getInstance().removeZone(zone);
    }

    @Override
    public void updateZone(String zone, String modified) {
        getInstance().updateZone(zone, modified);
    }

    @Override
    public void requestPassword() {
        getInstance().requestPassword();
    }

    @Override
    public void clearList() {
        getInstance().clearList();
    }

    @Override
    public List getSelectedValues() {
        return getInstance().getSelectedValues();
    }

    @Override
    public Object getSelectedValue() {
        return getInstance().getSelectedValue();
    }
}
