package simple.client;

import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import marauroa.client.net.IPerceptionListener;
import marauroa.client.net.PerceptionHandler;
import marauroa.common.game.RPEvent;
import marauroa.common.game.RPObject;
import marauroa.common.game.RPObject.ID;
import marauroa.common.net.message.MessageS2CPerception;
import simple.client.event.listener.RPEventNotifier;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class SimplePerceptionHandler extends PerceptionHandler implements IPerceptionListener {

    private final PerceptionDispatcher dispatch;
    private RPObjectChangeDispatcher rpobjDispatcher;
    private Map<RPObject.ID, RPObject> world_objects;
    private SimpleClient client;
    private static final Logger logger = Logger.getLogger(SimplePerceptionHandler.class.getSimpleName());

    public SimplePerceptionHandler(PerceptionDispatcher dispatch,
            RPObjectChangeDispatcher rpobjDispatcher,
            Map<ID, RPObject> world_objects, SimpleClient client) {
        super(dispatch);
        this.dispatch = dispatch;
        this.rpobjDispatcher = rpobjDispatcher;
        this.world_objects = world_objects;
        this.client = client;
        //Register itself so the methods below are executed.
        this.dispatch.register(SimplePerceptionHandler.this);
    }

    @Override
    public boolean onAdded(RPObject object) {
        logger.log(Level.FINE, "onAdded: {0}", object);
        rpobjDispatcher.dispatchAdded(object, isUser(object));
        return false;
    }

    @Override
    public boolean onClear() {
        return false;
    }

    @Override
    public boolean onDeleted(RPObject object) {
        logger.log(Level.FINE, "onDeleted: {0}", object);
        rpobjDispatcher.dispatchRemoved(object, isUser(object));
        return false;
    }

    @Override
    public void onException(Exception exception,
            MessageS2CPerception perception) {
        logger.log(Level.SEVERE, null, exception);
    }

    @Override
    public boolean onModifiedAdded(RPObject object, RPObject changes) {
        logger.log(Level.FINE, "onModifiedAdded: {0}:{1}", new Object[]{object, changes});
        rpobjDispatcher.dispatchModifyAdded(object, changes, false);
        return false;
    }

    @Override
    public boolean onModifiedDeleted(RPObject object, RPObject changes) {
        logger.log(Level.FINE, "onModifiedDeleted: {0}:{1}", new Object[]{object, changes});
        rpobjDispatcher.dispatchModifyRemoved(object, changes, false);
        return false;
    }

    @Override
    public boolean onMyRPObject(RPObject added, RPObject deleted) {
        logger.fine("onMyRPObject");
        RPObject.ID id = null;
        if (added != null) {
            id = added.getID();
        }
        if (deleted != null) {
            id = deleted.getID();
        }
        if (id == null) {
            // Unchanged.
            logger.fine("Unchanged, returning");
            return true;
        }
        RPObject object = world_objects.get(id);
        if (object != null) {
            //Get the list zones event results
            for (Entry<RPEvent, Boolean> entry : 
                    RPEventNotifier.get().logic(object.events()).entrySet()) {
                try {
                    /*
                     * Allow client to handle the unhandled events.
                     * This shouldn't happen. Client should register 
                     * listeners to all events it's expecting.
                     */
                    if (!entry.getValue()) {
                        client.processEvent(entry.getKey());
                        logger.log(Level.WARNING, "Sending event: {0}"
                                +" to client. Consider "
                                + "registering a listener for "
                                + "this event instead.", entry.getKey());
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, null, e);
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void onPerceptionBegin(byte type, int timestamp) {
    }

    @Override
    public void onPerceptionEnd(byte type, int timestamp) {
    }

    @Override
    public void onSynced() {
    }

    @Override
    public void onUnsynced() {
    }

    /**
     * Check to see if the object is the connected user. This is an ugly hack
     * needed because the perception protocol distinguishes between normal and
     * private (my) object changes, but not full add/removes.
     *
     * @param object
     *            An object.
     * 
     * @return <code>true</code> if it is the user object.
     */
    public boolean isUser(final RPObject object) {
        if (object.getRPClass().subclassOf("client_object")) {
            return client.getAccountUsername().equalsIgnoreCase(object.get("name"));
        } else {
            return false;
        }
    }
}
