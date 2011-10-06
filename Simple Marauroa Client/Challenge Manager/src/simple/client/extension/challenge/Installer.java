package simple.client.extension.challenge;

import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;
import simple.client.entity.IUserContext;
import simple.marauroa.client.components.api.IChallengeManager;
import simple.server.extension.ChallengeEvent;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
         Lookup.getDefault().lookup(IUserContext.class).registerRPEventListener(
                new ChallengeEvent(), Lookup.getDefault().lookup(IChallengeManager.class));
    }
}
