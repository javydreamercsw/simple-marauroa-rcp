package simple.marauroa.client.components.api;

import java.awt.Image;
import marauroa.common.game.IRPZone;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public interface IZoneListIconProvider {

    /**
     * Gets a custom icon for this zone
     * @param object Object to get icons for
     * @return New icon or null if nothing to modify
     */
    public Image getIcon(IRPZone object);
}
