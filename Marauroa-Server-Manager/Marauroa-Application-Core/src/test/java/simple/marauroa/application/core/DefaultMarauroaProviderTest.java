package simple.marauroa.application.core;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class DefaultMarauroaProviderTest {

    public DefaultMarauroaProviderTest() {
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
     * Test of getTemplate method, of class DefaultMarauroaProvider.
     */
    @Test
    public void testGetTemplate() {
        System.out.println("getTemplate");
        DefaultMarauroaProvider instance = new DefaultMarauroaProvider();
        assertTrue(instance.getTemplate() != null);
    }

    /**
     * Test of setTemplate method, of class DefaultMarauroaProvider.
     */
    @Test
    public void testSetTemplate() {
        System.out.println("setTemplate");
        DefaultMarauroaProvider instance = new DefaultMarauroaProvider();
        instance.setTemplate(null);
        assertTrue(instance.getTemplate() == null);
        instance.setTemplate(new DefaultMarauroaApplication());
        assertTrue(instance.getTemplate() instanceof DefaultMarauroaApplication);
    }
}
