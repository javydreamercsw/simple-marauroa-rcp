package simple.marauroa.application.core;

import org.dreamer.event.bus.EventBusListener;
import org.dreamer.event.bus.EventBus;
import java.util.Collection;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class EventBusTest {

    public EventBusTest() {
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
     * Test of getDefault method, of class EventBus.
     */
    @Test
    public void testGetDefault() {
        System.out.println("getDefault");
        EventBus result = EventBus.getDefault();
        assertEquals(EventBus.getDefault(), result);
    }

    /**
     * Test of publish method, of class EventBus.
     */
    @Test
    public void testSystem() {
        System.out.println("Test event system");
        Object object = new Object();
        EventBus.getDefault().subscribe(Object.class, new EventBusListener() {

            @Override
            public void notify(Object object) {
                assertTrue(true);
            }
        });
        EventBus.getDefault().publish(object);
        EventBusListener listener = new EventBusListener() {

            @Override
            public void notify(Object object) {
                assertFalse(false);
            }
        };
        EventBus.getDefault().subscribe(Object.class, listener);
        EventBus.getDefault().unpublish(Object.class);
        EventBus.getDefault().publish(object);
        EventBus.getDefault().subscribe(Object.class, listener);
        EventBus.getDefault().unsubscribe(Object.class, listener);
        EventBus.getDefault().publish(object);
        EventBus.getDefault().unpublish(Object.class);
        EventBus.getDefault().add("test");
        EventBus.getDefault().add(1);
        String lookup = EventBus.getDefault().lookup(String.class);
        assertTrue(lookup.equals("test"));
        Collection<? extends Object> lookupAll = EventBus.getDefault().lookupAll(Object.class);
        assertEquals(lookupAll.size(), 2);
    }
}
