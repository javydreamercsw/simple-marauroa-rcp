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
import simple.marauroa.application.core.db.ApplicationType;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class ApplicationTypeJpaControllerTest {
    
    public ApplicationTypeJpaControllerTest() {
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
     * Test of getEntityManager method, of class ApplicationTypeJpaController.
     */
    @Test
    public void testGetEntityManager() {
        System.out.println("getEntityManager");
        ApplicationTypeJpaController instance = null;
        EntityManager expResult = null;
        EntityManager result = instance.getEntityManager();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class ApplicationTypeJpaController.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        ApplicationType applicationType = null;
        ApplicationTypeJpaController instance = null;
        instance.create(applicationType);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of edit method, of class ApplicationTypeJpaController.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        ApplicationType applicationType = null;
        ApplicationTypeJpaController instance = null;
        instance.edit(applicationType);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of destroy method, of class ApplicationTypeJpaController.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        Integer id = null;
        ApplicationTypeJpaController instance = null;
        instance.destroy(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findApplicationTypeEntities method, of class ApplicationTypeJpaController.
     */
    @Test
    public void testFindApplicationTypeEntities_0args() {
        System.out.println("findApplicationTypeEntities");
        ApplicationTypeJpaController instance = null;
        List expResult = null;
        List result = instance.findApplicationTypeEntities();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findApplicationTypeEntities method, of class ApplicationTypeJpaController.
     */
    @Test
    public void testFindApplicationTypeEntities_int_int() {
        System.out.println("findApplicationTypeEntities");
        int maxResults = 0;
        int firstResult = 0;
        ApplicationTypeJpaController instance = null;
        List expResult = null;
        List result = instance.findApplicationTypeEntities(maxResults, firstResult);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findApplicationType method, of class ApplicationTypeJpaController.
     */
    @Test
    public void testFindApplicationType() {
        System.out.println("findApplicationType");
        Integer id = null;
        ApplicationTypeJpaController instance = null;
        ApplicationType expResult = null;
        ApplicationType result = instance.findApplicationType(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getApplicationTypeCount method, of class ApplicationTypeJpaController.
     */
    @Test
    public void testGetApplicationTypeCount() {
        System.out.println("getApplicationTypeCount");
        ApplicationTypeJpaController instance = null;
        int expResult = 0;
        int result = instance.getApplicationTypeCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
