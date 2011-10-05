package simple.marauroa.application.monitor;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import marauroa.client.BannedAddressException;
import marauroa.client.LoginFailedException;
import marauroa.client.TimeoutException;
import marauroa.common.game.CharacterResult;
import marauroa.common.game.RPAction;
import marauroa.common.game.RPEvent;
import marauroa.common.game.RPObject;
import marauroa.common.net.InvalidVersionException;
import marauroa.common.net.message.MessageS2CPerception;
import marauroa.common.net.message.TransferContent;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import simple.client.*;
import simple.client.event.listener.RPEventListener;
import simple.client.event.listener.RPEventNotifier;
import simple.client.gui.IGameObjects;
import simple.marauroa.application.api.IApplicationMonitor;
import simple.marauroa.application.api.IMarauroaApplication;
import simple.marauroa.application.core.MonitorService;
import simple.server.extension.MonitorEvent;
import simple.server.extension.MonitorExtension;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public final class MonitorClient extends SimpleClient implements
        IApplicationMonitor, RPEventListener {

    private String host;
    private String password;
    private String character;
    private String port, clientGameName, version;
    private marauroa.client.ClientFramework clientManager;
    private IMarauroaApplication application;
    private boolean running = false;
    private String client_name;
    private ConnectionThread thread = null;

    public MonitorClient(IMarauroaApplication app) {
        super(LOG4J_PROPERTIES);
        setApplication(app);
        state = ClientState.CHAT;
        rpobjDispatcher = new RPObjectChangeDispatcher(
                Lookup.getDefault().lookup(IGameObjects.class), getUserContext());
        RPEventNotifier.get().notifyAtEvent(MonitorEvent.class, MonitorClient.this);
        PerceptionToObject pto = new PerceptionToObject();
        pto.setObjectFactory(new ObjectFactory());
        dispatch.register(pto);
    }

    private void requestToServer(int req) {
        RPAction action = new RPAction();
        action.put("type", MonitorExtension._MONITOR);
        action.put(MonitorEvent.ACTION, req);
        clientManager.send(action);
    }

    private void createClientManager(String name, String gversion) {
        clientGameName = name;
        version = gversion;
        clientManager = new marauroa.client.ClientFramework(
                "log4j.properties") {

            @Override
            protected String getGameName() {
                return clientGameName;
            }

            @Override
            protected String getVersionNumber() {
                return version;
            }

            @Override
            protected void onPerception(MessageS2CPerception message) {
                try {
                    Logger.getLogger(MonitorClient.class.getSimpleName()).log(
                            Level.INFO, "Received perception {0}",
                            message.getPerceptionTimestamp());

                    handler.apply(message, world.getWorldObjects());
                    if (message.getPerceptionTimestamp() == 0) {
                        Logger.getLogger(MonitorClient.class.getSimpleName()).info(
                                "Sending register request to server...");
                        requestToServer(MonitorEvent.REGISTER);
                    }
                    if (message.getPerceptionTimestamp() % 10 == 0) {
                        Logger.getLogger(MonitorClient.class.getSimpleName()).info(
                                "Sending get zones request to server...");
                        requestToServer(MonitorEvent.GET_ZONES);
                    }
                    processPerception(message);
                } catch (Exception e) {
                    Logger.getLogger(MonitorClient.class.getSimpleName()).log(Level.SEVERE, null, e);
                }
            }

            @Override
            protected List<TransferContent> onTransferREQ(
                    List<TransferContent> items) {
                for (TransferContent item : items) {
                    item.ack = true;
                }
                return items;
            }

            @Override
            protected void onTransfer(List<TransferContent> items) {
                Logger.getLogger(MonitorClient.class.getSimpleName()).info("Transfering ----");
                for (TransferContent item : items) {
                    Logger.getLogger(MonitorClient.class.getSimpleName()).info(item.toString());
                }
            }

            @Override
            protected void onAvailableCharacters(String[] characters) {
                //See onAvailableCharacterDetails
            }

            @Override
            protected void onAvailableCharacterDetails(Map<String, RPObject> characters) {

                // if there are no characters, create one with the specified name automatically
                if (characters.isEmpty()) {
                    Logger.getLogger(MonitorClient.class.getSimpleName()).log(
                            Level.INFO, "The requested character is not available, "
                            + "trying to create character {0}", character);
                    final RPObject template = new RPObject();
                    try {
                        final CharacterResult result = createCharacter(character, template);
                        if (result.getResult().failed()) {
                            Logger.getLogger(MonitorClient.class.getSimpleName()).info(result.getResult().getText());
                        }
                    } catch (final Exception e) {
                        Logger.getLogger(MonitorClient.class.getSimpleName()).log(Level.SEVERE, null, e);
                    }
                    return;
                }

                // autologin if a valid character was specified.
                if ((character != null) && (characters.keySet().contains(character))) {
                    try {
                        chooseCharacter(character);
                    } catch (final Exception e) {
                        Logger.getLogger(MonitorClient.class.getSimpleName()).log(
                                Level.INFO, "MonitorClient::onAvailableCharacters{0}", e);
                    }
                }
            }

            @Override
            protected void onServerInfo(String[] info) {
                Logger.getLogger(MonitorClient.class.getSimpleName()).info(
                        "Server info");
                for (String info_string : info) {
                    Logger.getLogger(MonitorClient.class.getSimpleName()).info(info_string);
                }
            }

            @Override
            protected void onPreviousLogins(List<String> previousLogins) {
                Logger.getLogger(MonitorClient.class.getSimpleName()).info("Previous logins");
                for (String info_string : previousLogins) {
                    Logger.getLogger(MonitorClient.class.getSimpleName()).info(info_string);
                }
            }
        };
    }

    @Override
    public boolean startMonitor() {
        if (getApplication() != null) {
            host = getApplication().getHost();
            password = getApplication().getPassword();
            setAccountUsername(MonitorExtension._MONITOR);
            port = String.valueOf(getApplication().getPort());
            character = MonitorExtension._MONITOR;
            client_name = getApplication().getName();
            version = getApplication().getVersion();
            world = new World();
            handler = new SimplePerceptionHandler(dispatch, rpobjDispatcher, this);
            createClientManager(client_name != null ? client_name : "Simple",
                    version != null ? version : "0.02.03");
            return true;
        }
        return false;
    }

    @Override
    public boolean shutdown() {
        running = false;
        try {
            clientManager.logout();
        } catch (InvalidVersionException ex) {
            //We don't care
        } catch (TimeoutException ex) {
            //We don't care
        } catch (BannedAddressException ex) {
            //We don't care
        }
        clientManager.close();
        return true;
    }

    /**
     * @return the application
     */
    protected IMarauroaApplication getApplication() {
        return application;
    }

    /**
     * @param application the application to set
     */
    protected void setApplication(IMarauroaApplication application) {
        this.application = application;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void onRPEventReceived(RPEvent rpe) {
        //TODO
    }

    @Override
    public void start() {
        if (thread == null) {
            thread = new ConnectionThread();
            thread.start();
        }
    }

    private class ConnectionThread extends Thread {

        @Override
        public void run() {
            try {
                clientManager.connect(host, Integer.parseInt(port));
                Logger.getLogger(MonitorClient.class.getSimpleName()).log(
                        Level.INFO, "Logging as: {0} with pass: {1}",
                        new Object[]{MonitorClient.this.getAccountUsername(), password});
                clientManager.login(MonitorClient.this.getAccountUsername(), password);
            } catch (Exception e) {
                MonitorService service = Lookup.getDefault().lookup(MonitorService.class);
                if (service != null && service.getMonitor(application.getName()) == null) {
                    service.removeMonitor(application.getName());
                }
                try {
                    Logger.getLogger(MonitorClient.class.getSimpleName()).info("Creating account and logging in to continue....");
                    clientManager.createAccount(MonitorClient.this.getAccountUsername(), password, host);
                    Logger.getLogger(MonitorClient.class.getSimpleName()).log(
                            Level.INFO, "Logging as: {0} with pass: {1}",
                            new Object[]{MonitorClient.this.getAccountUsername(), password});
                    clientManager.login(MonitorClient.this.getAccountUsername(), password);
                } catch (LoginFailedException ex) {
                    Logger.getLogger(MonitorClient.class.getSimpleName()).log(Level.SEVERE, null, ex);
                    DialogDisplayer.getDefault().notifyLater(
                            new NotifyDescriptor.Message(NbBundle.getMessage(
                            MonitorClient.class,
                            "client.connection.error") + ex.getReason(),
                            NotifyDescriptor.ERROR_MESSAGE));
                } catch (TimeoutException ex) {
                    Logger.getLogger(MonitorClient.class.getSimpleName()).log(Level.SEVERE, null, ex);
                    DialogDisplayer.getDefault().notifyLater(
                            new NotifyDescriptor.Message(NbBundle.getMessage(
                            MonitorClient.class,
                            "client.connection.error") + ex.getLocalizedMessage(),
                            NotifyDescriptor.ERROR_MESSAGE));
                } catch (InvalidVersionException ex) {
                    Logger.getLogger(MonitorClient.class.getSimpleName()).log(Level.SEVERE, null, ex);
                    DialogDisplayer.getDefault().notifyLater(
                            new NotifyDescriptor.Message(NbBundle.getMessage(
                            MonitorClient.class,
                            "client.connection.error") + "Version: "
                            + ex.getProtocolVersion() + " Version 2: "
                            + ex.getVersion() + "\n" + ex.getLocalizedMessage(),
                            NotifyDescriptor.ERROR_MESSAGE));
                } catch (BannedAddressException ex) {
                    Logger.getLogger(MonitorClient.class.getSimpleName()).log(Level.SEVERE, null, ex);
                    DialogDisplayer.getDefault().notifyLater(
                            new NotifyDescriptor.Message(NbBundle.getMessage(
                            MonitorClient.class,
                            "client.connection.error") + ex.getLocalizedMessage(),
                            NotifyDescriptor.ERROR_MESSAGE));
                }
            }

            boolean cond = true;

            while (cond) {
                clientManager.loop(0);
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    Logger.getLogger(MonitorClient.class.getSimpleName()).log(Level.SEVERE, null, e);
                }
            }
        }

        @Override
        public synchronized void start() {
            startMonitor();
            super.start();
            running = true;
        }
    }

    private void processPerception(MessageS2CPerception message) {
        if (!message.getAddedRPObjects().isEmpty()) {
            //Process added objects
            Logger.getLogger(MonitorClient.class.getSimpleName()).log(
                    Level.INFO, "Got added objects! {0}", message.getAddedRPObjects().size());
            for (RPObject object : message.getAddedRPObjects()) {
                application.addObject(object);
            }
        }
        if (!message.getDeletedRPObjects().isEmpty()) {
            //Process deleted objects
            Logger.getLogger(MonitorClient.class.getSimpleName()).log(
                    Level.INFO, "Got deleted objects! {0}", message.getDeletedRPObjects().size());
            for (RPObject object : message.getDeletedRPObjects()) {
                application.removeObject(object);
            }
        }
        if (!message.getModifiedAddedRPObjects().isEmpty()) {
            //Process modified/added objects
            Logger.getLogger(MonitorClient.class.getSimpleName()).log(
                    Level.INFO, "Got modified/added objects! {0}", message.getModifiedAddedRPObjects().size());
            for (RPObject object : message.getAddedRPObjects()) {
                application.addObject(object);
            }
        }
        if (!message.getModifiedDeletedRPObjects().isEmpty()) {
            //Process modified/deleted objects
            Logger.getLogger(MonitorClient.class.getSimpleName()).log(
                    Level.INFO, "Got modified/deleted objects! {0}", message.getModifiedDeletedRPObjects().size());
            for (RPObject object : message.getDeletedRPObjects()) {
                application.removeObject(object);
            }
        }
    }
}
