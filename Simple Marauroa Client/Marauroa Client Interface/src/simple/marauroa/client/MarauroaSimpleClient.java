package simple.marauroa.client;

import marauroa.common.game.AccountResult;
import simple.marauroa.client.components.api.IClientFramework;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import marauroa.client.BannedAddressException;
import marauroa.client.LoginFailedException;
import marauroa.client.TimeoutException;
import marauroa.common.game.CharacterResult;
import marauroa.common.game.RPAction;
import marauroa.common.game.RPObject;
import marauroa.common.net.InvalidVersionException;
import marauroa.common.net.message.MessageS2CPerception;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDisplayer;
import org.openide.LifecycleManager;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.TaskListener;
import org.openide.util.lookup.ServiceProvider;
import org.xml.sax.SAXException;
import simple.client.SimpleClient;
import simple.client.action.update.ClientGameConfiguration;
import simple.client.conf.ExtensionXMLLoader;
import simple.client.soundreview.SoundMaster;
import simple.marauroa.client.components.common.MCITool;
import simple.server.core.action.chat.ChatAction;
import simple.server.core.event.PrivateTextEvent;
import simple.server.core.event.TextEvent;
import static simple.server.core.action.WellKnownActionConstants.*;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 * A base class for the Simple client UI (not GUI).
 *
 * This should have minimal UI-implementation dependent code. That's what
 * sub-classes are for!
 */
@ServiceProvider(service = IClientFramework.class)
public class MarauroaSimpleClient extends SimpleClient implements
        IClientFramework {

    private boolean loginDone = false, profileReady = false;
    private List<String> quotes = new ArrayList<String>();
    private static ExtensionXMLLoader extensionLoader;
    private static String confPath;
    private static boolean extLoaded = false;
    protected String userName;
    private static final Logger logger = Logger.getLogger(MarauroaSimpleClient.class.getSimpleName());
    /**
     * default folder
     */
    public static String APPLICATION_FOLDER;
    protected Class configRelativeTo;
    private String character;
    private String password;
    private String port;
    private String host;
    private boolean gotCredentials;
    private final static RequestProcessor RP = new RequestProcessor(
            "Server Interaction Process", 1, false);
    final ProgressHandle ph = ProgressHandleFactory.createHandle(
            "Talking with server");
    private RequestProcessor.Task theTask = null;
    private boolean running = false;

    public MarauroaSimpleClient() {
        super(LOG4J_PROPERTIES);
        if (configRelativeTo == null && !getClass().equals(MarauroaSimpleClient.class)) {
            DialogDisplayer.getDefault().notify(
                    new NotifyDescriptor.Message(NbBundle.getMessage(
                    MarauroaSimpleClient.class,
                    "invalid.client.setup"),
                    NotifyDescriptor.ERROR_MESSAGE));
            LifecycleManager.getDefault().exit();
        }
        gameName = ClientGameConfiguration.get("GAME_NAME");
        versionNumber = ClientGameConfiguration.get("GAME_VERSION");
        /** We set the main game folder to the game name */
        APPLICATION_FOLDER = System.getProperty("user.home")
                + System.getProperty("file.separator")
                + "." + gameName + System.getProperty("file.separator");
        startSoundMaster();
        startSwingLookAndFeel();
    }

    @Override
    protected void registerListeners() {
        if (MCITool.getChatManager() != null) {
            //Register the chat component
            userContext.registerRPEventListener(new TextEvent(),
                    MCITool.getChatManager());
            userContext.registerRPEventListener(new PrivateTextEvent(),
                    MCITool.getChatManager());
        }
    }

    /**
     * @return the world_objects
     */
    public Map<RPObject.ID, RPObject> getWorld_objects() {
        return world_objects;
    }

    /**
     * @return the character
     */
    public String getCharacter() {
        return character;
    }

    @Override
    protected void onAvailableCharacters(String[] characters) {
        //See onAvailableCharacterDetails
    }

    @Override
    protected void onAvailableCharacterDetails(Map<String, RPObject> characters) {
        // if there are no characters, create one with the specified name automatically
        if (characters.isEmpty()) {
            try {
                logger.log(Level.WARNING,
                        "The requested character is not available, trying to create character {0}", getCharacter());
                final RPObject template = new RPObject();
                final CharacterResult result = createCharacter(getCharacter(), template);
                if (result.getResult().failed()) {
                    logger.log(Level.SEVERE, result.getResult().getText());
                }
                return;
            } catch (TimeoutException ex) {
                Exceptions.printStackTrace(ex);
            } catch (InvalidVersionException ex) {
                Exceptions.printStackTrace(ex);
            } catch (BannedAddressException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        // autologin if a valid character was specified.
        if ((getCharacter() != null) && (characters.keySet().contains(getCharacter()))) {
            try {
                chooseCharacter(getCharacter());
            } catch (final Exception e) {
                logger.log(Level.INFO, MarauroaSimpleClient.class.getSimpleName()
                        + "::onAvailableCharacters{0}", e);
            }
            return;
        }
    }

    @Override
    protected void onPerception(MessageS2CPerception message) {
        try {
            super.onPerception(message);
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.INFO, "Received perception {0}", message.getPerceptionTimestamp());
                int i = message.getPerceptionTimestamp();

                sendMessage("Hi");
                if (i % 50 == 0) {
                    sendMessage("Hi");
                } else if (i % 100 == 0) {
                    sendMessage("How are you?");
                }
                if (logger.isLoggable(Level.FINEST)) {
                    logger.log(Level.INFO, "<World contents ------------------------------------->");
                    int j = 0;
                    for (RPObject object : getWorld_objects().values()) {
                        j++;
                        logger.log(Level.INFO, "{0}. {1}", new Object[]{j, object});
                    }
                    logger.log(Level.INFO, "</World contents ------------------------------------->");
                }
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void run() {
        ph.start();
        running = true;
        if (gotCredentials) {
            ph.switchToDeterminate(4);
            try {
                connect(host, Integer.parseInt(port));
                ph.progress("Connected", 1);
                logger.log(Level.INFO, "Logging as: {0} with pass: {1}",
                        new Object[]{userName,
                            (logger.isLoggable(Level.FINEST) ? password : "*******")});
                login(userName, password);
                ph.progress("Logged in", 2);
            } catch (Exception e) {
                try {
                    logger.log(Level.INFO, "Creating account and logging in to continue....");
                    AccountResult result = createAccount(userName, password, host);
                    if (!result.failed()) {
                        logger.log(Level.INFO, "Logging as: {0}", userName);
                        login(userName, password);
                        ph.progress("Logged in", 2);
                    } else {
                        logger.log(Level.SEVERE, "Unable to create account: {0}.\n{1}",
                                new Object[]{userName, result.getResult()});
                        DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                                "Unable to create account: " + userName + ".\n"
                                + result.getResult(),
                                "Account creation failed", // title of the dialog
                                NotifyDescriptor.PLAIN_MESSAGE,
                                NotifyDescriptor.ERROR_MESSAGE,
                                null,
                                NotifyDescriptor.OK_OPTION // default option is "Cancel"
                                ));
                        //Don't exit, let them retry.
                        ph.finish();
                    }
                } catch (LoginFailedException ex) {
                    logger.log(Level.SEVERE, null, ex);
                    DialogDisplayer.getDefault().notify(new NotifyDescriptor(e.getMessage(),
                            "Login failed", // title of the dialog
                            NotifyDescriptor.PLAIN_MESSAGE,
                            NotifyDescriptor.ERROR_MESSAGE,
                            null,
                            NotifyDescriptor.OK_OPTION // default option is "Cancel"
                            ));
                    //Don't exit, let them retry.
                    ph.finish();
                    return;
                } catch (TimeoutException ex) {
                    logger.log(Level.SEVERE, null, ex);
                    DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                            "Server is not available right now. The server may be "
                            + "down or, if you are using a custom server, you may "
                            + "have entered its name and port number incorrectly.",
                            "Error Logging In", // title of the dialog
                            NotifyDescriptor.PLAIN_MESSAGE,
                            NotifyDescriptor.ERROR_MESSAGE,
                            null,
                            NotifyDescriptor.OK_OPTION // default option is "Cancel"
                            ));
                    exit();
                } catch (InvalidVersionException ex) {
                    logger.log(Level.SEVERE, null, ex);
                    DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                            "You are running an incompatible version of "
                            + ClientGameConfiguration.get("GAME_NAME")
                            + ". Please update",
                            "Invalid version", // title of the dialog
                            NotifyDescriptor.PLAIN_MESSAGE,
                            NotifyDescriptor.ERROR_MESSAGE,
                            null,
                            NotifyDescriptor.OK_OPTION // default option is "Cancel"
                            ));
                    exit();
                } catch (BannedAddressException ex) {
                    logger.log(Level.SEVERE, null, ex);
                    DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                            "You IP is banned. If you think this is not right. "
                            + "Please send a Support request to "
                            + ClientGameConfiguration.get("SUPPORT"),
                            "IP Banned", // title of the dialog
                            NotifyDescriptor.PLAIN_MESSAGE,
                            NotifyDescriptor.ERROR_MESSAGE,
                            null,
                            NotifyDescriptor.OK_OPTION // default option is "Cancel"
                            ));
                    //Don't exit, let them retry.
                    return;
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, null, ex);
                    DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                            "Make sure the target server is correct and available."
                            + "Please send a Support request to "
                            + ClientGameConfiguration.get("SUPPORT"),
                            "Unexpected error", // title of the dialog
                            NotifyDescriptor.PLAIN_MESSAGE,
                            NotifyDescriptor.ERROR_MESSAGE,
                            null,
                            NotifyDescriptor.OK_OPTION // default option is "Cancel"
                            ));
                }
            }
            MCITool.getClient().setAccountUsername(userName);
            ph.progress("Processing", 3);
            MCITool.getClient().setLoginDone(true);
            ph.progress("Done!", 4);
            boolean cond = true;

            ph.switchToIndeterminate();
            ph.progress("Connected to server");
            ph.finish();
            while (cond) {
                loop(0);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        } else {
            DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                    "Unable to proceed without credentials.\n "
                    + "If you see this error contact the application's developer "
                    + "since this is a programming error.",
                    "Unable to proceed", // title of the dialog
                    NotifyDescriptor.PLAIN_MESSAGE,
                    NotifyDescriptor.ERROR_MESSAGE,
                    null,
                    NotifyDescriptor.OK_OPTION // default option is "Cancel"
                    ));
            ph.finish();
        }
    }

    private void exit() {
        ph.finish();
        MCITool.getClient().setLoginDone(true);
        LifecycleManager.getDefault().exit();
    }

    String popQuote() {
        if (quotes.isEmpty()) {
            return null;
        }
        String result = quotes.get(0);
        quotes.remove(0);
        return result;
    }

    @Override
    public void sendMessage(String text) {
        RPAction action;
        action = new RPAction();
        action.put("type", ChatAction._CHAT);
        action.put(TEXT, text);
        send(action);
    }

    @Override
    public String getGameName() {
        return gameName;
    }

    @Override
    public String getVersionNumber() {
        return versionNumber;
    }

    /**
     * @return the confPath
     */
    public static String getConfPath() {
        return confPath;
    }

    /**
     * @param confPath the confPath to set
     */
    public static void setConfPath(String cP) {
        if (!extLoaded) {
            confPath = cP;
            logger.log(Level.INFO, "Loading extensions from: {0}", confPath);
            if (extensionLoader == null) {
                extensionLoader = new ExtensionXMLLoader();
                try {
                    extensionLoader.load(new URI(getConfPath()));
                    extLoaded = true;
                } catch (SAXException ex) {
                    logger.log(Level.SEVERE, null, ex);
                    Exceptions.printStackTrace(ex);
                } catch (URISyntaxException ex) {
                    logger.log(Level.SEVERE, null, ex);
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    /**
     * @param gameName the gameName to set
     */
    @Override
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    /**
     * @param versionNumber the versionNumber to set
     */
    @Override
    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Override
    public void setLoginDone(boolean done) {
        loginDone = done;
    }

    /**
     * @return the loginDone
     */
    @Override
    public boolean isLoginDone() {
        return loginDone;
    }

    @Override
    public void setProfileReady(boolean ready) {
        profileReady = ready;
    }

    @Override
    public boolean isProfileReady() {
        return profileReady;
    }

    @Override
    public String getLogPropertiesPath() {
        return System.getProperty("file.separator") + "log";
    }

    /**
     * Set Account username
     * @param username
     */
    @Override
    public void setAccountUsername(String username) {
        userName = username;
    }

    /**
     * Get account username
     * @return
     */
    @Override
    public String getAccountUsername() {
        return userName;
    }

    /**
     * Start the Sound System
     */
    private static void startSoundMaster() {
        SoundMaster sm = new SoundMaster();
        sm.init();
        Thread th = new Thread(sm);
        th.start();
    }

    /**
     * Try to use the system look and feel.
     */
    private static void startSwingLookAndFeel() {
        try {
            if (System.getProperty("os.name", "").toLowerCase().indexOf("windows") > -1) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Can't change Look&Feel to match your OS. "
                    + "Using the Cross-Platform look & feel", e);
        }
    }

    @Override
    public void provideCredentials(String host, String username, String password,
            String character, final String port) {
        this.userName = username;
        this.password = password;
        this.port = port;
        this.host = host;
        this.character = character;
        gotCredentials = true;
        if (!running) {
            //Now connect
            theTask = RP.create(this); //the task is not started yet

            theTask.addTaskListener(new TaskListener() {

                @Override
                public void taskFinished(org.openide.util.Task task) {
                    if (running) {
                        ph.finish();
                    }
                    running = false;
                }
            });
            theTask.schedule(0); //start the task
        }
    }

    @Override
    public void sendPrivateText(String mess, String target) {
        RPAction action;
        action = new RPAction();
        action.put("type", ChatAction._PRIVATE_CHAT);
        action.put(TARGET, target);
        action.put(TEXT, mess);
        send(action);
    }
}
