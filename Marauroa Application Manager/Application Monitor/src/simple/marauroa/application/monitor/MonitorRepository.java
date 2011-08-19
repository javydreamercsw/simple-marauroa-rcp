package simple.marauroa.application.monitor;

import java.util.Collection;
import org.openide.util.LookupEvent;
import simple.marauroa.application.core.MonitorService;
import java.util.HashMap;
import org.openide.util.Lookup;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.ServiceProvider;
import simple.marauroa.application.api.IApplicationMonitor;
import simple.marauroa.application.api.IMarauroaApplication;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = MonitorService.class)
public class MonitorRepository implements MonitorService ,LookupListener{

    private HashMap<String, IApplicationMonitor> monitors =
            new HashMap<String, IApplicationMonitor>();
    private Lookup.Result result = null;
    private IMarauroaApplication currentApplication;

    public MonitorRepository() {
        result = Utilities.actionsGlobalContext().lookupResult(IMarauroaApplication.class);
        result.addLookupListener(this);
    }

    @Override
    public IApplicationMonitor getMonitor(String name) {
        return monitors.get(name);
    }

    @Override
    public void addMonitor(String name) throws Exception {
        MonitorClient client= new MonitorClient(getCurrentApplication());
        if (monitors.containsKey(name)) {
            throw new Exception("There's a monitor defined already for: " + name);
        } else {
            if (!client.isRunning()) {
                client.start();
            }
            monitors.put(name, client);
        }
    }

    @Override
    public void removeMonitor(String name) {
        if (monitors.containsKey(name)) {
            monitors.get(name).shutdown();
            monitors.remove(name);
        }
    }

    @Override
    public void resultChanged(LookupEvent le) {
        Lookup.Result r = (Lookup.Result) le.getSource();
        Collection c = r.allInstances();
        //Update the selected application
        if (!c.isEmpty()) {
            setApplication((IMarauroaApplication) c.iterator().next());
        }
    }

    private void setApplication(IMarauroaApplication app) {
        currentApplication = app;
    }

    /**
     * @return the currentApplication
     */
    public IMarauroaApplication getCurrentApplication() {
        return currentApplication;
    }

    @Override
    public void shutdown() {
        result.removeLookupListener(this);
        result = null;
    }
}
