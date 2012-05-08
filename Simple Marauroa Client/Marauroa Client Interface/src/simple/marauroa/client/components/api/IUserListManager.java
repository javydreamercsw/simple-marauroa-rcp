package simple.marauroa.client.components.api;

import org.openide.util.LookupListener;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public interface IUserListManager extends IClientComponent,
        LookupListener {

    /**
     * Clear the user list. This is needed when you are changing zones.
     */
    public void clearList();
}
