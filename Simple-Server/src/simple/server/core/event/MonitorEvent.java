/*
 * $Rev$
 * $LastChangedDate$
 * $LastChangedBy$
 */
package simple.server.core.event;

import simple.server.core.event.api.IMonitorEvent;
import marauroa.common.game.RPEvent;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class MonitorEvent extends RPEvent implements IMonitorEvent{

    public static final String ACTION = "action", TYPE = "monitor_event", STRING = "string", OBJECT = "object";
    public static final int GET_ZONE_INFO = 1, GET_ZONES = 2, REGISTER = 3, UNREGISTER = 4;

    public MonitorEvent(String string, int action) {
        put("type", TYPE);
        put(ACTION, action);
        put(STRING, string);
    }

    public MonitorEvent(Object object, int action) {
        put("type", TYPE);
        put(ACTION, action);
        put(OBJECT, object.toString());
    }
}
