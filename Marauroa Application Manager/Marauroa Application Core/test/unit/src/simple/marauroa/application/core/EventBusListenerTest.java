package simple.marauroa.application.core;

import org.dreamer.event.bus.EventBusListener;
import static org.junit.Assert.assertTrue;
import org.junit.*;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class EventBusListenerTest {

    public EventBusListenerTest() {
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
     * Test of notify method, of class EventBusListener.
     */
    @Test
    public void testNotify() {
        System.out.println("notify");
        Object object = null;
        EventBusListener instance = new EventBusListenerImpl();
        instance.notify(object);
    }

    public class EventBusListenerImpl implements EventBusListener {

        @Override
        public void notify(Object object) {
            assertTrue(true);
        }
    }
}
