package simple.marauroa.application.api;

/**
 * Listens for changes in application status
 * 
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public interface ApplicationStatusChangeListener {
    /*
     * Application changed status
     */
    public void statusChanged();
}
