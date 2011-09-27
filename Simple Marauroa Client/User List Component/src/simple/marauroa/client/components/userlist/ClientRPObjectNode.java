package simple.marauroa.client.components.userlist;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import java.net.MalformedURLException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import marauroa.common.game.RPObject;
import org.openide.nodes.BeanNode;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;
import simple.marauroa.application.core.tool.Tool;
import simple.marauroa.client.components.common.MCITool;
import simple.marauroa.client.components.userlist.dialog.PrivateChatDialog;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class ClientRPObjectNode extends BeanNode {

    private Action[] actions;
    private PrivateChatDialog dialog;

    public ClientRPObjectNode(RPObject object) throws IntrospectionException {
        super(object, null, Lookups.singleton(object));
        setDisplayName(object.get("name"));
    }

    @Override
    public Image getIcon(int type) {
        try {
            return Tool.createImage(
                    "simple.marauroa.application.gui",
                    "resources/images/entity.png",
                    "Entity icon");
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    public Action[] getActions(boolean popup) {
        actions = new Action[]{
            new StartPrivateChatAction()};
        updateActionsState();
        return actions;
    }

    private void updateActionsState() {
        for (int i = 0; i < actions.length; i++) {
            if (actions[i] != null && actions[i] instanceof StartPrivateChatAction) {
                actions[i].setEnabled(!getLookup().lookup(RPObject.class)
                        .get("name").equals(MCITool.getClient().getPlayerRPC()
                        .get("name")));
            }
        }
    }

    private class StartPrivateChatAction extends AbstractAction {

        public StartPrivateChatAction() {
            putValue(NAME, NbBundle.getMessage(ClientRPObjectNode.class, "private.chat"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getDialog(getLookup().lookup(RPObject.class).get("name")).setVisible(true);
        }

        private PrivateChatDialog getDialog(String target) {
            if (dialog == null) {
                dialog = new PrivateChatDialog(new JFrame(), target, true);
            }
            Tool.centerDialog(dialog);
            return dialog;
        }
    }
}
