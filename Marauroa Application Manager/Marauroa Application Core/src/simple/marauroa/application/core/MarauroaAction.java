package simple.marauroa.application.core;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import marauroa.common.game.RPObject;
import org.openide.util.Utilities;
import simple.common.game.ClientObjectInterface;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public abstract class MarauroaAction extends AbstractAction implements Comparable {

    public String POSITION = "Position";

    public MarauroaAction(String name, Icon icon) {
        super(name, icon);
    }

    public MarauroaAction(String name) {
        super(name);
    }

    /**
     * The action should be capable of deciding if it should be enabled or not.
     * Will be enabled by default.
     */
    public abstract void updateStatus();

    @Override
    public int compareTo(Object other) {
        if (!(other instanceof MarauroaAction)) {
            throw new ClassCastException(" Can only compare "
                    + getClass().getSimpleName() + "s");
        }
        MarauroaAction otherAction = (MarauroaAction) other;
        int position = (Integer) getValue(POSITION);
        int otherPosition = (Integer) otherAction.getValue(POSITION);
        return position - otherPosition;
    }

    protected boolean isMe(ClientObjectInterface object) {
        return Utilities.actionsGlobalContext().lookup(RPObject.class).get("name").equals(object.getName());
    }
}
