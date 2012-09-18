package simple.marauroa.application.core;

import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openide.util.Lookup;
import simple.marauroa.application.api.IMarauroaApplication;

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
        assertTrue(instance.getTemplate() instanceof IMarauroaApplication);
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
        assertTrue(instance.getTemplate() instanceof IMarauroaApplication);
    }
    
    /**
     * Test the presence in the Lookup
     */
    @Test
    public void testLookup(){
        boolean found = false;
        for (Iterator<? extends IMarauroaApplication> it = Lookup.getDefault().lookupAll(IMarauroaApplication.class).iterator(); it.hasNext();) {
            IMarauroaApplication app = it.next();
            if (app instanceof IMarauroaApplication) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
}
