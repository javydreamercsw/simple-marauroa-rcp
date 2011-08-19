package simple.marauroa.application.core;

import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import simple.marauroa.application.api.IMarauroaApplication;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class INIGeneratorTest {
    
    public INIGeneratorTest() {
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
     * Test of get method, of class INIGenerator.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        INIGenerator expResult = null;
        INIGenerator result = INIGenerator.get();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of showINI method, of class INIGenerator.
     */
    @Test
    public void testShowINI() {
        System.out.println("showINI");
        IMarauroaApplication app = null;
        Properties config = null;
        INIGenerator.showINI(app, config);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
