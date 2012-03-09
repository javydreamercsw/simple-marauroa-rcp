package simple.marauroa.application.core;

import org.dreamer.event.bus.EventBus;
import java.io.File;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class MarauroaApplicationRepositoryTest {

    private DefaultMarauroaApplication c = new DefaultMarauroaApplication();

    public MarauroaApplicationRepositoryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        c.setName("Dummy");
        c.deleteAppDirectory();
        c.createAppDirectory();
    }

    @After
    public void tearDown() {
        c.deleteAppDirectory();
    }

    /**
     * Test of get method, of class MarauroaApplicationRepository.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        MarauroaApplicationRepository expResult = MarauroaApplicationRepository.get();
        MarauroaApplicationRepository result = MarauroaApplicationRepository.get();
        assertEquals(expResult, result);
    }

    /**
     * Test of register method, of class MarauroaApplicationRepository.
     */
    @Test
    public void testRegister() throws Exception {
        System.out.println("register");
        assertTrue(EventBus.getDefault().lookupAll(DefaultMarauroaApplication.class).isEmpty());
        MarauroaApplicationRepository.register(new DefaultMarauroaApplication());
        assertFalse(EventBus.getDefault().lookupAll(DefaultMarauroaApplication.class).isEmpty());
        assertFalse(MarauroaApplicationRepository.getIMarauroaApplications().isEmpty());
        MarauroaApplicationRepository.unregister(c);
        assertFalse(MarauroaApplicationRepository.getIMarauroaApplications().isEmpty());
    }

    /**
     * Test of deleteFolder method, of class MarauroaApplicationRepository.
     */
    @Test
    public void testDeleteFolder() {
        System.out.println("deleteFolder");
        File folder = new File("temp");
        folder.mkdirs();
        assertTrue(folder.exists());
        MarauroaApplicationRepository.deleteFolder(folder);
        assertFalse(folder.exists());
    }
}
