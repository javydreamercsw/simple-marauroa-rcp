package simple.marauroa.client.components.chat;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import marauroa.common.game.RPEvent;
import marauroa.common.game.RPObject;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import simple.client.EventLine;
import simple.common.NotificationType;
import simple.marauroa.application.core.EventBus;
import simple.marauroa.application.core.EventBusListener;
import simple.marauroa.application.core.tool.Tool;
import simple.marauroa.client.components.api.IPrivateChatManager;
import simple.marauroa.client.components.api.IUserListActionProvider;
import simple.marauroa.client.components.api.actions.UserListAction;
import simple.marauroa.client.components.chat.dialog.PrivateChatDialog;
import simple.marauroa.client.components.common.MCITool;
import static simple.server.core.action.WellKnownActionConstant.FROM;
import static simple.server.core.action.WellKnownActionConstant.TEXT;
import simple.server.core.event.PrivateTextEvent;
import simple.server.core.event.api.IPrivateChatEvent;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@ServiceProviders({
    @ServiceProvider(service = IPrivateChatManager.class),
    @ServiceProvider(service = IUserListActionProvider.class)})
public class PrivateChatManager implements IPrivateChatManager,
        IUserListActionProvider, EventBusListener<IPrivateChatEvent> {

    private static final Logger logger =
            Logger.getLogger(PrivateChatManager.class.getSimpleName());

    @Override
    public void notify(IPrivateChatEvent event) {
        if (event != null) {
            logger.log(Level.FINE, "Got notified of event: {0}", event);
            PrivateTextEvent pTextEvent = new PrivateTextEvent();
            pTextEvent.fill((RPEvent) event);
            MCITool.getPublicChatManager().addLine((pTextEvent.get(FROM) == null ? "System" : pTextEvent.get(FROM)),
                    pTextEvent.get(TEXT), NotificationType.PRIVMSG);
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

    @Override
    public List<UserListAction> getActions() {
        ArrayList<UserListAction> actions = new ArrayList<UserListAction>();
        actions.add(new StartPrivateChatAction());
        return actions;
    }

    private class StartPrivateChatAction extends UserListAction {

        private PrivateChatDialog dialog;

        public StartPrivateChatAction() {
            super(100, NbBundle.getMessage(PrivateChatManager.class,
                    "private.chat"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getDialog(EventBus.getDefault().lookup(RPObject.class).get("name")).setVisible(true);
        }

        private PrivateChatDialog getDialog(String target) {
            if (dialog == null) {
                dialog = new PrivateChatDialog(new JFrame(), target, true);
            }
            Tool.centerDialog(dialog);
            return dialog;
        }

        @Override
        public void updateStatus() {
            //Active if it is not me
            setEnabled(!isMe(MCITool.getClient().getPlayerRPC()));
        }
    }
}
