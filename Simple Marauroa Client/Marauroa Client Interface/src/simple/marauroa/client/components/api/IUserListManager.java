package simple.marauroa.client.components.api;

import marauroa.common.game.RPObject;
import simple.marauroa.application.core.EventBusListener;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public interface IUserListManager extends IClientComponent,
        EventBusListener<RPObject> {
    
    /**
     * Clear the user list.
     * This is needed when you are changing zones.
     */
    public void clearList();
}
