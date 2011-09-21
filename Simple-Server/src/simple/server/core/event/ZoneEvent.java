package simple.server.core.event;

import marauroa.common.game.Definition.DefinitionClass;
import marauroa.common.game.Definition.Type;
import marauroa.common.game.RPAction;
import marauroa.common.game.RPClass;
import simple.server.core.engine.SimpleRPZone;
import simple.server.core.event.api.IZoneEvent;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class ZoneEvent extends SimpleRPEvent implements IZoneEvent {

    public static final String FIELD = "field", RPCLASS_NAME = "Zone_Event",
            ACTION = "action", DESC = "description";
    public static final int ADD = 1, UPDATE = 2, REMOVE = 3, LISTZONES = 4,
            NEEDPASS = 5;

    /**
     * Creates the rpclass.
     */
    public static void generateRPClass() {
        RPClass rpclass = new RPClass(RPCLASS_NAME);
        rpclass.add(DefinitionClass.ATTRIBUTE, FIELD, Type.STRING);
        rpclass.add(DefinitionClass.ATTRIBUTE, DESC, Type.LONG_STRING);
        rpclass.add(DefinitionClass.ATTRIBUTE, ACTION, Type.INT);
        addCommonAttributes(rpclass);
    }

    /**
     * @return the ROOM
     */
    public static String getField() {
        return FIELD;
    }

    /**
     * @return the ACTION
     */
    public static String getAction() {
        return ACTION;
    }

    public ZoneEvent() {
        super(RPCLASS_NAME);
    }

    /**
     * Creates a new room event.
     *
     * @param a action containing all the info so it can be resent from client
     * with the password
     * @param action Only works with NEEDPASS
     */
    public ZoneEvent(RPAction a, int action) {
        super(RPCLASS_NAME);
        if (action == NEEDPASS) {
            fill(a);
        }
        put(ACTION, action);
    }

    /**
     * Creates a new room event. 
     *
     * @param zone room added/deleted from server
     * @param action either add or remove
     */
    public ZoneEvent(SimpleRPZone zone, int action) {
        super(RPCLASS_NAME);
        put(FIELD, zone.getName());
        //Don't add the description if deleting the room...
        if (zone.getDescription() != null && !zone.getDescription().isEmpty()
                && action != REMOVE) {
            put(DESC, zone.getDescription());
        }
        put(ACTION, action);
    }

    /**
     * Creates a new room event.
     *
     * @param s string to be sent
     * @param action either add or remove
     */
    public ZoneEvent(String s, int action) {
        super(RPCLASS_NAME);
        put(FIELD, s);
        put(ACTION, action);
    }

    public static String getRPClassName() {
        return RPCLASS_NAME;
    }
}
