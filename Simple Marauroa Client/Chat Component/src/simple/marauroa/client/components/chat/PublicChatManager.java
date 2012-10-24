package simple.marauroa.client.components.chat;

import com.dreamer.outputhandler.InputMonitor;
import com.dreamer.outputhandler.OutputHandler;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import marauroa.common.game.RPEvent;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;
import simple.client.EventLine;
import simple.common.NotificationType;
import simple.marauroa.client.components.api.IPublicChatComponent;
import simple.marauroa.client.components.common.MCITool;
import simple.server.core.event.TextEvent;

/**
 * Manages the chat aspect of an application.
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = IPublicChatComponent.class)
public class PublicChatManager implements IPublicChatComponent {

    private final String chat = "Chat";
    private final String priv = "Private-";
    private ArrayList<String> privateTabs = new ArrayList<String>();
    private String sender = "";
    private static final Logger logger =
            Logger.getLogger(PublicChatManager.class.getSimpleName());

    @Override
    public void addLine(String header, String line, NotificationType type) {
        String tabName = "";
        if (isPrivate(type) && !"System".equals(header)) {
            sender = header;
            if (!OutputHandler.has(getPrivateOutputname())) {
                tabName = getPrivateOutputname();
            }
        } else {
            sender = "";
            if (!OutputHandler.has(getNormalOutputName())) {
                tabName = getNormalOutputName();
                privateTabs.add(tabName);
            }
        }
        if (!tabName.isEmpty() && !OutputHandler.has(tabName)) {
            OutputHandler.output(tabName, "");
            OutputHandler.setInputEnabled(tabName, true);
            //Create a monitor for the tab. This enables input in the tab as well.
            InputMonitor monitor = OutputHandler.createMonitor(tabName, 1000);
            //Add a listener to be notified.
            monitor.addListener(tabName.equals(getPrivateOutputname())
                    ? new PrivateChatMonitor() : this);
        }
        insertText((header == null || header.isEmpty() ? "" : "<" + header + "> ") + line, type);
    }

    @Override
    public void addLine(EventLine line) {
        addLine(line.getHeader(), line.getText(), line.getType());
    }

    @Override
    public void addLine(String header, String line) {
        addLine(header, line, NotificationType.NORMAL);
    }

    @Override
    public void addLine(String line, NotificationType type) {
        addLine("", line, type);
    }

    private boolean isPrivate(NotificationType type) {
        return type == NotificationType.PRIVMSG;
    }

    /**
     * Insert text
     *
     * @param text
     * @param type
     */
    protected void insertText(String text, NotificationType type) {
        OutputHandler.output(isPrivate(type)
                && !text.startsWith("<System>") ? getPrivateOutputname()
                : getNormalOutputName(), text, type.getColor());
    }

    @Override
    public String getNormalOutputName() {
        return chat;
    }

    @Override
    public String getPrivateOutputname() {
        return priv + sender;
    }

    @Override
    public void read(String read) {
        //It will be typed here when it gets to the client on the next perception.
        MCITool.getClient().sendMessage(read.replaceAll("\n", ""));
    }

    @Override
    public void open() {
        if (!OutputHandler.has(getNormalOutputName())) {
            OutputHandler.output(getNormalOutputName(), "");
        }
    }

    @Override
    public boolean close() {
        OutputHandler.closeOutput(chat);
        //Remove private tabs
        for (String tab : privateTabs) {
            if (OutputHandler.has(tab)) {
                OutputHandler.closeOutput(tab);
            }
        }
        return true;
    }

    @Override
    public void onRPEventReceived(RPEvent event) throws Exception {
        if (event != null) {
            logger.log(Level.FINE, "Got notified of event: {0}", event);
            TextEvent textEvent = new TextEvent();
            textEvent.fill((RPEvent) event);
            addLine((textEvent.get("from") == null ? "System" : textEvent.get("from")),
                    textEvent.get("text"), NotificationType.NORMAL);
        }
    }

    @Override
    public TopComponent getComponent() {
        return null;
    }
}
