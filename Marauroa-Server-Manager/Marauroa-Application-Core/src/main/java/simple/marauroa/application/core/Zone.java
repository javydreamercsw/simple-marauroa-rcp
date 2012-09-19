package simple.marauroa.application.core;

import java.util.Iterator;
import marauroa.common.game.Perception;
import marauroa.common.game.RPObject;
import marauroa.common.game.RPObjectInvalidException;
import simple.server.core.engine.SimpleRPZone;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public class Zone extends SimpleRPZone {

    private final String key;

    public Zone(String key) {
        super(key);
        this.key = key;
    }

    @Override
    public ID getID() {
        return new ID(key);
    }

    @Override
    public void onInit() throws Exception {
        //Do nothing
    }

    @Override
    public void onFinish() throws Exception {
        //Do nothing
    }

    @Override
    public void modify(RPObject rpo) throws RPObjectInvalidException {
        RPObject object = objects.get(rpo.getID());
        if (object != null) {
            object.fill(rpo);
        }
    }

    @Override
    public RPObject remove(RPObject.ID id) {
        RPObject object = objects.get(id);
        if (object != null) {
            objects.remove(id);
            return object;
        }
        return null;
    }

    @Override
    public RPObject get(RPObject.ID id) {
        return objects.get(id);
    }

    @Override
    public boolean has(RPObject.ID id) {
        return objects.containsKey(id);
    }

    @Override
    public Iterator<RPObject> iterator() {
        return objects.values().iterator();
    }

    @Override
    public long size() {
        return objects.size();
    }

    @Override
    public Perception getPerception(RPObject rpo, byte b) {
        return null;
    }

    @Override
    public void nextTurn() {
        //Do nothing
    }
}
