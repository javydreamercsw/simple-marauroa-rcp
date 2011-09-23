package simple.marauroa.event.manager.zone;

import java.util.logging.Logger;
import org.openide.modules.ModuleInstall;
import simple.marauroa.application.core.EventBus;
import simple.server.core.event.api.IZoneEvent;

public class Installer extends ModuleInstall {

    private ZoneEventManager zem = new ZoneEventManager();
    private static final Logger logger =
            Logger.getLogger(Installer.class.getCanonicalName());

    @Override
    public void restored() {
        logger.fine("Subscribing ZoneEventManager");
        //Register
        EventBus.getDefault().subscribe(IZoneEvent.class, zem);
    }

    @Override
    public void close() {
        logger.fine("Unsubscribing ZoneEventManager");
        //Unregister
        EventBus.getDefault().unsubscribe(IZoneEvent.class, zem);
    }
}
