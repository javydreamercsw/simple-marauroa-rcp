package simple.marauroa.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import marauroa.client.BannedAddressException;
import marauroa.client.LoginFailedException;
import marauroa.client.TimeoutException;
import marauroa.client.extension.MarauroaClientExtension;
import marauroa.common.game.*;
import marauroa.common.net.InvalidVersionException;
import marauroa.common.net.message.MessageS2CPerception;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDisplayer;
import org.openide.LifecycleManager;
import org.openide.NotifyDescriptor;
import org.openide.util.*;
import org.openide.util.lookup.ServiceProvider;
import simple.client.SimpleClient;
import simple.client.action.update.ClientGameConfiguration;
import simple.client.event.listener.RPEventListener;
import simple.marauroa.application.core.EventBus;
import simple.marauroa.application.core.LookupRPObjectManager;
import simple.marauroa.client.components.api.IClientFramework;
import simple.marauroa.client.components.common.MCITool;
import simple.server.core.action.WellKnownActionConstant;
import simple.server.core.action.chat.ChatAction;
import simple.server.core.event.PrivateTextEvent;
import simple.server.core.event.SimpleRPEvent;
import simple.server.core.event.TextEvent;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com> A base class for the
 * Simple client UI (not GUI).
 *
 * This should have minimal UI-implementation dependent code. That's what
 * sub-classes are for!
 */
@ServiceProvider(service = IClientFramework.class)
public class MarauroaSimpleClient extends SimpleClient implements
        IClientFramework {

    private boolean loginDone = false, profileReady = false;
    protected String userName;
    private final int bufferSize = 10;
    private ArrayList<String> processedEvents = new ArrayList<String>(bufferSize);
    private static final Logger logger =
            Logger.getLogger(MarauroaSimpleClient.class.getSimpleName());
    /**
     * default folder
     */
    private static String APPLICATION_FOLDER;
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
    private int duplicateCounter = 0;
    /**
     * When enabled zone is notified when a player enters/exit the zone.
     */
    private boolean chatNotifications = true;

    public MarauroaSimpleClient() {
        super(LOG4J_PROPERTIES);
        if (configRelativeTo == null && !getClass().equals(MarauroaSimpleClient.class)) {
            DialogDisplayer.getDefault().notify(
                    new NotifyDescriptor.Message(NbBundle.getMessage(
                    MarauroaSimpleClient.class,
                    "invalid.client.setup"),
                    NotifyDescriptor.ERROR_MESSAGE));
            LifecycleManager.getDefault().exit();
        } else {
            configRelativeTo = getClass();
        }
        ClientGameConfiguration.setRelativeTo(configRelativeTo);
        gameName = ClientGameConfiguration.get("GAME_NAME");
        versionNumber = ClientGameConfiguration.get("GAME_VERSION");
        /**
         * We set the main game folder to the game name
         */
        APPLICATION_FOLDER = System.getProperty("user.home")
                + System.getProperty("file.separator")
                + "." + gameName + System.getProperty("file.separator");
        startSwingLookAndFeel();
        if (logger.isLoggable(Level.FINE)) {
            EventBus.getDefault().getCentralLookup().setShowContents(true);
        }
    }

    @Override
    protected void registerListeners() {
        //Override SimpleClient registration
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
                logger.log(Level.SEVERE, MarauroaSimpleClient.class.getSimpleName()
                        + "onAvailableCharactersDetails\n{0}", e);
            }
        }
    }

    @Override
    protected void onPerception(MessageS2CPerception message) {
        try {
            super.onPerception(message);
            if (logger.isLoggable(Level.FINEST)) {
                logger.log(Level.FINEST, "<World contents ------------------------------------->");
                int j = 0;
                for (RPObject object : world.getWorldObjects().values()) {
                    j++;
                    logger.log(Level.FINEST, "{0}. {1}", new Object[]{j, object});
                }
                logger.log(Level.FINEST, "</World contents ------------------------------------->");
            }
        } catch (Exception ex) {
            logger.log(Level.WARNING, null, ex);
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
                            (logger.isLoggable(Level.INFO) ? password : "*******")});
                login(userName, password);
                ph.progress("Logged in", 2);
                logger.info("Log in successful!");
            } catch (Exception e) {
                logger.info("Trying to create account, it might not exist...");
                createAccount();
            }
            MCITool.getClient().setAccountUsername(userName);
            ph.progress("Processing", 3);
            MCITool.getLoginManager().setAttemptLogin(false);
            MCITool.getClient().setLoginDone(true);
            ph.progress("Done!", 4);
            ph.switchToIndeterminate();
            ph.progress("Connected to server");
            ph.finish();
            while (getConnectionState()) {
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

    private void createAccount() {
        try {
            logger.log(Level.INFO, "Creating account and logging in to continue....");
            AccountResult result = createAccount(userName, password, host);
            if (!result.failed()) {
                logger.log(Level.INFO, "Logging as: {0} with pass: {1}",
                        new Object[]{userName,
                            (logger.isLoggable(Level.FINEST) ? password : "*******")});
                login(userName, password);
                ph.progress("Logged in", 2);
            } else {
                logger.log(Level.SEVERE, "Unable to create account: {0}.\n{1}",
                        new Object[]{userName, result.getResult()});
                DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                        "Unable to create account: " + userName + ".\n"
                        + result == null ? "" : result.getResult(),
                        "Account creation failed", // title of the dialog
                        NotifyDescriptor.PLAIN_MESSAGE,
                        NotifyDescriptor.ERROR_MESSAGE,
                        null,
                        NotifyDescriptor.OK_OPTION // default option is "Cancel"
                        ));
                exit();
            }
        } catch (LoginFailedException ex) {
            logger.log(Level.SEVERE, null, ex);
            DialogDisplayer.getDefault().notify(new NotifyDescriptor(ex.getMessage(),
                    "Login failed", // title of the dialog
                    NotifyDescriptor.PLAIN_MESSAGE,
                    NotifyDescriptor.ERROR_MESSAGE,
                    null,
                    NotifyDescriptor.OK_OPTION // default option is "Cancel"
                    ));
            //Don't exit, let them retry
            ph.finish();
            MCITool.getClient().setLoginDone(true);
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
            //Don't exit, let them retry
            ph.finish();
            MCITool.getClient().setLoginDone(true);
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
            //Don't exit, let them retry
            ph.finish();
            MCITool.getClient().setLoginDone(true);
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
            //Don't exit, let them retry
            ph.finish();
            MCITool.getClient().setLoginDone(true);
        }
    }

    private void exit() {
        ph.finish();
        MCITool.getClient().setLoginDone(true);
        LifecycleManager.getDefault().exit();
    }

    @Override
    public void sendMessage(String text) {
        RPAction action;
        action = new RPAction();
        action.put("type", ChatAction._CHAT);
        action.put(WellKnownActionConstant.TEXT, text);
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
     *
     * @param username
     */
    @Override
    public void setAccountUsername(String username) {
        userName = username;
    }

    /**
     * Get account username
     *
     * @return Account user name
     */
    @Override
    public String getAccountUsername() {
        return userName;
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
    public void send(RPAction action) {
        //This is just for debugging purposes
        logger.log(Level.FINE, "Sending action: {0}", action);
        super.send(action);
    }

    @Override
    public void sendPrivateText(String mess, String target) {
        RPAction action;
        action = new RPAction();
        action.put("type", ChatAction._PRIVATE_CHAT);
        action.put(WellKnownActionConstant.TARGET, target);
        action.put(WellKnownActionConstant.TEXT, mess);
        send(action);
    }

    @Override
    public void processEvent(RPEvent event) {
        if (event != null) {
            if (!processedEvents.contains(event.get(SimpleRPEvent.EVENT_ID))) {
                logger.log(Level.FINE, "Received event: {0} from server", event);
                processedEvents.add(event.get(SimpleRPEvent.EVENT_ID));
                //Publish the event to the bus so all listeners can react to it
                //TODO: Remove hack while the interfaces are not part of Marauroa 
                if (event.getName().equals(TextEvent.RPCLASS_NAME)) {
                    logger.log(Level.FINE, TextEvent.RPCLASS_NAME);
                    TextEvent textEvent = new TextEvent();
                    textEvent.fill(event);
                    EventBus.getDefault().publish(textEvent);
                } else if (event.getName().equals(PrivateTextEvent.RPCLASS_NAME)) {
                    logger.log(Level.FINE, PrivateTextEvent.RPCLASS_NAME);
                    PrivateTextEvent privateTextEvent = new PrivateTextEvent();
                    privateTextEvent.fill(event);
                    EventBus.getDefault().publish(privateTextEvent);
                } else {
                    RPEvent processed = extensionProcessEvent(event);
                    if (processed != null) {
                        EventBus.getDefault().publish(processed);
                    }
                }
            } else {
                duplicateCounter++;
                logger.log(Level.FINE, "Ignoring duplicated event: {0}. "
                        + "{1} duplicates so far!", new Object[]{event, duplicateCounter});
            }
        } else {
            logger.warning("Received a null event from server.");
        }
    }

    private RPEvent extensionProcessEvent(RPEvent event) {
        RPEvent processed = null;
        for (Iterator<? extends MarauroaClientExtension> it = Lookup.getDefault().lookupAll(MarauroaClientExtension.class).iterator(); it.hasNext();) {
            MarauroaClientExtension extension = it.next();
            RPEvent processEvent = extension.processEvent(event);
            if (processEvent != null) {
                return processEvent;
            }
        }
        return processed;
    }

    @Override
    public void startModules() {
        //To be extended in clients if needed
    }

    /**
     * @return the APPLICATION_FOLDER
     */
    public static String getAPPLICATION_FOLDER() {
        return APPLICATION_FOLDER;
    }

    @Override
    public boolean onAdded(RPObject object) {
        if (object != null) {
            logger.log(Level.FINE, "onAdded object: {0}", object.toAttributeString());
            if (object.getInt("id") > 0) {
                //Check the lookup to see if its already there
                for (RPObject player : EventBus.getDefault().lookupAll(RPObject.class)) {
                    if (player.get("name").equals(object.get("name"))) {
                        EventBus.getDefault().getCentralLookup().remove(player);
                    }
                }
                //Add it to the lookup
                EventBus.getDefault().getCentralLookup().add(object);
            }
        }
        return true;
    }

    @Override
    public boolean onDeleted(RPObject object) {
        logger.log(Level.FINE, "onDeleted object: {0}", object);
        if (object != null) {
            LookupRPObjectManager.remove(object);
        }
        return true;
    }

    @Override
    public boolean onModifiedAdded(RPObject object, RPObject changes) {
        if (object != null) {
            if (itsMe(object)) {
                processEvents(changes);
            }
        }
        return true;
    }

    private boolean itsMe(RPObject object) {
        if (getPlayerRPC() != null && (object.get("id") == null
                ? getPlayerRPC().get("id") == null : object.get("id").equals(getPlayerRPC().get("id")))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onMyRPObject(RPObject added, RPObject deleted) {
        RPObject.ID id = null;
        if (added != null) {
            id = added.getID();
            //Process Events
            processEvents(added);
        }
        if (deleted != null) {
            id = deleted.getID();
        }
        if (id == null) {
            // Unchanged.
            logger.fine("Unchanged, returning");
            return true;
        }
        RPObject object = world.getWorldObjects().get(id);
        if (object != null) {
            if (getPlayerRPC() == null) {
                setPlayerRPC(object);
            }
        }
        return true;
    }

    /**
     * @return the chatNotifications
     */
    @Override
    public boolean isChatNotifications() {
        return chatNotifications;
    }

    /**
     * @param chatNotifications the chatNotifications to set
     */
    @Override
    public void setChatNotifications(boolean chatNotifications) {
        this.chatNotifications = chatNotifications;
    }

    @Override
    public RPObject getFromWorld(RPObject.ID id) {
        return world.getWorldObjects().get(id);
    }

    @Override
    public void addRPEventListener(Class<? extends RPEvent> event, RPEventListener l) {
        getUserContext().registerRPEventListener(event, l);
    }
}
