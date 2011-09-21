package simple.marauroa.event.manager.zone;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import simple.marauroa.application.core.EventBusListener;
import simple.marauroa.client.components.common.MCITool;
import simple.server.core.event.ZoneEvent;
import simple.server.core.event.api.IZoneEvent;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public class ZoneEventManager implements EventBusListener<IZoneEvent> {

    private static final Logger logger =
            Logger.getLogger(ZoneEventManager.class.getSimpleName());

    @Override
    public void notify(IZoneEvent event) {
        if (event != null && event.getName().equals(ZoneEvent.getRPClassName())) {
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

    private void processAdd(IZoneEvent event) {
        if (MCITool.getZoneListManager() != null) {
            logger.log(Level.INFO, "Adding zone: {0}", event.get(ZoneEvent.FIELD));
            MCITool.getZoneListManager().addZone(event.get(ZoneEvent.FIELD));
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
}
