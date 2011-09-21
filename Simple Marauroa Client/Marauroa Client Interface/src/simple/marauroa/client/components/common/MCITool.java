package simple.marauroa.client.components.common;

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

    public static void outputInChatComponent(EventLine el) {
        IPublicChatComponent app = Lookup.getDefault().lookup(IPublicChatComponent.class);
        if (app != null) {
            app.addLine(el);
        }
    }

    /**
     * Get client implementation
     * @return client implementation
     */
    public static IClientFramework getClient() {
        return Lookup.getDefault().lookup(IClientFramework.class);
    }

    /**
     * Get public chat implementation
     * @return chat implementation
     */
    public static IPublicChatComponent getPublicChatManager() {
        return Lookup.getDefault().lookup(IPublicChatComponent.class);
    }
    
    /**
     * Get private chat implementation
     * @return chat implementation
     */
    public static IPrivateChatComponent getPrivateChatManager() {
        return Lookup.getDefault().lookup(IPrivateChatComponent.class);
    }

    /**
     * Get login implementation
     * @return login implementation
     */
    public static ILoginManager getLoginManager() {
        return Lookup.getDefault().lookup(ILoginManager.class);
    }

    /**
     * Get create account implementation
     * @return account implementation
     */
    public static ICAManager getCAManager() {
        return Lookup.getDefault().lookup(ICAManager.class);
    }

    public static IUserListComponent getUserListManager() {
        return Lookup.getDefault().lookup(IUserListComponent.class);
    }
    
    public static IZoneListManager getZoneListManager() {
        return Lookup.getDefault().lookup(IZoneListManager.class);
    }
}
