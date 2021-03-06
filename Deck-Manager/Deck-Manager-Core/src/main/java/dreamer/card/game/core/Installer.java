package dreamer.card.game.core;

import com.dreamer.outputhandler.OutputHandler;
import com.reflexit.magiccards.core.cache.AbstractCardCache;
import com.reflexit.magiccards.core.cache.ICardCache;
import com.reflexit.magiccards.core.model.ICardGame;
import com.reflexit.magiccards.core.model.storage.db.DataBaseStateListener;
import com.reflexit.magiccards.core.model.storage.db.IDataBaseCardStorage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.modules.ModuleInstall;
import org.openide.modules.Places;
import org.openide.util.Lookup;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.windows.WindowSystemEvent;
import org.openide.windows.WindowSystemListener;

@ServiceProvider(service = DataBaseStateListener.class)
public class Installer extends ModuleInstall implements ActionListener,
        DataBaseStateListener, WindowSystemListener {

    private static final Logger LOG = Logger.getLogger(Installer.class.getName());
    private static final long serialVersionUID = 1L;
    private final ArrayList<GameUpdateAction> updaters;
    private final ArrayList<Thread> runnables;
    private Timer timer;
    private final int period = 30000, pause = 10000;
    private final Map<String, String> dbProperties;
    private long start;
    private boolean waitDBInit = true;

    public Installer() {
        this.dbProperties = new HashMap<String, String>();
        this.updaters = new ArrayList<GameUpdateAction>();
        this.runnables = new ArrayList<Thread>();
    }

    @Override
    public void restored() {
        //Create game cache dir
        File cacheDir = Places.getCacheSubdirectory(".Deck Manager");
        dbProperties.put(PersistenceUnitProperties.JDBC_URL, "jdbc:h2:file:"
                + cacheDir.getAbsolutePath()
                + "/data/card_manager");
        dbProperties.put(PersistenceUnitProperties.TARGET_DATABASE,
                "org.eclipse.persistence.platform.database.H2Platform");
        dbProperties.put(PersistenceUnitProperties.JDBC_PASSWORD, "test");
        dbProperties.put(PersistenceUnitProperties.JDBC_DRIVER,
                "org.h2.Driver");
        dbProperties.put(PersistenceUnitProperties.JDBC_USER, "deck_manager");
        OutputHandler.select("Output");
        File cardCacheDir = new File(
                Places.getCacheSubdirectory(".Deck Manager").getAbsolutePath()
                + System.getProperty("file.separator") + "cache");
        //Create game cache dir
        if (!cardCacheDir.exists()) {
            cardCacheDir.mkdirs();
        }
        AbstractCardCache.setCachingEnabled(true);
        AbstractCardCache.setLoadingEnabled(true);
        AbstractCardCache.setCacheDir(cardCacheDir);
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            @SuppressWarnings("unchecked")
            public void run() {
                try {
                    //Timer for inactivity background work
                    timer = new Timer(period, Installer.this);
                    timer.setInitialDelay(pause);
                    timer.start();
                    OutputHandler.output("Output", "Initializing database...");
                    IDataBaseCardStorage db =
                            Lookup.getDefault().lookup(
                            IDataBaseCardStorage.class);
                    if (db != null) {
                        db.setDataBaseProperties(dbProperties);
                        //Start the database activities
                        LOG.log(Level.FINE, "Initializing database...");
                        try {
                            //Make sure to load the driver
                            start = System.currentTimeMillis();
                            Lookup.getDefault().lookup(ClassLoader.class)
                                    .loadClass(dbProperties.get(
                                    PersistenceUnitProperties.JDBC_DRIVER));
                            LOG.log(Level.FINE,
                                    "Succesfully loaded driver: {0}",
                                    dbProperties.get(
                                    PersistenceUnitProperties.JDBC_DRIVER));
                            db.initialize();
                            while (waitDBInit) {
                                Thread.currentThread().sleep(100);
                            }
                        } catch (Exception ex) {
                            LOG.log(Level.SEVERE, null, ex);
                            DialogDisplayer.getDefault().notify(
                                    new NotifyDescriptor.Message(
                                    "Unable to connect to database. "
                                    + "Please restart application",
                                    NotifyDescriptor.ERROR_MESSAGE));
                        }
                    }
                } catch (IllegalArgumentException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
                OutputHandler.output("Output", "Core loaded!");
            }
        });
    }

    @Override
    public boolean closing() {
        super.closing();
        try {
            OutputHandler.output("Output", "Shutting background tasks...");
            for (Iterator<GameUpdateAction> it =
                    updaters.iterator(); it.hasNext();) {
                GameUpdateAction updater = it.next();
                updater.shutdown();
            }

            for (Iterator<Thread> it = runnables.iterator(); it.hasNext();) {
                Thread runnable = it.next();
                runnable.interrupt();
            }
            OutputHandler.output("Output", "Done!");
            IDataBaseCardStorage db =
                    Lookup.getDefault().lookup(IDataBaseCardStorage.class);
            if (db != null) {
                db.close();
            }
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error shuting down!", e);
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (afterUpdates()) {
            //We are done
            timer.stop();
        } else {
            //Make sure to retry in case we got an update.
            timer.restart();
        }
    }

    private boolean afterUpdates() {
        boolean ready = true;
        for (Iterator<GameUpdateAction> it =
                updaters.iterator(); it.hasNext();) {
            GameUpdateAction gua = it.next();
            if (!gua.finished) {
                ready = false;
                break;
            }
        }
        if (ready) {
            for (Iterator<Thread> it = runnables.iterator(); it.hasNext();) {
                Thread t = it.next();
                if (!t.isAlive()) {
                    ready = false;
                    break;
                }
            }
            OutputHandler.output("Output", "Executing after update code...");
            //TODO: Code to be executed after all updates are complete
            OutputHandler.output("Output", "Done!");
        }
        return ready;
    }

    @Override
    public void initialized() {
        LOG.fine("DB ready!");
        waitDBInit = false;
        //Code to be done after the db is ready
        LOG.log(Level.FINE, "Database initialized");
        OutputHandler.output("Output", "Database initialized");
        LOG.log(Level.FINE, "Initializing database took: {0}",
                Tool.elapsedTime(start));
        LOG.log(Level.FINE, "Initializing games...");
        Runnable task;
        OutputHandler.output("Output", "Starting game updaters...");
        for (Iterator<? extends ICardGame> it =
                Lookup.getDefault().lookupAll(ICardGame.class).iterator();
                it.hasNext();) {
            ICardGame game = it.next();
            new GameInitializationAction(game).actionPerformed(null);
            task = game.getUpdateRunnable();
            if (task != null) {
                if (task instanceof IProgressAction) {
                    //Properly created to display progress in the IDE
                    updaters.add(new GameUpdateAction((IProgressAction) task));
                } else {
                    //No progress information available
                    runnables.add(new Thread(task, game.getName()
                            + " game updater"));
                }
            }
        }
        OutputHandler.output("Output", "Done!");
        OutputHandler.output("Output", "Starting cache updaters...");
        for (Iterator<? extends ICardCache> it =
                Lookup.getDefault().lookupAll(ICardCache.class).iterator();
                it.hasNext();) {
            ICardCache cache = it.next();
            task = cache.getCacheTask();
            if (task != null) {
                if (task instanceof IProgressAction) {
                    //Properly created to display progress in the IDE
                    updaters.add(new CacheUpdateAction((IProgressAction) task));
                } else {
                    //No progress information available
                    runnables.add(new Thread(task, cache.getGameName()
                            + " cache updater"));
                }
            }
        }
        OutputHandler.output("Output", "Done!");
        for (Iterator<GameUpdateAction> it = updaters.iterator();
                it.hasNext();) {
            GameUpdateAction updater = it.next();
            updater.actionPerformed(null);
        }
        for (Iterator<Thread> it = runnables.iterator(); it.hasNext();) {
            Thread runnable = it.next();
            runnable.start();
        }
    }

    @Override
    public void beforeLoad(WindowSystemEvent event) {
        String role = NbPreferences.forModule(TopComponent.class)
                .get("currentScreen", "game_view");
        WindowManager.getDefault().setRole(role);
        WindowManager.getDefault().removeWindowSystemListener(this);
    }

    @Override
    public void afterLoad(WindowSystemEvent wse) {
        //Do nothing
    }

    @Override
    public void beforeSave(WindowSystemEvent wse) {
        //Do nothing
    }

    @Override
    public void afterSave(WindowSystemEvent wse) {
        //Do nothing
    }
}
