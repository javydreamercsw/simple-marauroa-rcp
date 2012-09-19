/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simple.marauroa.application.core.db.controller;

import java.util.List;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import simple.marauroa.application.core.db.Application;
import simple.marauroa.application.core.db.ApplicationPK;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class ApplicationJpaControllerTest {
    
    public ApplicationJpaControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
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
     * Test of getEntityManager method, of class ApplicationJpaController.
     */
    @Test
    public void testGetEntityManager() {
        System.out.println("getEntityManager");
        ApplicationJpaController instance = null;
        EntityManager expResult = null;
        EntityManager result = instance.getEntityManager();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class ApplicationJpaController.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        Application application = null;
        ApplicationJpaController instance = null;
        instance.create(application);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of edit method, of class ApplicationJpaController.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        Application application = null;
        ApplicationJpaController instance = null;
        instance.edit(application);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of destroy method, of class ApplicationJpaController.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        ApplicationPK id = null;
        ApplicationJpaController instance = null;
        instance.destroy(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findApplicationEntities method, of class ApplicationJpaController.
     */
    @Test
    public void testFindApplicationEntities_0args() {
        System.out.println("findApplicationEntities");
        ApplicationJpaController instance = null;
        List expResult = null;
        List result = instance.findApplicationEntities();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findApplicationEntities method, of class ApplicationJpaController.
     */
    @Test
    public void testFindApplicationEntities_int_int() {
        System.out.println("findApplicationEntities");
        int maxResults = 0;
        int firstResult = 0;
        ApplicationJpaController instance = null;
        List expResult = null;
        List result = instance.findApplicationEntities(maxResults, firstResult);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findApplication method, of class ApplicationJpaController.
     */
    @Test
    public void testFindApplication() {
        System.out.println("findApplication");
        ApplicationPK id = null;
        ApplicationJpaController instance = null;
        Application expResult = null;
        Application result = instance.findApplication(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getApplicationCount method, of class ApplicationJpaController.
     */
    @Test
    public void testGetApplicationCount() {
        System.out.println("getApplicationCount");
        ApplicationJpaController instance = null;
        int expResult = 0;
        int result = instance.getApplicationCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
