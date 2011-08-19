package simple.marauroa.client.components.common;

import org.openide.util.Lookup;
import simple.client.EventLine;
import simple.marauroa.client.components.api.ICAManager;
import simple.marauroa.client.components.api.IChatComponent;
import simple.marauroa.client.components.api.IClientFramework;
import simple.marauroa.client.components.api.ILoginManager;

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
        IChatComponent app = Lookup.getDefault().lookup(IChatComponent.class);
        if (app != null) {
            app.addLine(el);
        }
    }

    public static IClientFramework getClient() {
        return Lookup.getDefault().lookup(IClientFramework.class);
    }
    
    public static ILoginManager getLoginManager(){
        return Lookup.getDefault().lookup(ILoginManager.class);
    }
    
    public static ICAManager getCAManager(){
        return Lookup.getDefault().lookup(ICAManager.class);
    }
}
