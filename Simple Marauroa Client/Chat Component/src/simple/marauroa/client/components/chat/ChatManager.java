package simple.marauroa.client.components.chat;

import com.dreamer.outputhandler.InputMonitor;
import com.dreamer.outputhandler.OutputHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import marauroa.common.game.RPEvent;
import org.jivesoftware.smack.util.ReaderListener;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;
import simple.client.EventLine;
import simple.common.NotificationType;
import simple.marauroa.client.components.api.IChatComponent;
import simple.marauroa.client.components.common.MCITool;
import simple.server.core.event.PrivateTextEvent;
import simple.server.core.event.TextEvent;

/**
 * Manages the chat aspect of an application.
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = IChatComponent.class)
public class ChatManager implements IChatComponent, ReaderListener {

    private final String chat = "Chat";
    private final String priv = "Private-";
    private String sender = "";
    private static final Logger logger = Logger.getLogger(ChatManager.class.getName());

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
            }
        }
        if (!tabName.isEmpty()) {
            OutputHandler.output(tabName, "");
            OutputHandler.setInputEnabled(tabName, true);
            //Create a monitor for the tab. This enables input in the tab as well.
            InputMonitor monitor = OutputHandler.createMonitor(tabName, 1000);
            //Add a listener to be notified.
            monitor.addListener(tabName.equals(getPrivateOutputname()) ? 
                    new PrivateChatMonitor(tabName) : this);
        }
        insertText((header == null ? "" : "<" + header + "> ") + line, type);
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
    public void onRPEventReceived(RPEvent event) throws Exception {
        try {
            if (event != null) {
                logger.log(Level.INFO, "Got notified of event: {0}", event);
                if (event.getName().equals(TextEvent.getRPClassName())) {
                    TextEvent textEvent = new TextEvent();
                    textEvent.fill(event);
                    addLine((textEvent.get("from") == null ? "System" : textEvent.get("from")),
                            textEvent.get("text"), NotificationType.NORMAL);
                } else if (event.getName().equals(PrivateTextEvent.getRPClassName())) {
                    PrivateTextEvent pTextEvent = new PrivateTextEvent();
                    pTextEvent.fill(event);
                    addLine((pTextEvent.get("from") == null ? "System" : pTextEvent.get("from")),
                            pTextEvent.get("text"), NotificationType.PRIVMSG);
                }
            }
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
            logger.log(Level.SEVERE, null, e);
        }
    }
}
