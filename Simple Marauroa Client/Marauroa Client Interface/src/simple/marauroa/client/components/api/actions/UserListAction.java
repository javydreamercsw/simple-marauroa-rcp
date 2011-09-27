package simple.marauroa.client.components.api.actions;

import javax.swing.Icon;
import marauroa.common.game.RPObject;
import simple.marauroa.application.core.EventBus;
import simple.marauroa.application.core.MarauroaAction;
import simple.marauroa.client.components.common.MCITool;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public abstract class UserListAction extends MarauroaAction {

    public UserListAction(int position, String name, Icon icon) {
        super(name, icon);
        putValue(NAME, name);
        putValue(POSITION, position);
    }

    public UserListAction(int position, String name) {
        super(name);
        putValue(NAME, name);
        putValue(POSITION, position);
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return (Integer) getValue(POSITION);
    }

    /**
     * @return the actionName
     */
    public String getName() {
        return (String) getValue(NAME);
    }

    protected boolean isMe() {
        return !EventBus.getDefault().lookup(RPObject.class).get("name").equals(MCITool.getClient().getPlayerRPC().get("name"));
    }
}
