package simple.marauroa.client.components.chat;

import java.util.logging.Logger;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;
import simple.client.entity.IUserContext;
import simple.marauroa.client.components.common.MCITool;
import simple.server.core.event.PrivateTextEvent;
import simple.server.core.event.TextEvent;

public class Installer extends ModuleInstall {

    private static final Logger logger =
            Logger.getLogger(Installer.class.getCanonicalName());

    @Override
    public void restored() {
        logger.info("Listening for chat");
        Lookup.getDefault().lookup(IUserContext.class).registerRPEventListener(new TextEvent(), MCITool.getPublicChatManager());

        Lookup.getDefault().lookup(IUserContext.class).registerRPEventListener(new PrivateTextEvent(), MCITool.getPrivateChatManager());
    }
}
