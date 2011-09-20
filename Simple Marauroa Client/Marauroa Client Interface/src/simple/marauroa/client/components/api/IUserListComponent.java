package simple.marauroa.client.components.api;

import javax.swing.JList;
import marauroa.common.game.RPObject;
import simple.client.WorldChangeListener;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public interface IUserListComponent extends IClientComponent, WorldChangeListener {

    /**
     * Add a player to the user list
     * @param p Player to add
     */
    public void addPlayer(RPObject p);

    /**
     * Remove a player from the UI
     * @param p
     */
    void removePlayer(RPObject p);

    /**
     * Get JList used to represent users in zone
     * @return JList
     */
    public JList getPlayerList();
}
