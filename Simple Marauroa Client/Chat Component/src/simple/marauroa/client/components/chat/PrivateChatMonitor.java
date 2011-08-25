package simple.marauroa.client.components.chat;

import org.jivesoftware.smack.util.ReaderListener;
import simple.marauroa.client.components.common.MCITool;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class PrivateChatMonitor implements ReaderListener {

    private String tabName;

    public PrivateChatMonitor(String tabName) {
        this.tabName = tabName;
    }

    @Override
    public void read(String read) {
        MCITool.getClient().sendPrivateText(read.replaceAll("\n", ""), tabName);
    }
}
