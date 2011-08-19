package simple.marauroa.client.components.api;

import simple.marauroa.client.components.common.ProfileList;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public interface ILoginManager extends IManager{
    /**
     * Display the Login Manager
     */
    public void displayLoginManager();
    
    /**
     * Login to the server
     * @param username  Username
     * @param password  Password
     * @param server    Server
     * @param port      Port
     */
    public void login(String username, String password, String server, String port);
    
    /*
     * Check for valid available profiles. Allow user to create one if none
     * available.
     */
    public void checkProfiles();
    
    /**
     * Load profiles.
     * @return ProfileList
     */
    public ProfileList loadProfiles();
    
    /*
     * Get profiles.
     * @return ProfileList
     */
    public ProfileList getProfiles();
}
