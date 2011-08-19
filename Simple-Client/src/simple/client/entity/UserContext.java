/*
 * $Rev: 348 $
 * $LastChangedDate: 2011-04-17 09:52:49 -0500 (Sun, 17 Apr 2011) $
 * $LastChangedBy: javydreamercsw $
 */
package simple.client.entity;

import simple.client.SimpleClient;
import simple.client.event.listener.RPEventListener;
import simple.client.event.listener.RPEventNotifier;
import java.net.URL;
import java.util.HashMap;

import java.util.Map.Entry;
import javax.swing.ImageIcon;
import marauroa.common.Log4J;
import marauroa.common.Logger;
import marauroa.common.game.RPEvent;
import marauroa.common.game.RPObject;
import simple.client.RPObjectChangeListener;
import simple.client.gui.GameObjects;

/**
 * The player user context. This class holds/manages the data for the user of
 * this client. This is independent of any on-screen representation ClientEntity that,
 * while related, serves an entirely different purpose.
 * 
 * Currently this is just a helper class for jWrestlingClient. Maybe it will be
 * directly used by other code later.
 */
public class UserContext implements RPObjectChangeListener {

    /**
     * The logger.
     */
    private static final Logger logger = Log4J.getLogger(UserContext.class);
    private final String smileyPath = "/games/jwrestling/resources/smileys/";
    private static HashMap<String, String> smileys = new HashMap<String, String>();
    /**
     * The currently known buddies.
     */
    protected HashMap<String, Boolean> buddies;
    /**
     * The currently enabled features.
     */
    protected HashMap<String, String> features;
    /**
     * The RPEvent listeners.
     */
    protected RPEventNotifier eventNotifier;
    /**
     * The admin level.
     */
    protected int adminlevel;
    /**
     * The game objects.
     */
    protected final GameObjects gameObjects;
    /**
     * The player character's name.
     */
    protected String name;
    /**
     * The player client.
     */
    protected SimpleClient client;
    private boolean inited = false;

    /**
     * Constructor.
     * @param client CLient using this usercontext
     */
    public UserContext(SimpleClient client) {
        this.client = client;
        adminlevel = 0;
        eventNotifier = RPEventNotifier.get();
        gameObjects = GameObjects.getInstance();
        name = null;
        buddies = new HashMap<String, Boolean>();
        features = new HashMap<String, String>();
        init();
    }

    /**
     *
     * @param event
     * @param listener
     */
    public void registerRPEventListener(RPEvent event, RPEventListener listener) {
        logger.debug("Adding event: " + event.getName() + " to the listener list");
        eventNotifier.notifyAtEvent(event, listener);
    }

    /**
     * Fire admin level change event to all registered listeners.
     *
     * @param adminLevel
     *            The new admin level.
     */
    protected void fireAdminLevelChanged(int adminLevel) {
        // TODO: Impl
    }

    /**
     * Fire name change event to all registered listeners.
     *
     * @param newName
     *            The new player name.
     */
    protected void fireNameChanged(String newName) {
        // TODO: Impl
    }

    /**
     * Get the admin level.
     *
     * @return The admin level.
     */
    public int getAdminLevel() {
        return adminlevel;
    }

    /**
     * Get the player character name.
     *
     * @return The player character name.
     */
    public String getName() {
        return name;
    }

    /**
     * Determine if the user is an admin.
     *
     * @return <code>true</code> is the user is an admin.
     */
    public boolean isAdmin() {
        return (getAdminLevel() != 0);
    }

    //
    // RPObjectChangeListener
    //
    /**
     * An object was added.
     *
     * @param object
     *            The object.
     */
    @Override
    public void onAdded(final RPObject object) {
        if (object.has("adminlevel")) {
            adminlevel = object.getInt("adminlevel");
            fireAdminLevelChanged(adminlevel);
        }
    }

    /**
     * The object added/changed attribute(s).
     *
     * @param object
     *            The base object.
     * @param changes
     *            The changes.
     */
    @Override
    public void onChangedAdded(final RPObject object, final RPObject changes) {
        if (changes.has("adminlevel")) {
            adminlevel = changes.getInt("adminlevel");
            fireAdminLevelChanged(adminlevel);
        }

        if (changes.has("name")) {
            name = changes.get("name");
            fireNameChanged(name);
        }
    }

    /**
     * An object removed attribute(s).
     *
     * @param object
     *            The base object.
     * @param changes
     *            The changes.
     */
    @Override
    public void onChangedRemoved(final RPObject object, final RPObject changes) {
        if (changes.has("adminlevel")) {
            adminlevel = 0;
            fireAdminLevelChanged(adminlevel);
        }

        if (changes.has("name")) {
            name = null;
            fireNameChanged(name);
        }
    }

    /**
     * An object was removed.
     *
     * @param object
     *            The object.
     */
    @Override
    public void onRemoved(final RPObject object) {
        adminlevel = 0;
        fireAdminLevelChanged(adminlevel);

        name = null;
        fireNameChanged(null);
    }

    /**
     * A slot object was added.
     *
     * @param object
     *            The container object.
     * @param slotName
     *            The slot name.
     * @param sobject
     *            The slot object.
     */
    @Override
    public void onSlotAdded(final RPObject object, final String slotName,
            final RPObject sobject) {
        if (sobject.getRPClass().subclassOf("entity")) {
            synchronized (gameObjects) {
                ClientEntity entity = gameObjects.get(sobject);

                if (entity != null) {
                    ClientEntity parent = gameObjects.get(object);

                    if (logger.isDebugEnabled()) {
                        logger.debug("Added: " + entity);
                        logger.debug("   To: " + parent + "  [" + slotName + "]");
                    }
                }
            }
        }
    }

    /**
     * A slot object added/changed attribute(s).
     *
     * @param object
     *            The base container object.
     * @param slotName
     *            The container's slot name.
     * @param sobject
     *            The slot object.
     * @param schanges
     *            The slot object changes.
     */
    @Override
    public void onSlotChangedAdded(final RPObject object,
            final String slotName, final RPObject sobject,
            final RPObject schanges) {
    }

    /**
     * A slot object removed attribute(s).
     *
     * @param object
     *            The base container object.
     * @param slotName
     *            The container's slot name.
     * @param sobject
     *            The slot object.
     * @param schanges
     *            The slot object changes.
     */
    @Override
    public void onSlotChangedRemoved(final RPObject object,
            final String slotName, final RPObject sobject,
            final RPObject schanges) {
    }

    /**
     * A slot object was removed.
     *
     * @param object
     *            The container object.
     * @param slotName
     *            The slot name.
     * @param sobject
     *            The slot object.
     */
    @Override
    public void onSlotRemoved(final RPObject object, final String slotName,
            final RPObject sobject) {
        if (sobject.getRPClass().subclassOf("entity")) {
            synchronized (gameObjects) {
                ClientEntity entity = gameObjects.get(sobject);

                if (entity != null) {
                    ClientEntity parent = gameObjects.get(object);

                    if (logger.isDebugEnabled()) {
                        logger.debug("Removed: " + entity);
                        logger.debug("   From: " + parent + "  [" + slotName + "]");
                    }
                }
            }
        }
    }

    @Override
    public RPObject onRPEvent(RPObject object) {
        HashMap<RPEvent, Boolean> result = eventNotifier.logic(object.events());
        if (!result.entrySet().isEmpty()) {
            logger.info("Here are the processed events. A false means that probably RPEventListeners not registered.\n");
            for (Entry e : result.entrySet()) {
                logger.debug(e.getKey() + " Processed? " + e.getValue());
            }
        } else {
            if (!object.events().isEmpty()) {
                logger.info("Unable to process events:");
                for (RPEvent e : object.events()) {
                    logger.info(e);
                }
            }
        }
        object.clearEvents();
        return object;
    }

    private void init() {
        if (!inited) {
            logger.debug("Init UserContext.");
            addSmiley(":-)", "happy.gif");
            addSmiley(":-(", "sad.gif");
            addSmiley(":-o", "surprise.gif");
            addSmiley(":-d", "smile.gif");
            addSmiley(":-p", "tongue.gif");
            addSmiley(":-i", "no-expression.gif");
            addSmiley(";-)", "wink.gif");
            addSmiley(">-@", "mad.gif");
            addSmiley("8-)", "cool.gif");
            addSmiley(":'-(", "tear.gif");
            addSmiley("(y)", "yes.gif");
            addSmiley("(n)", "no.gif");
            addSmiley("lol", "LOL.gif");
            addSmiley(":-$", "shhhh.gif");
            addSmiley("brb", "BRB.gif");
            inited = true;
        }
    }

    private void addSmiley(String s, String imagePath) {
        if (s.contains("-")) {
            logger.debug("Adding: " + s.toLowerCase().replaceAll("-", "") + ": " + imagePath);
            smileys.put(s.toLowerCase().replaceAll("-", ""), smileyPath + imagePath);
        }
        logger.debug("Adding: " + s + ": " + imagePath);
        smileys.put(s.toLowerCase(), smileyPath + imagePath);
    }

    /**
     *
     * @param txt
     * @return
     */
    public boolean isSmiley(String txt) {
        boolean found = smileys.containsKey(txt.toLowerCase());
        //TODO: Reevaluate
        return found;
    }

    /**
     *
     * @param txt
     * @return
     */
    public ImageIcon getSmiley(String txt) {
        init();
        if (isSmiley(txt)) {
            return loadImage(smileys.get(txt.toLowerCase()));
        }
        return null;
    }

    private ImageIcon loadImage(String image_name) {
        URL image_url = null;
        try {
            image_url = getClass().getResource(image_name);
            if (image_url != null) {
                return new ImageIcon(image_url);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        } finally {
            image_url = null;
        }
    }
    
    
}
