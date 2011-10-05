package simple.marauroa.event.manager.zone;

import java.util.logging.Logger;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;
import simple.client.entity.IUserContext;
import simple.marauroa.client.components.api.IZoneListManager;
import simple.server.extension.ZoneEvent;

public class Installer extends ModuleInstall {

    private static final Logger logger =
            Logger.getLogger(Installer.class.getCanonicalName());

    @Override
    public void restored() {
        logger.fine("Subscribing ZoneEventManager");
        Lookup.getDefault().lookup(IUserContext.class).registerRPEventListener(
                new ZoneEvent(), Lookup.getDefault().lookup(IZoneListManager.class));
    }
}
