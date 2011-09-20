package simple.marauroa.client.components.chat;

import java.util.logging.Level;
import java.util.logging.Logger;
import marauroa.common.game.RPEvent;
import org.openide.util.lookup.ServiceProvider;
import simple.client.EventLine;
import simple.common.NotificationType;
import simple.marauroa.application.core.EventBusListener;
import simple.marauroa.client.components.api.IPrivateChatComponent;
import simple.marauroa.client.components.common.MCITool;
import simple.server.core.event.PrivateTextEvent;
import simple.server.core.event.api.IPrivateChatEvent;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = IPrivateChatComponent.class)
public class PrivateChatManager implements IPrivateChatComponent, EventBusListener<IPrivateChatEvent> {

    private static final Logger logger =
            Logger.getLogger(PrivateChatManager.class.getSimpleName());

    @Override
    public void notify(IPrivateChatEvent event) {
        if (event != null) {
            logger.log(Level.FINE, "Got notified of event: {0}", event);
            PrivateTextEvent pTextEvent = new PrivateTextEvent();
            pTextEvent.fill((RPEvent) event);
            MCITool.getPublicChatManager().addLine((pTextEvent.get("from") == null ? "System" : pTextEvent.get("from")),
                    pTextEvent.get("text"), NotificationType.PRIVMSG);
        }
    }

    @Override
    public void addLine(String header, String line, NotificationType type) {
        throw new UnsupportedOperationException("Not supported yet. "
                + "Use MCITool.getPublicChatManager() methods instead.");
    }

    @Override
    public void addLine(EventLine line) {
        throw new UnsupportedOperationException("Not supported yet. "
                + "Use MCITool.getPublicChatManager() methods instead.");
    }

    @Override
    public void addLine(String header, String line) {
        throw new UnsupportedOperationException("Not supported yet. "
                + "Use MCITool.getPublicChatManager() methods instead.");
    }

    @Override
    public void addLine(String line, NotificationType type) {
        throw new UnsupportedOperationException("Not supported yet. "
                + "Use MCITool.getPublicChatManager() methods instead.");
    }

    @Override
    public String getNormalOutputName() {
        throw new UnsupportedOperationException("Not supported yet. "
                + "Use MCITool.getPublicChatManager() methods instead.");
    }

    @Override
    public String getPrivateOutputname() {
        throw new UnsupportedOperationException("Not supported yet. "
                + "Use MCITool.getPublicChatManager() methods instead.");
    }

    @Override
    public void open() {
        throw new UnsupportedOperationException("Not supported yet. "
                + "Use MCITool.getPublicChatManager() methods instead.");
    }

    @Override
    public boolean close() {
        throw new UnsupportedOperationException("Not supported yet. "
                + "Use MCITool.getPublicChatManager() methods instead.");
    }

    @Override
    public void read(String string) {
        throw new UnsupportedOperationException("Not supported yet. "
                + "Use MCITool.getPublicChatManager() methods instead.");
    }
}
