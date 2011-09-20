package simple.marauroa.client.components.chat;

import org.openide.modules.ModuleInstall;
import simple.marauroa.application.core.EventBus;
import simple.marauroa.client.components.common.MCITool;
import simple.server.core.event.api.IPrivateChatEvent;
import simple.server.core.event.api.IPublicChatEvent;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        //Register PublicChatManager to receive Public Chat related events
        EventBus.getDefault().subscribe(IPublicChatEvent.class, MCITool.getPublicChatManager());
        //Register PrivateChatManager to receive Private Chat related events
        EventBus.getDefault().subscribe(IPrivateChatEvent.class, MCITool.getPrivateChatManager());
    }

    @Override
    public void close() {
        //Unregister PublicChatManager
        EventBus.getDefault().unsubscribe(IPublicChatEvent.class, MCITool.getPublicChatManager());
        //Unregister PrivateChatManager
        EventBus.getDefault().unsubscribe(IPrivateChatEvent.class, MCITool.getPrivateChatManager());
    }
}
