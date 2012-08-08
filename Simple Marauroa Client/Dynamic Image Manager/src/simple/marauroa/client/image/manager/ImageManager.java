package simple.marauroa.client.image.manager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public interface ImageManager {

    /**
     * Add a text layer (uses black as default text color)
     *
     * @param font Font to use
     * @param text text to display
     * @param height height of the resulting image
     * @param width width of the resulting image
     * @param location Point (top left corner) of the text
     */
    void addText(Font font, String text, int height, int width, Point location);

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
    void addText(Font font, String text, int height, int width, Point location, Color color);

    /**
     * Generate an image based on the provided layers
     *
     * @param image File where the resulting image is going to be created
     * @throws IOException
     */
    void generate(File image) throws IOException;

    /**
     * @return the layers
     */
    ArrayList<BufferedImage> getLayers();

    /**
     * @return the textLayers
     */
    ArrayList<BufferedImage> getTextLayers();
}
