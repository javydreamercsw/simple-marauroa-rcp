package simple.marauroa.application.core.db.manager;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openide.util.Exceptions;
import simple.marauroa.application.core.DefaultMarauroaApplication;
import simple.marauroa.application.core.db.Application;
import simple.marauroa.application.core.db.ApplicationType;
import simple.marauroa.application.core.db.controller.exceptions.NonexistentEntityException;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class DataBaseManagerTest {

    public DataBaseManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        //Clear database
        for (Application app : DataBaseManager.getApplications()) {
            DataBaseManager.deleteApplication(app);
        }
        for (ApplicationType appType : DataBaseManager.getApplicationTypes()) {
            DataBaseManager.deleteApplicationType(appType);
        }
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of applicationExists method, of class DataBaseManager.
     */
    @Test
    public void testDataBaseManager() {
        System.out.println("applicationExists");
        assertFalse(DataBaseManager.applicationExists("test"));
        DefaultMarauroaApplication app = new DefaultMarauroaApplication("test");
        assertTrue(DataBaseManager.findApplicationType(app).isEmpty());
        assertTrue(DataBaseManager.getApplications().isEmpty());
        DataBaseManager.addApplication(new DefaultMarauroaApplication("test"));
        assertTrue(DataBaseManager.applicationExists("test"));
        assertTrue(DataBaseManager.findApplicationType(app).size() == 1);
        assertTrue(DataBaseManager.getApplications().size() == 1);
        for (Application app2 : DataBaseManager.getApplications()) {
            try {
                DataBaseManager.deleteApplication(app2);
            } catch (NonexistentEntityException ex) {
                Exceptions.printStackTrace(ex);
                fail();
            }
        }
        assertTrue(DataBaseManager.getApplications().isEmpty());
    }
}
