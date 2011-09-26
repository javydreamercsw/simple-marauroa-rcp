package simple.marauroa.application.core;

import marauroa.common.game.RPObject;

/**
 * Special handler for RPObjects in the EventBus's lookup
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public class LookupRPObjectManager {

    /**
     * Use EventBus lookup by default
     */
    private static CentralLookup lookup = EventBus.getDefault().getCentralLookup();

    public static void update(RPObject object, RPObject changes) {
        //Update lookup
        for (RPObject entry : lookup.lookupAll(RPObject.class)) {
            if (entry.get("id") != null
                    && entry.get("id").equals(object.get("id"))) {
                entry.fill(changes);
                break;
            }
        }
    }

    public static void remove(RPObject object) {
        //Update lookup
        for (RPObject entry : lookup.lookupAll(RPObject.class)) {
            if (entry.get("id") != null
                    && entry.get("id").equals(object.get("id"))) {
                EventBus.getDefault().getCentralLookup().remove(entry);
                return;
            }
        }
    }

    /**
     * @param aLookup the lookup to set
     */
    public static void setLookup(CentralLookup aLookup) {
        lookup = aLookup;
    }
}
