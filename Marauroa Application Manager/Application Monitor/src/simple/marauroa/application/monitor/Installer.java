package simple.marauroa.application.monitor;

import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;
import simple.marauroa.application.core.MonitorService;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        //Call the service so it registers as a listener
        Lookup.getDefault().lookup(MonitorService.class);
    }
}
