package simple.marauroa.application.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 * This class just keeps EventBus synched with the changes on the global lookup
 * so selections in the global lookup are passed to the EventBus
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public class LookupSync implements LookupListener {

    private Lookup.Result result = null;
    private static final Logger logger =
            Logger.getLogger(LookupSync.class.getSimpleName());
    private Class topic;

    public LookupSync(Class template) {
        result = Utilities.actionsGlobalContext().lookupResult(template);
        result.addLookupListener(LookupSync.this);
        topic = template;
    }

    @Override
    public void resultChanged(LookupEvent le) {
        Lookup.Result r = (Lookup.Result) le.getSource();
        Collection c = r.allInstances();
        //Copy
        if (!c.isEmpty()) {
            Iterator iterator = c.iterator();
            logger.log(Level.FINE, "Synchronizong topic: {0}", topic.getSimpleName());
            while (iterator.hasNext()) {
                Object next = iterator.next();
                if (next != null) {
                    logger.log(Level.FINE, "Adding: {0}", next);
                    EventBus.getDefault().add(next);
                }
            }
        }
    }

    public void close() {
        result.removeLookupListener(this);
        result = null;
    }
}
