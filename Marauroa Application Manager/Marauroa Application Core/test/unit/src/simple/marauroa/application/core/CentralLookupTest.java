package simple.marauroa.application.core;

import static org.junit.Assert.assertTrue;
import org.junit.*;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class CentralLookupTest {

    public CentralLookupTest() {
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
     * Test of add method, of class CentralLookup.
     */
    @Test
    public void testCentralLookup() {
        System.out.println("add");
        String object = "test";
        CentralLookup instance = new CentralLookup();
        instance.add(object);
        assertTrue(object.equals(instance.lookup(String.class)));
        instance.add("test2");
        assertTrue(instance.lookupAll(String.class).size() == 2);
        instance.remove(object);
        assertTrue(instance.lookupAll(String.class).size() == 1);
        assertTrue(!instance.lookup(String.class).equals(object));
    }
}
