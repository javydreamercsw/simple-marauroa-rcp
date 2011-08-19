/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.marauroa.application.core;

import org.openide.util.Exceptions;
import java.io.IOException;
import java.io.File;
import java.awt.Image;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import simple.marauroa.application.api.ConfigurationElement;
import static org.junit.Assert.*;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class DefaultMarauroaApplicationTest {

    private int port = Integer.valueOf(ConfigurationElement.TCP_PORT.getValue().toString());
    private static DefaultMarauroaApplication instance = new DefaultMarauroaApplication();

    public DefaultMarauroaApplicationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        //Create a dummy application
        instance.setName("Dummy");
        instance.deleteAppDirectory();
        instance.createAppDirectory();
        //Copy the files since the test won't find them
        File folder = new File(System.getProperty("user.dir")
                + System.getProperty("file.separator") + ".."
                + System.getProperty("file.separator") + "Marauroa-Lib"
                + System.getProperty("file.separator") + "release"
                + System.getProperty("file.separator") + "modules"
                + System.getProperty("file.separator") + "ext");
        String path = new File(instance.getAppDirPath() + System.getProperty("file.separator") + "lib").getAbsolutePath();
        if (folder != null) {
            try {
                DefaultMarauroaApplication.copyContentsOfFolder(folder, path);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        folder = new File(System.getProperty("user.dir")
                + System.getProperty("file.separator") + ".."
                + System.getProperty("file.separator") + "Marauroa-Simple-Server-Lib"
                + System.getProperty("file.separator") + "release"
                + System.getProperty("file.separator") + "modules"
                + System.getProperty("file.separator") + "ext");
        if (folder != null) {
            try {
                DefaultMarauroaApplication.copyContentsOfFolder(folder, path);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        //Recreate the ini so it sees the libraries
        instance.updateStartScript();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        instance.deleteAppDirectory();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getIcon method, of class DefaultMarauroaApplication.
     */
    @Test
    public void testGetIcon() {
        System.out.println("getIcon");
        int type = 0;
        Image expResult = null;
        Image result = instance.getIcon(type);
        assertEquals(expResult, result);
    }

    /**
     * Test of start method, of class DefaultMarauroaApplication.
     */
    @Test
    public void testStartAndShutdown() throws Exception {
        System.out.println("start");
        //Check the port is available
        assertTrue(DefaultMarauroaApplication.available(port));
        assertTrue(instance.start());
        //Check the port is binded
        assertFalse(DefaultMarauroaApplication.available(port));

        System.out.println("shutdown");
        //Check the port is binded
        assertFalse(DefaultMarauroaApplication.available(port));
        assertEquals(true, instance.shutdown());
        //Check the port is available
        assertTrue(DefaultMarauroaApplication.available(port));
    }
}
