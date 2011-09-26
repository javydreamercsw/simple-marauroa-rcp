package simple.marauroa.client.components.api;

import java.util.List;
import simple.marauroa.application.core.EventBusListener;
import simple.server.core.event.api.IZoneEvent;

/**
 * This is a component that handles the zone addition and removal from the
 * world. It makes more sense for chat-like applications that have chat rooms
 * created and removed.
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public interface IZoneListManager extends EventBusListener<IZoneEvent>{

    /**
     * Add a zone
     * @param zone 
     */
    public void addZone(String zone);
    
    /**
     * Remove a zone
     * @param zone 
     */
    public void removeZone(String zone);

    /**
     * Update zone description
     * @param zone Zone to modify
     * @param modified New description
     */
    public void updateZone(String zone, String modified);

    /**
     * Need password to join room
     */
    public void requestPassword();
    
    /**
     * Clear the list
     */
    public void clearList();
    
    /**
     * Get the selected values
     * @return selected values or null if none is selected
     */
    public List getSelectedValues();
    
    /**
     * Get the selected value
     * @return selected value or null if none is selected
     */
    public Object getSelectedValue();
}
