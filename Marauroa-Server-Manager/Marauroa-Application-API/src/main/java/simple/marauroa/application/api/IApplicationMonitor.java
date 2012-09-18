package simple.marauroa.application.api;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public interface IApplicationMonitor {
    /**
     * Starts the monitor instance
     * @return true is successful. False otherwise
     */
    public boolean startMonitor();
    
    /**
     * Stops the monitor instance
     * @return true is successful. False otherwise
     */
    public boolean shutdown();
    
    /**
     * Checks if the monitor is running
     * @return true is successful. False otherwise
     */
    public boolean isRunning();
    
    /**
     * Checks if the monitor is running
     * @return true is successful. False otherwise
     */
    public void start();
}
