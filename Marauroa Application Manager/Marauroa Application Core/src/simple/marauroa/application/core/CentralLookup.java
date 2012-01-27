package simple.marauroa.application.core;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Class used to house anything one might want to store
 * in a central lookup which can affect anything within
 * the application. It can be thought of as a central context
 * where any application data may be stored and watched.
 * 
 * A singleton instance is created using @see getDefault().
 * This class is as thread safe as Lookup. Lookup appears to be safe.
 * 
 * @author Wade Chandler
 * @version 1.0
 */
public class CentralLookup extends AbstractLookup {

    private InstanceContent content = null;
    private static CentralLookup def = new CentralLookup();
    private static final Logger logger =
            Logger.getLogger(CentralLookup.class.getSimpleName());
    private static boolean showContents = false;

    /**
     * Creates a CentralLookup instances with a specific content set.
     * @param content the InstanceContent to use
     */
    public CentralLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    /**
     * Creates a new CentralLookup
     */
    public CentralLookup() {
        this(new InstanceContent());
    }

    /**
     * Adds an instance to the Lookup. The instance will be added with the classes
     * in its hierarchy as keys which may be used to lookup the instance(s).
     * @param instance The instance to add
     */
    public void add(Object instance) {
        content.add(instance);
        if(showContents){
            displayLookupContents(instance.getClass());
        }
    }

    /**
     * Removes the specific instance from the Lookup content.
     * @param instance The specific instance to remove.
     */
    public void remove(Object instance) {
        content.remove(instance);
        if(showContents){
            displayLookupContents(instance.getClass());
        }
    }

    /**
     * Returns the default CentralLookup. This can be used as an application context for
     * the entire application. If needed CentralLookup may be used directly through the
     * constructors to allow for more than one if needed. CentralLookup is nothing more
     * than an InstanceContent instance wrapped in a Lookup with the add and remove methods
     * added to make updating the data easier.
     * @return The default CentralLookup which is global in nature.
     */
    public static CentralLookup getDefault() {
        return def;
    }
    
    public void setShowContents(boolean show){
        showContents = show;
    }
    
    private void displayLookupContents(Class template) {
        Collection result = EventBus.getDefault().getCentralLookup().lookupAll(template);
        logger.log(Level.INFO, "{0} {1} in lookup.",
                new Object[]{result.size(), template.getSimpleName()});
        for (Object o : result) {
            logger.log(Level.INFO, o.toString());
        }
    }
}
