package simple.marauroa.client.image.manager;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.openide.util.lookup.ServiceProvider;

/**
 * This class is able to generate an image in runtime by applying layers of
 * images one on top of another. Also allows to add text in the desired
 * position.
 *
 * Example of use:
 *
 * DefaultImageManager manager = new DefaultImageManager(); URL url =
 * DefaultImageManager.class.getResource("weather-rain.png");
 * manager.getLayers().add(ImageIO.read(url)); url =
 * DefaultImageManager.class.getResource("weather-sun.png");
 * manager.getLayers().add(ImageIO.read(url)); manager.addText(new
 * Font("Arial",Font.PLAIN, 10), "text", 200, 150, new Point(20, 20));
 * manager.generate(new File("temp.png"));
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = ImageManager.class)
public class DefaultImageManager implements ImageManager {

    /**
     * Layers used to create the final image
     */
    private ArrayList<BufferedImage> layers = new ArrayList<BufferedImage>();
    /**
     * Layers used for the text only
     */
    private ArrayList<BufferedImage> textLayers = new ArrayList<BufferedImage>();

    private Dimension getMaxSize() {
        int width = 0, height = 0;
        for (BufferedImage img : getLayers()) {
            if (img.getWidth() > width) {
                width = img.getWidth();
            }
            if (img.getHeight() > height) {
                height = img.getHeight();
            }
        }
        for (BufferedImage img : getTextLayers()) {
            if (img.getWidth() > width) {
                width = img.getWidth();
            }
            if (img.getHeight() > height) {
                height = img.getHeight();
            }
        }
        return new Dimension(width, height);
    }

    /**
     * Add a text layer (uses black as default text color)
     *
     * @param font Font to use
     * @param text text to display
     * @param height height of the resulting image
     * @param width width of the resulting image
     * @param location Point (top left corner) of the text
     */
    @Override
    public void addText(Font font, String text, int height, int width,
            Point location) {
        addText(font, text, height, width, location, Color.BLACK);
    }

    /**
     * Add a text layer
     *
     * @param font Font to use
     * @param text text to display
     * @param height height of the resulting image
     * @param width width of the resulting image
     * @param location Point (top left corner) of the text
     * @param color Color of the text
     */
    @Override
    public void addText(Font font, String text, int height, int width,
            Point location, Color color) {
        BufferedImage textImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        HashMap<TextAttribute, Object> map =
                new HashMap<TextAttribute, Object>();
        map.put(TextAttribute.FAMILY, font.getFamily());
        map.put(TextAttribute.SIZE, font.getSize());
        map.put(TextAttribute.FOREGROUND, color);
        AttributedString aString = new AttributedString(text, map);
        AttributedCharacterIterator paragraph = aString.getIterator();
        // index of the first character in the paragraph.
        int paragraphStart = paragraph.getBeginIndex();
        // index of the first character after the end of the paragraph.
        int paragraphEnd = paragraph.getEndIndex();
        Graphics2D graphics = textImage.createGraphics();
        FontRenderContext frc = graphics.getFontRenderContext();
        // The LineBreakMeasurer used to line-break the paragraph.
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);
        // Set break width to width of Component.
        float breakWidth = width;
        float drawPosY = location.y;
        // Set position to the index of the first character in the paragraph.
        lineMeasurer.setPosition(paragraphStart);

        // Get lines until the entire paragraph has been displayed.
        while (lineMeasurer.getPosition() < paragraphEnd) {
            // Retrieve next layout. A cleverer program would also cache
            // these layouts until the component is re-sized.
            TextLayout layout = lineMeasurer.nextLayout(breakWidth);

            // Compute pen x position. If the paragraph is right-to-left we
            // will align the TextLayouts to the right edge of the panel.
            // Note: this won't occur for the English text in this sample.
            // Note: drawPosX is always where the LEFT of the text is placed.
            float drawPosX = layout.isLeftToRight()
                    ? location.x : breakWidth - layout.getAdvance();

            // Move y-coordinate by the ascent of the layout.
            drawPosY += layout.getAscent();

            // Draw the TextLayout at (drawPosX, drawPosY).
            layout.draw(graphics, drawPosX, drawPosY);

            // Move y-coordinate in preparation for next layout.
            drawPosY += layout.getDescent() + layout.getLeading();
        }
        getTextLayers().add(textImage);
    }

    /**
     * Generate an image based on the provided layers
     *
     * @param image File where the resulting image is going to be created
     * @throws IOException
     */
    @Override
    public void generate(File image) throws IOException {
        Dimension size = getMaxSize();
        BufferedImage finalImage = new BufferedImage(size.width, size.height,
                BufferedImage.TYPE_INT_ARGB);
        for (BufferedImage img : getLayers()) {
            finalImage.createGraphics().drawImage(img,
                    0, 0, size.width, size.height,
                    0, 0, img.getWidth(null),
                    img.getHeight(null),
                    null);
        }
        for (BufferedImage text : getTextLayers()) {
            finalImage.createGraphics().drawImage(text,
                    0, 0, text.getWidth(), text.getHeight(),
                    0, 0, text.getWidth(null),
                    text.getHeight(null),
                    null);
        }
        ImageIO.write(finalImage,
                image.getName().substring(image.getName().lastIndexOf('.') + 1),
                image);
    }

    /**
     * @return the textLayers
     */
    @Override
    public ArrayList<BufferedImage> getTextLayers() {
        return textLayers;
    }

    /**
     * @return the layers
     */
    @Override
    public ArrayList<BufferedImage> getLayers() {
        return layers;
    }
}
