package simple.marauroa.application.core;

/**
 * This code is adapted from OpenBlueSky project. For some reason I'm unable to
 * retrieve the original files so I ended up copying code from:
 * http://netbeans.dzone.com/news/publish-subscribe-netbeans-pla?page=0,1
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

public class ListenerAdapter<T> implements LookupListener {

    private final EventBusListener eventBusListener;
    private int size;
    private static final Logger logger =
            Logger.getLogger(ListenerAdapter.class.getSimpleName());

    public ListenerAdapter(final EventBusListener eventBusListener) {
        this.eventBusListener = eventBusListener;
    }

    @Override
    public void resultChanged(final LookupEvent event) {
        final Lookup.Result result = (Lookup.Result) event.getSource();
        int newSize = result.allInstances().size();
        logger.log(Level.INFO, "{0} = {1}?",
                    new Object[]{size, newSize});
        if (!result.allInstances().isEmpty() && newSize > size) {
            //There's something to add
            Iterator iterator = result.allInstances().iterator();
            logger.log(Level.INFO, "Notifying {0} listeners!",
                    result.allInstances().size());
            while (iterator.hasNext()) {
                Object listener = iterator.next();
                logger.log(Level.FINE, listener.getClass().getCanonicalName());
                eventBusListener.notify((T) listener);
            }
        } else {
            //If new size is less, someone got removed.
            logger.info("Notifying something got removed (null)");
            eventBusListener.notify(null);
        }
        size = newSize;
    }
}
