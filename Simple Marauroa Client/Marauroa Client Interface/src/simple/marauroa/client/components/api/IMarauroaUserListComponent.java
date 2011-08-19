package simple.marauroa.client.components.api;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import marauroa.common.game.RPObject;
import simple.marauroa.client.components.common.SortedListModel;

/**
 *
 * @author user=Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public interface IMarauroaUserListComponent {
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

    /**
     * Check if model has an element named e
     * @param m Model to check
     * @param e element to look for
     * @return search result
     */
    public boolean modelHasElement(AbstractListModel m, String e);

    /**
     * Add list to model
     * @param list
     * @return
     */
    public SortedListModel addToModel(final String list);
    
    /**
     * The character to separate items in the list (String representation)
     * @param character 
     */
    public void setListSeparator(char character);
}
