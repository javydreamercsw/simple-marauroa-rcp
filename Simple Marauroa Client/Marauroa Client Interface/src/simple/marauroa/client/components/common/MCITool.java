package simple.marauroa.client.components.common;

import java.util.Iterator;
import org.openide.util.Lookup;
import simple.client.EventLine;
import simple.marauroa.client.components.api.*;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class MCITool {

    private static MCITool instance;

    private MCITool() {
    }

    public static MCITool get() {
        if (instance == null) {
            instance = new MCITool();
        }
        return instance;
    }

    /**
     * Output to the chat component
     *
     * @param el
     */
    public static void outputInChatComponent(EventLine el) {
        IPublicChatComponent app = Lookup.getDefault().lookup(IPublicChatComponent.class);
        if (app != null) {
            app.addLine(el);
        }
    }

    /**
     * Get client implementation
     *
     * @return client implementation
     */
    public static IClientFramework getClient() {
        IClientFramework client = null;
        for (Iterator<? extends IClientFramework> it = Lookup.getDefault().lookupAll(IClientFramework.class).iterator(); it.hasNext();) {
            client = it.next();
        }
        return client;
    }

    /**
     * Get public chat implementation
     *
     * @return chat implementation
     */
    public static IPublicChatComponent getPublicChatManager() {
        return Lookup.getDefault().lookup(IPublicChatComponent.class);
    }

    /**
     * Get private chat implementation
     *
     * @return chat implementation
     */
    public static IPrivateChatManager getPrivateChatManager() {
        return Lookup.getDefault().lookup(IPrivateChatManager.class);
    }

    /**
     * Get login implementation
     *
     * @return login implementation
     */
    public static ILoginManager getLoginManager() {
        return Lookup.getDefault().lookup(ILoginManager.class);
    }

    /**
     * Get create account implementation
     *
     * @return account implementation
     */
    public static ICAManager getCAManager() {
        return Lookup.getDefault().lookup(ICAManager.class);
    }

    /**
     * Get the User List implementation
     *
     * @return User List implementation
     */
    public static IUserListManager getUserListManager() {
        return Lookup.getDefault().lookup(IUserListManager.class);
    }

    /**
     * Get the Zone List implementation
     *
     * @return Zone List implementation
     */
    public static IZoneListManager getZoneListManager() {
        return Lookup.getDefault().lookup(IZoneListManager.class);
    }
}
