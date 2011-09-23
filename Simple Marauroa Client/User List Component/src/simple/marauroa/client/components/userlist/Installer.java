package simple.marauroa.client.components.userlist;

import marauroa.common.game.RPObject;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;
import simple.marauroa.application.core.EventBus;
import simple.marauroa.client.components.common.MCITool;

public class Installer extends ModuleInstall {

    private Lookup.Result result = null;

    @Override
    public void restored() {
        result = EventBus.getDefault().getCentralLookup().lookupResult(RPObject.class);
        result.addLookupListener(MCITool.getUserListManager());
    }

    @Override
    public void close() {
        result.removeLookupListener(MCITool.getUserListManager());
        result = null;
        MCITool.getUserListManager().close();
    }
}
