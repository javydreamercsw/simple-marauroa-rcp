package simple.server.application.db;

import marauroa.common.Configuration;
import static org.junit.Assert.assertEquals;


import marauroa.common.game.RPEvent;
import marauroa.common.game.RPObject;
import marauroa.common.game.RPSlot;
import marauroa.server.db.DBTransaction;
import marauroa.server.db.TransactionPool;
import marauroa.server.game.db.DAORegister;
import marauroa.server.game.db.RPZoneDAO;
import marauroa.server.game.rp.MarauroaRPZone;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import simple.common.application.RPClassTestHelper;
import simple.server.core.engine.MockSimpleRPWorld;

/**
 * This test unit test the load and store methods of rpzoneDAO.
 * 
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 * based on code by miguel
 * 
 */
public class RPZoneAccessTest{

    private static TransactionPool transactionPool;
    private static RPZoneDAO rpzoneDAO;
    private RPObject object;
    private MarauroaRPZone zone;

    /**
     * 
     * @param name
     */
    @BeforeClass
    public static void createDatabase() throws Exception {
        MockSimpleRPWorld.get();

        transactionPool = new TransactionPool(Configuration.getConfiguration().getAsProperties());
        rpzoneDAO = DAORegister.get().get(RPZoneDAO.class);
    }

    /**
     * Setup one time the rpzoneDAO.
     *
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void closeDatabase() throws Exception {
        transactionPool.close();
    }

    /**
     * Populates the zone with some objects.
     *
     */
    @Before
    public void populateZone() {
        object = new RPObject();
        object.put("a", 1);
        object.put("b", "1");
        object.put("c", 2.0);
        object.put("d", "string of text");

        object.addSlot("lhand");
        object.addSlot("rhand");

        RPClassTestHelper.generateRPClasses();
        RPEvent chat = new RPEvent("chat");
        chat.put("text", "Hi there");
        object.addEvent(chat);

        chat = new RPEvent("chat");
        chat.put("text", "Does this work?");
        object.addEvent(chat);

        RPSlot lhand = object.getSlot("lhand");

        RPObject pocket = new RPObject();
        pocket.put("size", 1);
        pocket.addSlot("container");
        lhand.add(pocket);

        RPSlot container = pocket.getSlot("container");

        RPObject coin = new RPObject();
        coin.put("euro", 100);
        coin.put("value", 100);
        container.add(coin);

        zone = new MarauroaRPZone("test");
        /* Define the object as storable */
        object.store();

        zone.assignRPObjectID(object);
        zone.add(object);
    }

    /**
     * Test the store and load methods of database by creating a zone and adding
     * a object and then storing it for at a later stage load the zone from
     * database into a new zone instance.
     *
     * @throws Exception
     */
    @Test
    public void storeAndLoadObjects() throws Exception {
        DBTransaction transaction = transactionPool.beginWork();

        try {
            rpzoneDAO.storeRPZone(transaction, zone);

            MarauroaRPZone newzone = new MarauroaRPZone("test");
            rpzoneDAO.loadRPZone(transaction, newzone);

            RPObject.ID id = new RPObject.ID(1, "test");
            assertEquals(zone.get(id), newzone.get(id));
        } finally {
            transactionPool.rollback(transaction);
        }
    }

    /**
     * Test the store and load methods of database by creating a zone and adding
     * a object and then storing it for at a later stage load the zone from
     * database into a new zone instance and repeating the operation a second
     * time ( to test database update ).
     *
     * @throws Exception
     */
    @Test
    public void storeAndStoreAndLoadObjects() throws Exception {
        DBTransaction transaction = transactionPool.beginWork();

        try {
            rpzoneDAO.storeRPZone(transaction, zone);

            MarauroaRPZone newzone = new MarauroaRPZone("test");
            rpzoneDAO.loadRPZone(transaction, newzone);

            RPObject.ID id = new RPObject.ID(1, "test");
            assertEquals(zone.get(id), newzone.get(id));

            rpzoneDAO.storeRPZone(transaction, newzone);

            MarauroaRPZone doublestoredzone = new MarauroaRPZone("test");
            rpzoneDAO.loadRPZone(transaction, doublestoredzone);

            id = new RPObject.ID(1, "test");
            assertEquals(zone.get(id), doublestoredzone.get(id));
        } finally {
            transactionPool.rollback(transaction);
        }
    }
}
