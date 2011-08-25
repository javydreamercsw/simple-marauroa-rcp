package simple.marauroa.client.components.login;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import marauroa.client.BannedAddressException;
import marauroa.client.TimeoutException;
import marauroa.common.Log4J;
import marauroa.common.net.InvalidVersionException;
import org.openide.DialogDisplayer;
import org.openide.LifecycleManager;
import org.openide.NotifyDescriptor;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import simple.client.action.update.ClientGameConfiguration;
import simple.common.NotificationType;
import simple.marauroa.application.core.MarauroaApplicationRepository;
import simple.marauroa.client.components.common.MCITool;

/**
 * Manages a module's life cycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    private static final Logger logger = Logger.getLogger(Installer.class.getName());
    private String gameName;
    private String versionNumber;
    private String APPLICATION_FOLDER;

    @Override
    public void restored() {
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

            @Override
            public void run() {
                try {
                    //Initialize Client Configuration
                    ClientGameConfiguration.setRelativeTo(LoginManager.class);
                    gameName = ClientGameConfiguration.get("GAME_NAME");
                    versionNumber = ClientGameConfiguration.get("GAME_VERSION");
                    /** We set the main game folder to the game name */
                    APPLICATION_FOLDER = System.getProperty("user.home")
                            + System.getProperty("file.separator")
                            + "." + getGameName() + System.getProperty("file.separator");
                    startLogSystem();
                    MarauroaApplicationRepository.get();
                    //Check for valid profiles
                    MCITool.getLoginManager().checkProfiles();
                    //Show login screen
                    MCITool.getLoginManager().displayLoginManager();
                    MCITool.getLoginManager().waitUntilDone();
                    //Start the chat window after successful login
                    if (MCITool.getChatManager() != null) {
                        MCITool.getChatManager().addLine("System", NbBundle.getMessage(
                                LoginManager.class,
                                "welcome.message"), NotificationType.NORMAL);
                    }
                } catch (Exception e) {
                    DialogDisplayer.getDefault().notify(
                            new NotifyDescriptor.Message(NbBundle.getMessage(
                            LoginManager.class,
                            "error.startup")    ,
                            NotifyDescriptor.ERROR_MESSAGE));
                    Exceptions.printStackTrace(e);
                    LifecycleManager.getDefault().exit();
                }
            }
        });
    }

    /**
     * Starts the LogSystem.
     */
    private void startLogSystem() {
        //Create log folder
        File log = new File(APPLICATION_FOLDER + "log");
        if (!log.exists()) {
            logger.log(Level.INFO, "Creating log folder at:{0}log", APPLICATION_FOLDER);
            log.mkdirs();
        }
        Log4J.init("simple/marauroa/client/components/login/log4j.properties");
        logger.log(Level.INFO, "Setting base at: {0}", APPLICATION_FOLDER);
        logger.log(Level.INFO, "{0} {1}", new Object[]{getGameName(),
                    getVersionNumber()});
        String patchLevel = System.getProperty("sun.os.patch.level");
        if ((patchLevel == null) || (patchLevel.equals("unknown"))) {
            patchLevel = "";
        }
        logger.log(Level.INFO, "OS: {0} {1} {2} {3}", new Object[]{
                    System.getProperty("os.name"), patchLevel,
                    System.getProperty("os.version"),
                    System.getProperty("os.arch")});
        logger.log(Level.INFO, "Java-Runtime: {0} {1} from {2}", new Object[]{
                    System.getProperty("java.runtime.name"),
                    System.getProperty("java.runtime.version"),
                    System.getProperty("java.home")});
        logger.log(Level.INFO, "Java-VM: {0} {1} {2}", new Object[]{
                    System.getProperty("java.vm.vendor"),
                    System.getProperty("java.vm.name"),
                    System.getProperty("java.vm.version")});
    }

    /**
     * @return the gameName
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * @return the versionNumber
     */
    public String getVersionNumber() {
        return versionNumber;
    }

    @Override
    public void close() {
        try {
            MCITool.getClient().logout();
        } catch (BannedAddressException ex) {
            Exceptions.printStackTrace(ex);
        } catch (TimeoutException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvalidVersionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
