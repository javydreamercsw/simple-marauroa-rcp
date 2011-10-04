package simple.marauroa.application.core;

import marauroa.common.game.RPObject;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public class LookupRPObjectManagerTest {

    public LookupRPObjectManagerTest() {
    }

    /**
     * Test of update method, of class LookupRPObjectManager.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        RPObject obj1 = new RPObject("entity");
        obj1.put("x1", 1);
        obj1.put("x2", 1);
        obj1.put("x3", 1);
        obj1.put("name", "obj1");
        RPObject obj2 = new RPObject("entity");
        obj2.fill(obj1);
        obj2.put("name", "obj2");
        int before = EventBus.getDefault().lookupAll(RPObject.class).size();
        EventBus.getDefault().add(obj1);
        EventBus.getDefault().add(obj2);
        assertTrue(before + 2 == EventBus.getDefault().lookupAll(RPObject.class).size());
        RPObject changes = new RPObject("entity");
        changes.fill(obj1);
        changes.put("x1", 2);
        LookupRPObjectManager.update(obj1, changes);
        for (RPObject object : EventBus.getDefault().lookupAll(RPObject.class)) {
            if (object.get("name").equals(obj1.get("name"))) {
                for (int i = 1; i < 4; i++) {
                    assertTrue(object.get("x" + i).equals(changes.get("x" + i)));
                }
            }
        }
    }

    /**
     * Test of remove method, of class LookupRPObjectManager.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        RPObject obj1 = new RPObject("entity");
        obj1.put("x1", 1);
        obj1.put("x2", 1);
        obj1.put("x3", 1);
        obj1.put("name", "obj1");
        RPObject obj2 = new RPObject("entity");
        obj2.fill(obj1);
        obj2.put("name", "obj2");
        EventBus.getDefault().add(obj1);
        EventBus.getDefault().add(obj2);
        int before = EventBus.getDefault().lookupAll(RPObject.class).size();
        LookupRPObjectManager.remove(obj2);
        assertTrue(before > EventBus.getDefault().lookupAll(RPObject.class).size());
        EventBus.getDefault().remove(obj1);
        EventBus.getDefault().remove(obj2);
    }
}
