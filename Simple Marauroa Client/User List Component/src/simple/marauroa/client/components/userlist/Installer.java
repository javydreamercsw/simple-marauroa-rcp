package simple.marauroa.client.components.userlist;

import java.util.logging.Logger;
import marauroa.common.game.RPObject;
import org.openide.modules.ModuleInstall;
import simple.marauroa.application.core.EventBus;
import simple.marauroa.client.components.common.MCITool;

public class Installer extends ModuleInstall {
private static final Logger logger =
            Logger.getLogger(Installer.class.getCanonicalName());
    @Override
    public void restored() {
        logger.fine("Subscribing ZoneEventManager");
        //Register
        EventBus.getDefault().subscribe(RPObject.class, MCITool.getUserListManager());
    }

    @Override
    public void close() {
        logger.fine("Subscribing ZoneEventManager");
        //Register
        EventBus.getDefault().subscribe(RPObject.class, MCITool.getUserListManager());
    }
}
