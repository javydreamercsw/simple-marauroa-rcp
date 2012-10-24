/*
 * This interface provides common Manager methods
 */
package simple.marauroa.client.components.api;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public interface IManager {
    /*
     * Show/Hide the Manager's screen
     */
    public void setVisible(boolean visible);
    
    /*
     * Enable/Disable the Manager's screen
     */
    public void setEnabled(boolean enabled);
    
    /*
     * Wait for the manager to complete it's task
     */
    public void waitUntilDone();
}
