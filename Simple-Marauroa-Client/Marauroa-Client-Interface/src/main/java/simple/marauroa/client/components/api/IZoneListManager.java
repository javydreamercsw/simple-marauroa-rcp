package simple.marauroa.client.components.api;

import simple.client.event.listener.RPEventListener;

/**
 * This is a component that handles the zone addition and removal from the
 * world. It makes more sense for chat-like applications that have chat rooms
 * created and removed.
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public interface IZoneListManager extends RPEventListener, IClientComponent {

    /**
     * Add a zone
     *
     * @param zone
     */
    public void addZone(String zone);

    /**
     * Remove a zone
     *
     * @param zone
     */
    public void removeZone(String zone);

    /**
     * Update zone description
     *
     * @param zone Zone to modify
     * @param modified New description
     */
    public void updateZone(String zone, String modified);

    /**
     * Need password to join room
     */
    public void requestPassword();
}
