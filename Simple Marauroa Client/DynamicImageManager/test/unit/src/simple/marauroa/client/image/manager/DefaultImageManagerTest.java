package simple.marauroa.client.image.manager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.File;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class DefaultImageManagerTest {

    public DefaultImageManagerTest() {
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
     * Test of addText method, of class DefaultImageManager.
     */
    @Test
    public void testAddText_5args() {
        System.out.println("addText");
        Font font = new Font("Arial", Font.PLAIN, 10);
        String text = "Sample text";
        int height = 20;
        int width = 50;
        Point location = new Point(5, 5);
        DefaultImageManager instance = new DefaultImageManager();
        instance.addText(font, text, height, width, location);
        assertTrue(instance.getTextLayers().size() > 0);
    }

    /**
     * Test of addText method, of class DefaultImageManager.
     */
    @Test
    public void testAddText_6args() {
        System.out.println("addText");
        Font font = new Font("Arial", Font.PLAIN, 10);
        String text = "Sample text";
        int height = 20;
        int width = 50;
        Point location = new Point(5, 5);
        DefaultImageManager instance = new DefaultImageManager();
        instance.addText(font, text, height, width, location, Color.BLUE);
        assertTrue(instance.getTextLayers().size() > 0);
    }

    /**
     * Test of generate method, of class DefaultImageManager.
     */
    @Test
    public void testGenerate() throws Exception {
        System.out.println("generate");
        File image = new File("sample.png");
        DefaultImageManager instance = new DefaultImageManager();
        Font font = new Font("Arial", Font.PLAIN, 10);
        String text = "Sample text";
        int height = 20;
        int width = 50;
        Point location = new Point(5, 5);
        instance.addText(font, text, height, width, location, Color.BLUE);
        assertTrue(instance.getTextLayers().size() > 0);
        instance.generate(image);
        assertTrue(image.exists());
    }
}
