package simple.marauroa.application.core;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class DefaultMarauroaProviderTest {

    public DefaultMarauroaProviderTest() {
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
