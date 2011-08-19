package simple.marauroa.application.core;

/**
 * This code is adapted from OpenBlueSky project. For some reason I'm unable to 
 * retrieve the original files so I ended up copying code from:
 * http://netbeans.dzone.com/news/publish-subscribe-netbeans-pla?page=0,1
 * 
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

public class ListenerAdapter<T> implements LookupListener {

    private final EventBusListener eventBusListener;

    public ListenerAdapter(final EventBusListener eventBusListener) {
        this.eventBusListener = eventBusListener;
    }

    @Override
    public void resultChanged(final LookupEvent event) {
        final Lookup.Result result = (Lookup.Result) event.getSource();

        if (!result.allInstances().isEmpty()) {
            eventBusListener.notify((T) result.allInstances().iterator().next());
        } else {
            eventBusListener.notify(null);
        }
    }
}
