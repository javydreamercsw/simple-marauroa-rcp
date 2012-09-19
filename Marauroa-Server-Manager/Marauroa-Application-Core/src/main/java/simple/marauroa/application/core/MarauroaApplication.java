package simple.marauroa.application.core;

import com.dreamer.outputhandler.OutputHandler;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import marauroa.common.crypto.RSAKey;
import marauroa.common.game.RPObject;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import simple.marauroa.application.api.*;
import simple.marauroa.application.core.executor.DefaultMarauroaProcess;
import simple.marauroa.application.core.executor.IMarauroaProcess;
import simple.marauroa.application.core.executor.ScriptExecuter;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public abstract class MarauroaApplication implements IMarauroaApplication {

    private static int MIN_PORT_NUMBER = 1;
    private static int MAX_PORT_NUMBER = 65535;
    protected boolean running = false;
    private String app_name = "", version = "";
    private Properties configuration = new Properties();
    private String appINIFileName = "server.ini";
    private String scriptName = "marauroad.bat";
    private String appDirPath, appINIFilePath;
    public static final String workingDir = System.getProperty("user.home")
            + System.getProperty("file.separator") + ".Marauroa-Server-Manager"
            + System.getProperty("file.separator");
    private List listeners = Collections.synchronizedList(new LinkedList());
    private String commandLine;
    private IMarauroaProcess process;
    private String host = "localhost";
    private boolean exists;
    EnumMap<ConfigurationElement, Properties> properties =
            new EnumMap<ConfigurationElement, Properties>(ConfigurationElement.class);
    //Populate the ini file
    private Properties props = new Properties(), custom = new Properties();
    private static final Logger logger = Logger.getLogger(MarauroaApplication.class.getSimpleName());
    /*
     * A map of zones and its contents
     */
    private HashMap<String, ArrayList<RPObject>> contents =
            new HashMap<String, ArrayList<RPObject>>();
    private Class relativeToClass = getClass();

    public MarauroaApplication(String name) {
        setName(name);
        app_name = name;
        if (OSValidator.isMac()) {
            setScriptName(replaceExtension(getScriptName(), "sh"));
        }
        if (OSValidator.isUnix()) {
            setScriptName(replaceExtension(getScriptName(), "sh"));
        }
    }

    public MarauroaApplication() {
        setName("Default Marauroa Application");
    }

    public static boolean validateName(String name) {
        return name != null && !name.isEmpty();
    }

    /*
     * For adding custom properties, override this method caling super() then
     * add your properties into the HashMap
     */
    @Override
    public Properties loadINIConfiguration() {
        Properties config = new Properties();
        for (Entry entry : configuration.entrySet()) {
            logger.log(Level.FINE, "{0}: {1}", new Object[]{entry.getKey(),
                        entry.getValue() == null ? "" : entry.getValue().toString()});
            config.put(entry.getKey(), entry.getValue());
        }
        try {
            //Now modify specific properties from the template
            InputStream defaultProperties =
                    getRelativeToClass().getResourceAsStream("default.ini");
            if (defaultProperties != null) {
                config.load(defaultProperties);
                defaultProperties.close();
                File file = new File(getAppINIFilePath());
                file.mkdirs();
                FileOutputStream out = new FileOutputStream(file);
                config.store(out, "");
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
        }
        return config;
    }

    @Override
    public String getName() {
        return app_name;
    }

    @Override
    public final void setName(String name) {
        fire("name", app_name, name);
        app_name = name;
    }

    @Override
    public String getApplicationPath() {
        return getAppDirPath();
    }

    @Override
    public void setApplicationPath(String path) {
        setAppDirPath(path);
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        fire("version", this.version, version);
        this.version = version;
    }

    @Override
    public boolean start() {
        process = new DefaultMarauroaProcess(this);
        boolean result = true;
        try {
            process.execute();
        } catch (ExecutionException ex) {
            logger.log(Level.SEVERE, null, ex);
            Exceptions.printStackTrace(ex);
            result = false;
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
            Exceptions.printStackTrace(ex);
            result = false;
        }
        return result;
    }

    @Override
    public boolean shutdown() {
        if (process != null) {
            process.stop();
        }
        checkState();
        return true;
    }

    @Override
    public Image getIcon(int type) {
        //Use default icon
        return null;
    }

    @Override
    public boolean saveINIFile(Properties config) {
        FileOutputStream out = null;
        try {
            //Create the application directory
            File appDir = new File(getAppDirPath());
            appDir.mkdirs();
            out = new FileOutputStream(getAppINIFilePath());
            config.store(out, "---No Comment---");
            out.close();
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
            Exceptions.printStackTrace(ex);
            return false;
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            Exceptions.printStackTrace(ex);
            return false;
        } finally {
            try {
                out.close();
                configuration = config;
                return true;
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
                Exceptions.printStackTrace(ex);
                return false;
            }
        }
    }

    @Override
    public boolean loadINIFile() {
        FileInputStream in = null;
        try {
            File iniFile = new File(getAppINIFilePath());
            if (!hasINIFile()) {
                return false;
            }
            in = new FileInputStream(iniFile);
            configuration.load(in);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
            Exceptions.printStackTrace(ex);
            return false;
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            Exceptions.printStackTrace(ex);
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                return true;
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
                Exceptions.printStackTrace(ex);
                return false;
            }
        }
    }

    @Override
    public boolean hasINIFile() {
        File ini = new File(getAppINIFilePath());
        return ini != null && ini.exists();
    }

    @Override
    public boolean isRunning() {
        checkState();
        return running;
    }

    @Override
    public void checkState() {
        if (!hasINIFile()) {
            running = false;
            return;
        }
        //Make sure the settings are loaded
        loadINIFile();
        //For now just check if there's something in the TCP port
        try {
            running = !available(Integer.valueOf(configuration.getProperty(ConfigurationElement.TCP_PORT.getName())));
        } catch (NumberFormatException e) {
            //Try to close it just in case.
            running = true;
        }
    }

    /**
     * Checks to see if a specific port is available.
     *
     *
     * @param port the port to check for availability
     * @return true if port is available
     */
    public static boolean available(int port) {
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid port: " + port);
        }
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            //Nothing to log
        } finally {
            if (ds != null) {
                ds.close();
            }
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /*
                     * should not be thrown
                     */
                    logger.log(Level.SEVERE, null, e);
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "MarauroaApplication{" + "running=" + running + ", app_name="
                + app_name + ", version=" + version + ", configuration="
                + configuration + ", appDirPath=" + getAppDirPath()
                + ", appINIFileName=" + getAppINIFileName() + ", appINIFilePath="
                + getAppINIFilePath() + '}';
    }

    @Override
    public String toStringForDisplay() {
        return app_name + (version.isEmpty() ? "" : ": " + version);
    }

    @Override
    public boolean createAppDirectory() {
        if (!appDirExists()) {
            File appDir = new File(getAppDirPath());
            if (Lookup.getDefault().lookup(IAddApplicationDialogProvider.class) != null
                    && !Lookup.getDefault().lookup(IAddApplicationDialogProvider.class).isFolderCreationIgnored()
                    && appDirExists()) {
                DialogDisplayer.getDefault().notifyLater(
                        new NotifyDescriptor.Message(NbBundle.getMessage(
                        MarauroaApplication.class,
                        "application.dir.exists").replaceAll("%d",
                        appDir.toURI().getPath()),
                        NotifyDescriptor.ERROR_MESSAGE));
                return false;
            } else {
                boolean success = appDir.mkdirs();
                if (!success) {
                    DialogDisplayer.getDefault().notifyLater(
                            new NotifyDescriptor.Message(NbBundle.getMessage(
                            MarauroaApplication.class,
                            "add.application.unable.create.dir").replaceAll("%d",
                            getAppDirPath()),
                            NotifyDescriptor.ERROR_MESSAGE));
                } else {
                    //Set to true when something goes wrong so we can cleanup
                    boolean rollback = false;
                    File log = new File(appDir.getAbsoluteFile()
                            + System.getProperty("file.separator")
                            + "log");
                    if (!log.mkdirs()) {
                        Exceptions.printStackTrace(new Exception(
                                "Unable to create log directory: "
                                + log.getAbsolutePath()));
                        rollback = true;
                        success = false;
                    }
                    //Create server.ini file
                    try {
                        if (success) {
                            updateIniFile();
                        }
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                        rollback = true;
                        success = false;
                    }
                    //Create libraries
                    try {
                        if (success) {
                            updateLibs();
                        }
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                        rollback = true;
                        success = false;
                    }
                    //Copy extension libraries
                    if (success) {
                        try {
                            updateExtLibs();
                        } catch (IOException ex) {
                            Exceptions.printStackTrace(ex);
                            rollback = true;
                            success = false;
                        }
                    }
                    //Create marauroad.bat file
                    try {
                        if (success) {
                            updateStartScript();
                        }
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                        rollback = true;
                        success = false;
                    }
                    if (!success) {
                        rollback = true;
                    }
                    if (rollback) {
                        //Just delete the application folder
                        deleteAppDirectory();
                    } else {
                        //At this point everything is fine, register in manager
                        logger.log(Level.INFO,
                                "Created application directory at: {0}", getAppDirPath());
                    }
                }
                return success;
            }
        } else {
            return true;
        }
    }

    @Override
    public boolean deleteAppDirectory() {
        MarauroaApplicationRepository.deleteFolder(new File(getAppDirPath()));
        return true;
    }

    protected void updateLibs() throws IOException {
        OutputHandler.setStatus(NbBundle.getMessage(
                MarauroaApplication.class,
                "application.update.lib.start"));
        //Delete the library contents in case something got removed.
        File dir = new File(getAppDirPath() + System.getProperty("file.separator") + "lib");
        MarauroaApplicationRepository.deleteFolder(dir);
        //Copy the required jars
        copyCoreJarsToDir(dir.getAbsolutePath(), false);
        for (Iterator<File> it = getApplicationJars().iterator(); it.hasNext();) {
            File customLib = it.next();
            if (customLib.isFile()) {
                copyFile(customLib, new File(dir.getAbsolutePath()
                        + System.getProperty("file.separator") + customLib.getName()));
            }
        }
        OutputHandler.setStatus(NbBundle.getMessage(
                MarauroaApplication.class,
                "application.update.lib.end"));
    }

    @Override
    public String getIniPath() {
        return getAppDirPath() + System.getProperty("file.separator")
                + getAppINIFileName();
    }

    /*
     * This method takes care of generating the INI file for the application.
     * The INI file links Marauroa with the application by specifying the
     * following: database_implementation: Class in charge of the database
     * access factory_implementation: Factory in charge of creating objects
     * gameserver_implementation: The application's server manage world: The
     * application's "world" ruleprocessor: The application's Rule processor
     *
     * Don't get fooled by the references to game. Marauroa can be used for
     * applications other than games.
     *
     */
    protected void updateIniFile() throws IOException {
        OutputHandler.setStatus(NbBundle.getMessage(MarauroaApplication.class,
                "application.create.ini.start"));
        File ini = new File(getIniPath());
        if (ini.exists()) {
            props.load(new FileInputStream(ini));
        } else {
            ini.createNewFile();
        }
        exists = !props.isEmpty();
        if (ini.exists()) {
            for (Entry ce : loadINIConfiguration().entrySet()) {
                //Just add new properties. Don't modify values that might have been manually changed
                if (ConfigurationElement.get(ce.getKey().toString()) == null) {
                    //Not a predefined property store in a group of their own
                    custom.put(ce.getKey().toString(), ce.getValue().toString());
                } else {
                    //Only overwrite the server version property
                    if (ce.getKey().toString().equals(ConfigurationElement.SERVER_VERSION.getName())) {
                        props.put(ce.getKey().toString(), ConfigurationElement.SERVER_VERSION.getValue().toString());
                    } else if (!props.containsKey(ce.getKey().toString())) {
                        props.put(ce.getKey().toString(), ce.getValue().toString());
                    }
                }
            }
            //Don't override key if already there
            if ((!props.contains("n") && !props.contains("e") && !props.contains("d"))
                    && (!custom.contains("n") && !custom.contains("e") && !custom.contains("d"))) {
                //Add RSA key
                /*
                 * The size of the RSA Key in bits, usually 512
                 */
                String keySize = "512";
                OutputHandler.setStatus(NbBundle.getMessage(
                        MarauroaApplication.class,
                        "application.create.rsakey"));
                RSAKey rsakey = RSAKey.generateKey(Integer.valueOf(keySize));
                props.put("n", rsakey.getN().toString());
                props.put("e", rsakey.getE().toString());
                props.put("d", rsakey.getD().toString());
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(ini));
            writer.write("#Generated .ini file for " + getName() + " on " + new Date());
            writer.newLine();
            writer.newLine();
            writer.write("#Configurable Section");
            writer.newLine();
            writer.newLine();
            //Use defaults
            outputDBProperties(writer);
            writer.write("#Location of the logging configuration");
            writer.newLine();
            writer.write(getProperty(ConfigurationElement.LOG4J));
            writer.newLine();
            writer.newLine();
            writer.write("#Unless you want this on a different place leave it unchanged.");
            writer.newLine();
            writer.write(getProperty(ConfigurationElement.STATS));
            writer.newLine();
            writer.newLine();
            writer.write("#End Configurable Section. Modify the following properties at your own risk!");
            writer.newLine();
            writer.newLine();
            writer.write(getProperty(ConfigurationElement.DATABASE));
            writer.newLine();
            writer.newLine();
            writer.write(getProperty(ConfigurationElement.FACTORY));
            writer.newLine();
            writer.newLine();
            writer.write(getProperty(ConfigurationElement.UNIVERSE));
            writer.newLine();
            writer.newLine();
            writer.write(getProperty(ConfigurationElement.RULE_PROCESSOR));
            writer.newLine();
            writer.newLine();
            writer.write(getProperty(ConfigurationElement.CLIENT_OBJECT));
            writer.newLine();
            writer.newLine();
            writer.write(getProperty(ConfigurationElement.SERVER_TYPE));
            writer.newLine();
            writer.newLine();
            writer.write(getProperty(ConfigurationElement.SERVER_NAME));
            writer.newLine();
            writer.newLine();
            writer.write(getProperty(ConfigurationElement.SERVER_VERSION));
            writer.newLine();
            writer.newLine();
            writer.write(getProperty(ConfigurationElement.SERVER_WELCOME));
            writer.newLine();
            writer.newLine();
            writer.write(getProperty(ConfigurationElement.SERVER_CONTACT));
            writer.newLine();
            writer.newLine();
            writer.write("#System account credentials!");
            writer.newLine();
            writer.write(getProperty(ConfigurationElement.SYSTEM_PASSWORD));
            writer.newLine();
            writer.newLine();
            writer.write(getProperty(ConfigurationElement.SYSTEM_EMAIL));
            writer.newLine();
            writer.newLine();
            writer.write(getProperty(ConfigurationElement.SYSTEM_NAME));
            writer.newLine();
            writer.newLine();
            writer.write(getProperty(ConfigurationElement.TCP_PORT));
            writer.newLine();
            writer.newLine();
            writer.write(getProperty(ConfigurationElement.TURN_LENGTH));
            writer.newLine();
            writer.newLine();
            writer.write("#Server key. Never change it!");
            writer.newLine();
            writer.write("n = " + props.get("n").toString());
            writer.newLine();
            writer.newLine();
            writer.write("e = " + props.get("e").toString());
            writer.newLine();
            writer.newLine();
            writer.write("d = " + props.get("d").toString());
            writer.newLine();
            writer.newLine();
            writer.write("#Custom properties");
            writer.newLine();
            for (Entry e : custom.entrySet()) {
                writer.write(e.getKey().toString() + " = " + e.getValue().toString());
                writer.newLine();
                writer.newLine();
            }
            writer.write("#Enabled extensions");
            writer.newLine();
            writer.write(getProperty(ConfigurationElement.SERVER_EXTENSION));
            writer.flush();
        }
        OutputHandler.setStatus(NbBundle.getMessage(
                MarauroaApplication.class,
                "application.create.ini.end"));
    }

    private boolean isMysql() {
        if (exists) {
            return getProperty(ConfigurationElement.JDBC_URL).contains("mysql");
        } else {
            return exists;
        }
    }

    private String getProperty(ConfigurationElement ce) {
        //Only write if not already defined
        String property = ce.getName() + "=" + props.getProperty(ce.getName(),
                ce.getValue() == null ? "" : ce.getValue().toString());
        logger.fine(property);
        return property;
    }

    protected void updateStartScript() throws IOException {
        OutputHandler.setStatus(NbBundle.getMessage(
                MarauroaApplication.class,
                "application.create.script.start"));
        if (OSValidator.isMac() && !getScriptName().endsWith("sh")) {
            setScriptName(replaceExtension(getScriptName(), "sh"));
        }
        if (OSValidator.isUnix() && !getScriptName().endsWith("sh")) {
            setScriptName(replaceExtension(getScriptName(), "sh"));
        }
        if (OSValidator.isWindows() && !getScriptName().endsWith("bat")) {
            setScriptName(replaceExtension(getScriptName(), "bat"));
        }
        File marauroad = new File(getAppDirPath() + System.getProperty("file.separator")
                + getScriptName());
        marauroad.createNewFile();
        if (marauroad.exists()) {
            //TODO: FIX
            if (OSValidator.isUnix()) {
                ArrayList<String> commands = new ArrayList<String>();
                commands.add("bin/chmod");
                commands.add("u+x");
                commands.add(marauroad.getName());
                //Need to provide execution permission to the file
                ScriptExecuter se = new ScriptExecuter("Granting Linux Permissions",
                        commands, marauroad.getParentFile().getAbsolutePath());
                try {
                    se.execute();
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (ExecutionException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            //Populate the file
            FileWriter fstream = new FileWriter(marauroad);
            BufferedWriter out = new BufferedWriter(fstream);
            if (OSValidator.isUnix()) {
                out.write("#!/bin/sh");
                out.newLine();
            }
            out.write("java -cp ");
            out.write("\"" + getLibraries() + "\" "
                    + "marauroa.server.marauroad -c server.ini -l");
        }
        OutputHandler.setStatus(NbBundle.getMessage(
                MarauroaApplication.class,
                "application.create.script.end"));
    }

    public boolean appDirExists() {
        return new File(getAppDirPath()).exists();
    }

    public static void copyCoreJarsToDir(String path) {
        copyCoreJarsToDir(path, true);
    }

    public static void copyCoreJarsToDir(String path, boolean recursive) {
        File folder = InstalledFileLocator.getDefault().locate("modules/ext",
                "marauroa.lib", false);
        if (folder != null) {
            try {
                copyContentsOfFolder(folder, path, recursive);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    protected static void copyContentsOfFolder(File folder, String path) throws IOException {
        copyContentsOfFolder(folder, path, true);
    }

    protected static void copyContentsOfFolder(File folder, String path, boolean recursive) throws IOException {
        assert folder.isDirectory();
        for (File lib : folder.listFiles()) {
            if (lib.isDirectory()) {
                if (recursive) {
                    copyContentsOfFolder(lib, path + System.getProperty("file.separator")
                            + lib.getName());
                }
            } else {
                copyFile(lib, new File(path
                        + System.getProperty("file.separator") + lib.getName()));
            }
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists()) {
            //Make sure the new path exists
            destFile.getParentFile().mkdirs();
        }
        if (!sourceFile.exists()) {
            throw new IOException("Source file: "
                    + sourceFile.getAbsolutePath() + " doesn't exist!");
        }
        if (!sourceFile.canRead()) {
            throw new IOException("Source file: "
                    + sourceFile.getAbsolutePath() + " can't be read!");
        }
        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    @Override
    public Collection<File> getApplicationJars() {
        //Place application specific jars in a folder named <application name>
        //as defined in the class extending MarauroaApplication
        ArrayList<File> jars = new ArrayList<File>();
        File marauroa = InstalledFileLocator.getDefault().locate("modules/ext",
                "marauroa.lib", false);
        if (marauroa != null && marauroa.exists()) {
            File folder = new File(marauroa.getAbsolutePath()
                    + System.getProperty("file.separator") + getName());
            if (folder != null && folder.exists()) {
                assert folder.isDirectory();
                for (File file : folder.listFiles()) {
                    if (file.isFile()) {
                        jars.add(file);
                    }
                }
            }
        }
        return jars;
    }

    @Override
    public String getAppINIFileName() {
        return appINIFileName;
    }

    /**
     * @param appINIFileName the appINIFileName to set
     */
    @Override
    public void setAppINIFileName(String appINIFileName) {
        fire("appINIFileName", this.appINIFileName, appINIFileName);
        this.appINIFileName = appINIFileName;
    }

    /**
     * @return the appDirPath
     */
    public String getAppDirPath() {
        //Update with name
        setApplicationPath(workingDir + "Applications"
                + System.getProperty("file.separator") + getName());
        return appDirPath;
    }

    /**
     * @param appDirPath the appDirPath to set
     */
    public void setAppDirPath(String appDirPath) {
        fire("appDirPath", this.appDirPath, appDirPath);
        this.appDirPath = appDirPath;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        listeners.add(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        listeners.remove(pcl);
    }

    private void fire(String propertyName, Object old, Object nue) {
        //Passing 0 below on purpose, so you only synchronize for one atomic call:
        PropertyChangeListener[] pcls = (PropertyChangeListener[]) listeners.toArray(new PropertyChangeListener[0]);
        for (int i = 0; i < pcls.length; i++) {
            pcls[i].propertyChange(new PropertyChangeEvent(this, propertyName, old, nue));
        }
    }

    @Override
    public void setDefaultCommand() {
    }

    @Override
    public String getCommandLine() {
        return this.commandLine;
    }

    @Override
    public void setCommandLine(String command) {
        this.commandLine = command;
    }

    /**
     * @return the scriptName
     */
    @Override
    public final String getScriptName() {
        return scriptName;
    }

    /**
     * @param scriptName the scriptName to set
     */
    @Override
    public final void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    @Override
    public void update() {
        try {
            if (isRunning()) {
                shutdown();
            }
            updateLibs();
            //Copy extension libraries
            updateExtLibs();
            //Recreate script in case update lib added/removed libraries
            File script = new File(getScriptName());
            if (script.exists()) {
                script.delete();
            }
            updateStartScript();

            //Check ini file
            updateIniFile();

            //Make sure the log folder is there
            File log = new File(getAppDirPath()
                    + System.getProperty("file.separator")
                    + "log");
            log.mkdirs();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            DialogDisplayer.getDefault().notifyLater(
                    new NotifyDescriptor.Message(NbBundle.getMessage(
                    MarauroaApplication.class,
                    "application.update.lib.error"),
                    NotifyDescriptor.ERROR_MESSAGE));
        }
    }

    /**
     * @return the appINIFilePath
     */
    public String getAppINIFilePath() {
        setAppINIFilePath(getAppDirPath()
                + System.getProperty("file.separator") + getAppINIFileName());
        return appINIFilePath;
    }

    /**
     * @param appINIFilePath the appINIFilePath to set
     */
    public void setAppINIFilePath(String appINIFilePath) {
        this.appINIFilePath = appINIFilePath;
    }

    private String replaceExtension(String origName, String extension) {
        assert origName != null : "Parameter origName can't be null";
        assert !origName.isEmpty() : "Parameter origName can't be empty";
        assert extension != null : "Parameter extension can't be null";
        assert !extension.isEmpty() : "Parameter extension can't be empty";
        StringBuilder updated = new StringBuilder();
        if (!origName.contains(".")) {
            updated.append(origName).append(".").append(extension);
        } else {
            updated.append(origName.substring(0, origName.lastIndexOf('.') + 1)).append(extension);
        }
        return updated.toString();
    }

    @Override
    public String getLibraries() {
        //Create from application directory
        StringBuilder libs = new StringBuilder();
        File libDir = new File(getAppDirPath()
                + System.getProperty("file.separator") + "lib");
        if (libDir.exists()) {
            listLibrariesInFolder(libDir, libs);
        }
        File extLibDir = new File(getAppDirPath()
                + System.getProperty("file.separator") + "lib"
                + System.getProperty("file.separator") + "extensions");
        if (extLibDir.exists()) {
            listLibrariesInFolder(extLibDir, libs);
        }
        return libs.toString();
    }

    private void listLibrariesInFolder(File folder, StringBuilder libs) {
        if (folder.exists()) {
            for (File lib : folder.listFiles()) {
                if (!lib.isDirectory()) {
                    if (!libs.toString().isEmpty()) {
                        if (OSValidator.isUnix()) {
                            libs.append(":");
                        }
                        if (OSValidator.isWindows()) {
                            libs.append(";");
                        }
                        if (OSValidator.isMac()) {
                            libs.append(";");
                        }
                    }
                    if (folder.getParentFile().getName().equals("lib")) {
                        libs.append(folder.getParentFile().getName()).append(System.getProperty("file.separator"));
                    }
                    libs.append(folder.getName()).append(System.getProperty("file.separator")).append(lib.getName());
                } else {
                    listLibrariesInFolder(lib, libs);
                }
            }
        }
    }

    @Override
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getAccountName() {
        return props.getProperty(ConfigurationElement.SYSTEM_NAME.getName());
    }

    @Override
    public String getPassword() {
        return props.getProperty(ConfigurationElement.SYSTEM_PASSWORD.getName());
    }

    @Override
    public int getPort() {
        return Integer.valueOf(props.getProperty(ConfigurationElement.TCP_PORT.getName()));
    }

    @Override
    public void addObject(RPObject object) {
        if (!contents.containsKey(object.get("zoneid"))) {
            //New zone, add it to the list
            getContents().put(object.get("zoneid"), new ArrayList<RPObject>());
        }
        boolean add = getContents().get(object.get("zoneid")).add(object);
        Logger.getLogger(DefaultMarauroaApplication.class.getSimpleName()).log(Level.INFO,
                "Added {0} {1}", new Object[]{object, add ? "successfully" : "unsuccesfully"});
        setStatus(STATUS.UPDATED);
    }

    @Override
    public void removeObject(RPObject object) {
        if (getContents().containsKey(object.get("zoneid"))) {
            boolean remove = getContents().get(object.get("zoneid")).remove(object);
            Logger.getLogger(DefaultMarauroaApplication.class.getSimpleName()).log(Level.INFO,
                    "Removed {0} {1}", new Object[]{object, remove ? "successfully" : "unsuccesfully"});
            setStatus(STATUS.UPDATED);
        }
    }

    /**
     * @return the contents
     */
    @Override
    public HashMap<String, ArrayList<RPObject>> getContents() {
        return contents;
    }

    @Override
    public void setStatus(STATUS applicationStatus) {
        switch (applicationStatus) {
            case STOPPED:
                status.remove(STATUS.STARTED);
                status.remove(STATUS.CONNECTED);
                setStatus(STATUS.DISCONNECTED);
                break;
            case STARTED:
                status.remove(STATUS.STOPPED);
                break;
            case CONNECTED:
                status.remove(STATUS.DISCONNECTED);
                break;
            case DISCONNECTED:
                status.remove(STATUS.CONNECTED);
                break;
            default:
            //Do nothing
        }
        if (!status.contains(applicationStatus)) {
            status.add(applicationStatus);
        }
        notifyListeners();
    }

    @Override
    public ArrayList<STATUS> getStatus() {
        return status;
    }

    @Override
    public void addStatusListener(ApplicationStatusChangeListener listener) {
        if (!statusListeners.contains(listener)) {
            statusListeners.add(listener);
        }
    }

    @Override
    public void removeStatusListener(ApplicationStatusChangeListener listener) {
        statusListeners.remove(listener);
    }

    private void notifyListeners() {
        for (ApplicationStatusChangeListener listener : statusListeners) {
            listener.statusChanged();
        }
    }

    private void outputDBProperties(BufferedWriter writer) throws IOException {
        writer.write("#Pre-defined Mysql connection");
        writer.newLine();
        if (isMysql()) {
            writer.write(getProperty(ConfigurationElement.JDBC_URL));
        } else {
            writer.write("#" + getAlternateProperty(ConfigurationElement.JDBC_URL));
        }
        writer.newLine();
        writer.newLine();
        writer.write("#Pre-defined Mysql driver");
        writer.newLine();
        if (isMysql()) {
            writer.write(getProperty(ConfigurationElement.JDBC_CLASS));
        } else {
            writer.write("#" + getAlternateProperty(ConfigurationElement.JDBC_CLASS));
        }
        writer.newLine();
        writer.newLine();
        writer.write("#To use Marauroa's MySQL support also uncomment next line");
        writer.newLine();
        if (isMysql()) {
            writer.write(getProperty(ConfigurationElement.DATABASE_ADAPTER));
        } else {
            writer.write("#" + getAlternateProperty(ConfigurationElement.DATABASE_ADAPTER));
        }
        writer.newLine();
        writer.newLine();
        writer.write("#Pre-defined embedded database. Not for production use");
        writer.newLine();
        if (isMysql()) {
            writer.write("#" + ConfigurationElement.JDBC_URL.getName() + " = "
                    + ConfigurationElement.JDBC_URL.getValue());
        } else {
            writer.write(getProperty(ConfigurationElement.JDBC_URL));
        }
        writer.newLine();
        writer.newLine();
        writer.write("#Pre-defined embedded database. Not for production use");
        writer.newLine();
        if (isMysql()) {
            writer.write("#" + ConfigurationElement.JDBC_CLASS.getName() + " = "
                    + ConfigurationElement.JDBC_CLASS.getValue());
        } else {
            writer.write(getProperty(ConfigurationElement.JDBC_CLASS));
        }
        writer.newLine();
        writer.newLine();
        writer.write("#To use Marauroa's H2 support also uncomment next line");
        writer.newLine();
        if (isMysql()) {
            writer.write("#" + ConfigurationElement.DATABASE_ADAPTER.getName() + " = "
                    + ConfigurationElement.DATABASE_ADAPTER.getValue());
        } else {
            writer.write(getProperty(ConfigurationElement.DATABASE_ADAPTER));
        }
        writer.newLine();
        writer.newLine();
        writer.write("#Database settings. Configure to meet your database settings");
        writer.newLine();
        writer.write(getProperty(ConfigurationElement.JDBC_USER));
        writer.newLine();
        writer.newLine();
        writer.write(getProperty(ConfigurationElement.JDBC_PWD));
        writer.newLine();
        writer.newLine();
    }

    private String getAlternateProperty(ConfigurationElement ce) {
        return ce.getName() + "=" + ce.getAlternate().toString();
    }

    /**
     * @return the relativeToClass
     */
    protected Class getRelativeToClass() {
        return relativeToClass;
    }

    /**
     * @param relativeToClass the relativeToClass to set
     */
    protected void setRelativeToClass(Class relativeToClass) {
        this.relativeToClass = relativeToClass;
    }

    private void updateExtLibs() throws IOException {
        File folder = InstalledFileLocator.getDefault().locate("modules/ext",
                "marauroa.lib", false);
        if (folder != null) {
            //Delete the library contents in case something got removed.
            File dir = new File(getAppDirPath() + System.getProperty("file.separator") + "lib");
            MarauroaApplicationRepository.deleteFolder(dir);
            copyContentsOfFolder(folder, dir.getAbsolutePath(), true);
        }
    }
}
