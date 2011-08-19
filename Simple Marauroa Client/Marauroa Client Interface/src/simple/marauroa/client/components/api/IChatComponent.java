package simple.marauroa.client.components.api;

import java.awt.Color;
import java.awt.event.KeyEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import simple.client.EventLine;
import simple.common.NotificationType;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public interface IChatComponent {

    /**
     * The implemented method.
     *
     * @param header
     * a string with the header name
     * @param line
     * a string representing the line to be printed
     * @param type
     * The logical format type.
     */
    void addLine(String header, String line, NotificationType type);

    void addLine(final EventLine line);

    /**
     * Add a line of text
     * @param header
     * @param line
     */
    void addLine(String header, String line);

    /**
     * Add a line of text
     * @param line
     * @param type
     */
    void addLine(String line, NotificationType type);

    JScrollPane getChatScrollPane();

    /**
     * @param desiredColor
     * the color with which the text must be colored
     * @return the colored style
     */
    Style getColor(Color desiredColor);

    /**
     * Get the current TextPane
     * @return JTextPane
     */
    JTextPane getCurrentTextPane();

    /**
     * React to key presses
     * @param e
     */
    void onKeyPressed(KeyEvent e);

    /**
     * React to key release
     * @param e
     */
    void onKeyReleased(KeyEvent e);

    /**
     * Request game quit
     */
    void requestQuit();

    /**
     * This is used to switch where the messages will be written to
     * @param tp
     */
    void setCurrentTextPane(JTextPane tp);
    
}
