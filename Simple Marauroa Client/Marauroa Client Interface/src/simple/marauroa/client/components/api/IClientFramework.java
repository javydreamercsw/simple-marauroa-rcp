package simple.marauroa.client.components.api;

import java.io.IOException;
import marauroa.client.BannedAddressException;
import marauroa.client.LoginFailedException;
import marauroa.client.TimeoutException;
import marauroa.common.game.AccountResult;
import marauroa.common.game.RPObject;
import marauroa.common.net.InvalidVersionException;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public interface IClientFramework extends Runnable{

    /**
     * Get account username
     *
     * @return
     */
    String getAccountUsername();

    /*
     * Get availablecharacternames
     */
    String[] getAvailableCharacters();

    /*
     * Get the ClientObject representing the player on the server
     */
    RPObject getPlayerRPC();

    void sendMessage(String text);

    /**
     * Set Account username
     * @param username
     */
    void setAccountUsername(String username);

    /**
     * Set Game name
     * @param gameName the gameName to set
     */
    void setGameName(String gameName);

    /**
     * Get game name
     */
    public String getGameName();

    /**
     * @param versionNumber the versionNumber to set
     */
    void setVersionNumber(String versionNumber);

    /*
     * Get version number
     */
    public String getVersionNumber();

    /*
     * Connect to server
     */
    void connect(String s, int i) throws IOException;

    /*
     * Login to server
     */
    void login(String user, String pass) throws BannedAddressException,
            LoginFailedException, TimeoutException, InvalidVersionException;

    /*
     * Set login complete flag
     */
    void setLoginDone(boolean done);

    /*
     * Is login process done?
     */
    public boolean isLoginDone();

    /*
     * Create an account in the server
     */
    public AccountResult createAccount(String accountUsername, String password, String email)
            throws BannedAddressException, InvalidVersionException,
            InvalidVersionException, TimeoutException;

    /*
     * Flag that the profiles are available and seems to be valid
     */
    public void setProfileReady(boolean b);

    /*
     * Check the profile flag
     */
    public boolean isProfileReady();

    /*
     * Disconnect from server
     */
    public boolean logout() throws marauroa.client.BannedAddressException,
            marauroa.client.TimeoutException, marauroa.common.net.InvalidVersionException;

    /*
     * Get the path to the application's log properties file
     */
    public String getLogPropertiesPath();

    /**
     * Client main loop. This just exposing the Marauroa's ClientFramework 
     * method to make everything work.
     * @param delay delay
     * @return true if new messages were received.
     */
    public boolean loop(int delay);

    /**
     * Connect to the server
     * @param host
     * @param username
     * @param password
     * @param character
     * @param port
     * @param name
     * @param verison 
     */
    public void provideCredentials(String host, String username, String password,
            String character, final String port);

    /**
     * Send private text
     * @param mess      Message to send
     * @param target    Message's target
     */
    public void sendPrivateText(String mess, String target);

    /**
     * Logic to start additional modules after the login was successful.
     */
    public void startModules();
    
    /**
     * Check if chat notifications is enabled
     * @return true if enabled
     */
    public boolean isChatNotifications();
    
    /**
     * Set chat notifications
     * @param chatNotifications true to enable
     */
    public void setChatNotifications(boolean chatNotifications);
    
    /**
     * Get an RPObject by its id as know by the client
     * 
     * @param id Id to look for
     * @return RPObject with that id or null if not found
     */
    public RPObject getFromWorld(RPObject.ID id);
}
