package simple.marauroa.client.components.api;

import org.jivesoftware.smack.util.ReaderListener;
import simple.client.EventLine;
import simple.client.event.listener.RPEventListener;
import simple.common.NotificationType;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public interface IChatComponent extends ReaderListener, RPEventListener {

    /**
     * Add line to the output window.
     *
     * @param header
     * a string with the header name
     * @param line
     * a string representing the line to be printed
     * @param type
     * The logical format type.
     */
    void addLine(String header, String line, NotificationType type);

    /**
     * 
     * @param line
     * Event line to output
     */
    void addLine(final EventLine line);

    /**
     * Add a line of text
     * @param header
     * Header of the message
     * @param line
     * String to output
     */
    void addLine(String header, String line);

    /**
     * Add a line of text
     * @param line
     * String to output
     * @param type
     * Type of notification
     * @param from
     * The sender of the message
     */
    void addLine(String line, NotificationType type);

    /**
     * Get the normal window name
     * @return normal window name
     */
    String getNormalOutputName();

    /**
     * Get private window name
     * @return private window name
     */
    String getPrivateOutputname();
}
