package simple.marauroa.application.core;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import org.junit.*;
import static org.junit.Assert.*;
import org.netbeans.junit.MockServices;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Exceptions;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class MarauroaApplicationTest {

    public MarauroaApplicationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        MockServices.setServices(IFLI.class);
    }

    public static class IFLI extends InstalledFileLocator {

        @Override
        public File locate(String path, String cnb, boolean loc) {
            File f = new File(System.getProperty("release", path));
            return f.exists() ? f : null;
        }
    }

    /**
     * Test of validateName method, of class MarauroaApplication.
     */
    @Test
    public void testValidateName() {
        System.out.println("validateName");
        assertTrue(MarauroaApplication.validateName("x"));
        assertFalse(MarauroaApplication.validateName(null));
        assertFalse(MarauroaApplication.validateName(""));
    }

    /**
     * Test of loadINIConfiguration method, of class MarauroaApplication.
     */
    @Test
    public void testLoadINIConfiguration() {
        System.out.println("loadINIConfiguration");
        MarauroaApplication instance = new MarauroaApplicationImpl();
        instance.setRelativeToClass(getClass());
        assertTrue(instance.loadINIConfiguration().isEmpty());
    }

    /**
     * Test of getName method, of class MarauroaApplication.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        MarauroaApplication instance = new MarauroaApplicationImpl();
        String expResult = "test";
        instance.setName(expResult);
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getApplicationPath method, of class MarauroaApplication.
     */
    @Test
    public void testGetApplicationPath() {
        System.out.println("getApplicationPath");
        MarauroaApplication instance = new MarauroaApplicationImpl();
        instance.setApplicationPath("test");
        assertFalse(instance.getApplicationPath().isEmpty());
    }

    /**
     * Test of getVersion method, of class MarauroaApplication.
     */
    @Test
    public void testGetVersion() {
        System.out.println("getVersion");
        MarauroaApplication instance = new MarauroaApplicationImpl();
        String expResult = "1.0";
        instance.setVersion(expResult);
        String result = instance.getVersion();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIcon method, of class MarauroaApplication.
     */
    @Test
    public void testGetIcon() {
        System.out.println("getIcon");
        int type = 0;
        MarauroaApplication instance = new MarauroaApplicationImpl();
        Image result = instance.getIcon(type);
        assertEquals(null, result);
    }

    /**
     * Test of saveINIFile method, of class MarauroaApplication.
     */
    @Test
    public void testINIFile() {
        System.out.println("saveINIFile");
        MarauroaApplication instance = new MarauroaApplicationImpl();
        instance.setName("test");
        instance.setRelativeToClass(getClass());
        try {
            instance.updateIniFile();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            fail();
        }
        instance.loadINIFile();
        //TODO
//        assertTrue(instance.hasINIFile());
    }

    /**
     * Test of isRunning method, of class MarauroaApplication.
     */
    @Test
    public void testIsRunning() {
        System.out.println("isRunning");
        MarauroaApplication instance = new MarauroaApplicationImpl();
        boolean expResult = false;
        boolean result = instance.isRunning();
        assertEquals(expResult, result);
    }

    public static class MarauroaApplicationImpl extends MarauroaApplication {

        @Override
        public String getIniPath() {
            return getAppINIFileName();
        }
    }
}
