package simple.marauroa.client.components.chat;

import org.openide.modules.ModuleInstall;
import simple.marauroa.application.core.EventBus;
import simple.marauroa.client.components.common.MCITool;
import simple.server.core.event.api.IChatEvent;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        //Register ChatManager to receive Chat related events
        EventBus.getDefault().subscribe(IChatEvent.class, MCITool.getChatManager());
    }

    @Override
    public void close() {
        //Unregister ChatManager
        EventBus.getDefault().unsubscribe(IChatEvent.class, MCITool.getChatManager());
    }
}
