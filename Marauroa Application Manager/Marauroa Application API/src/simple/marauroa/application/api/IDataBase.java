package simple.marauroa.application.api;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public interface IDataBase {
    /**
     * Check if application exists
     * @param name Application name
     * @return true if it exists, false otherwise.
     */
    public boolean applicationExists(String name);
    
    /**
     * Delete application from database
     * @param name Application name
     * @return true if it was removed, false otherwise.
     */
    public boolean deleteApplication(String name);

    /**
     * Adds IMarauroaApplication to database
     * @param newInstance 
     */
    public void addApplication(IMarauroaApplication newInstance);
    
    /**
     * Get an IMarauroaApplication from a name
     * @param name Application name
     * @return IMarauroaApplication
     */
    public IMarauroaApplication getApplication(String name);
}
