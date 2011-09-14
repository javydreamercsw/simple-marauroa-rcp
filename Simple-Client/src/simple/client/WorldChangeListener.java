package simple.client;

import marauroa.common.game.RPObject;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public interface WorldChangeListener {

    /**
     * RPObject was added to the zone
     * @param object 
     */
    public void onAdded(RPObject object);
    
    /**
     * RPObject was removed from the zone
     * @param object 
     */
    public void onDeleted(RPObject object);
}
