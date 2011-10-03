package simple.marauroa.application.api;

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import marauroa.common.game.RPObject;

/**
 * A Marauroa Application represents an application to be ran on top of 
 * Marauroa server. This API provides the requirements of such application to 
 * be used with Marauroa Server Manager.
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public interface IMarauroaApplication {
    /*
     * List of lisntenrs 
     */
    ArrayList<ApplicationStatusChangeListener> statusListeners =
            new ArrayList<ApplicationStatusChangeListener>();
    
    /*
     * Contains the application statuses
     */
    ArrayList<STATUS> status = new ArrayList<STATUS>();

    /*
     * Get the application's stauts
     */
    public ArrayList<STATUS> getStatus();

    /*
     * Set an application's status
     */
    public void setStatus(STATUS applicationStatus);

    /*
     * Add an application status listener
     */
    public void addStatusListener(ApplicationStatusChangeListener listener);

    /*
     * Remove an application status listener
     */
    public void removeStatusListener(ApplicationStatusChangeListener listener);

    /*
     * Get the application's name
     */
    public String getName();

    /*
     * Set the application's name
     */
    public void setName(String name);

    /*
     * Get the application's version
     */
    public String getVersion();

    /*
     * Set the application's version
     */
    public void setVersion(String version);

    /*
     * Any pre-start actions on the application (before starting it's
     * Marauroa instance)
     */
    public boolean start() throws Exception;

    /*
     * Preapare application for shutdown
     */
    public boolean shutdown() throws Exception;

    /*
     * Loads the INI configuration for this application so the INI generator
     * wizard can be ran.
     */
    public Properties loadINIConfiguration();

    /*
     * Get application icon for display. Return null to use
     * Arianne's icon (default).
     */
    public Image getIcon(int type);

    /*
     * Save the ini file for this application
     */
    public boolean saveINIFile(Properties config);

    /*
     * Load the ini file for this application
     */
    public boolean loadINIFile();

    /*
     * Simple test to check the precense of the ini file for this application
     */
    public boolean hasINIFile();

    /*
     * Simple test to check if the application is running or not
     */
    public boolean isRunning();

    /*
     * Perfoms checks to update the application's status (i.e. run state)
     */
    public void checkState();

    /*
     * Sumarized string representation in the following format:
     * <app name>:<version>
     */
    public String toStringForDisplay();

    /*
     * Creates the application directory if it doesn't exists
     */
    public boolean createAppDirectory();

    /*
     * Deletes the application directory if it doesn't exists
     */
    public boolean deleteAppDirectory();

    /*
     * Get a list of application specific jars needed for the server to run.
     * This doesn't include Marauroa and/or Simple-Marauroa client and server 
     * related jars.
     */
    public Collection<File> getApplicationJars();

    /*
     * Get the application's path on the file system
     */
    public String getApplicationPath();
    
    /*
     * Set the application's path on the file system
     */
    public void setApplicationPath(String path);

    /*
     * Get the command string to start the application
     */
    public String getCommandLine();

    /*
     * Set the command string to start the application
     */
    public void setCommandLine(String command);

    /**
     * Set the default command line arguments
     */
    public void setDefaultCommand();

    /*
     * Check that the application folder is correct and update if needed
     */
    public void update();

    /**
     * @return the scriptName
     */
    public String getScriptName();

    /**
     * @param scriptName the scriptName to set
     */
    public void setScriptName(String scriptName);

    /**
     * Get string list of libraries
     * @return 
     */
    public String getLibraries();

    /**
     * @return the appINIFileName
     */
    public String getAppINIFileName();

    /**
     * @param appINIFileName the appINIFileName to set
     */
    public void setAppINIFileName(String appINIFileName);

    /**
     * Get the host of this application. This will come in handy to connect to
     * remote applications
     * @return String representation of the application's IP
     */
    public String getHost();

    /**
     * @param host the host to set
     */
    public void setHost(String host);

    /*
     * Get account name
     */
    public String getAccountName();

    /*
     * Get password
     */
    public String getPassword();

    /*
     * Get port
     */
    public int getPort();

    /*
     * Add an object to the application
     */
    public void addObject(RPObject object);

    /*
     * Remove an object from the application
     */
    public void removeObject(RPObject object);

    /*
     * Get a map of the zones and it's contents
     */
    public HashMap<String, ArrayList<RPObject>> getContents();
    
    /**
     * Get the ini file path for this application
     * By default is: <application's path>/server.ini
     * @return ini file path for this application
     */
    public String getIniPath();
}
