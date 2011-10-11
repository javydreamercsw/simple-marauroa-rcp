package simple.marauroa.client.components.api;

import org.openide.windows.TopComponent;

/**
 * Expose TopComponents methods
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public interface IClientComponent {

    /**
     * Open the component
     */
    void open();

    /**
     * Close the component
     *
     * @return if it was closed
     */
    boolean close();

    /**
     * Get the TopComponent (if already opened)
     * @return 
     */
    TopComponent getComponent();
}
