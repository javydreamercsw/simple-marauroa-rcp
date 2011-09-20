package simple.marauroa.client;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import marauroa.client.BannedAddressException;
import marauroa.client.ClientFramework;
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
import simple.marauroa.application.core.MarauroaApplicationRepository;
import simple.marauroa.client.components.common.MCITool;

public class Installer extends ModuleInstall {

    private static final Logger logger = Logger.getLogger(Installer.class.getName());
    private String APPLICATION_FOLDER;

    @Override
    public void restored() {
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

            @Override
            public void run() {
                try {
                    MarauroaApplicationRepository.get();
                    //Initialize Client Configuration
                    ClientGameConfiguration.setRelativeTo(MarauroaSimpleClient.class);
                    /**
                     * We set the main game folder to the game name
                     */
                    APPLICATION_FOLDER = System.getProperty("user.home")
                            + System.getProperty("file.separator")
                            + "." + MCITool.getClient().getGameName()
                            + System.getProperty("file.separator");
                    startLogSystem();
                    //Check for valid profiles
                    MCITool.getLoginManager().checkProfiles();
                    //Show login screen
                    MCITool.getLoginManager().displayLoginManager();
                    //Wait for the login process to finish
                    MCITool.getLoginManager().waitUntilDone();
                    //Now start client specific modules
                    MCITool.getClient().startModules();
                } catch (Exception e) {
                    DialogDisplayer.getDefault().notify(
                            new NotifyDescriptor.Message(NbBundle.getMessage(
                            Installer.class,
                            "error.startup"),
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
        Log4J.init("log4j.properties");
        logger.log(Level.INFO, "Setting base at: {0}", APPLICATION_FOLDER);
        logger.log(Level.INFO, "{0} {1}", new Object[]{MCITool.getClient().getGameName(),
                    MCITool.getClient().getVersionNumber()});
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

    @Override
    public void close() {
        try {
            if (MCITool.getClient() != null) {
                ((ClientFramework) MCITool.getClient()).logout();
            }
        } catch (BannedAddressException ex) {
            Exceptions.printStackTrace(ex);
        } catch (TimeoutException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvalidVersionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
