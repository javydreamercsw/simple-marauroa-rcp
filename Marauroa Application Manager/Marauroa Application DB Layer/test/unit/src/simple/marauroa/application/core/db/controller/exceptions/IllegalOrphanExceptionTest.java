/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simple.marauroa.application.core.db.controller.exceptions;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class IllegalOrphanExceptionTest {
    
    public IllegalOrphanExceptionTest() {
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
     * Test of getMessages method, of class IllegalOrphanException.
     */
    @Test
    public void testGetMessages() {
        System.out.println("getMessages");
        IllegalOrphanException instance = null;
        List expResult = null;
        List result = instance.getMessages();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
