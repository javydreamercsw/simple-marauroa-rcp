package simple.marauroa.event.manager.zone;

import org.openide.modules.ModuleInstall;
import simple.marauroa.application.core.EventBus;
import simple.server.core.event.api.IZoneEvent;

public class Installer extends ModuleInstall {

    private ZoneEventManager zem = new ZoneEventManager();

    @Override
    public void restored() {
        EventBus.getDefault().subscribe(IZoneEvent.class, zem);
    }

    @Override
    public void close() {
        EventBus.getDefault().unsubscribe(IZoneEvent.class, zem);
    }
}
