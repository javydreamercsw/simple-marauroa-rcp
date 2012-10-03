package simple.marauroa.event.manager.zone;

import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;
import simple.client.entity.IUserContext;
import simple.marauroa.client.components.api.IZoneListManager;
import simple.server.extension.ZoneEvent;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        Lookup.getDefault().lookup(IUserContext.class).registerRPEventListener(
                new ZoneEvent(), Lookup.getDefault().lookup(IZoneListManager.class));
    }
}
