package simple.marauroa.application.core.tool;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import marauroa.common.game.IRPZone;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import simple.marauroa.application.core.MarauroaAction;
import simple.marauroa.application.core.MarauroaActionProvider;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public class Tool {

    private Tool() {
    }

    /**
     * Center dialog in screen
     *
     * @param dialog
     */
    public static void centerDialog(JDialog dialog) {

        Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension window = dialog.getSize();

        dialog.setLocation((s.width - window.width) / 2,
                (s.height - window.height) / 2);

    }

    //Obtain the image URL
    public static Image createImage(String module_id, String path, 
            String description)
            throws MalformedURLException, Exception {
        File icon = InstalledFileLocator.getDefault().locate(path,
                "simple.marauroa.application.gui", false);
        URL imageURL = Utilities.toURI(icon).toURL();

        if (imageURL == null) {
            throw new Exception("Resource not found: " + path);
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

    public static <T> Action[] 
        getActions(Class<? extends MarauroaActionProvider> provider) {
        ArrayList<MarauroaAction> actionList = new ArrayList<MarauroaAction>();
        for (MarauroaActionProvider providerImpl : 
                Lookup.getDefault().lookupAll(provider)) {
            actionList.addAll(providerImpl.getActions());
        }
        //Sort them in position order
        Collections.sort(actionList);
        //Update its status
        for (MarauroaAction action : actionList) {
            action.updateStatus();
        }
        Action[] actions = new Action[actionList.size()];
        actionList.toArray(actions);
        return actions;
    }

    public static String getZoneName(IRPZone.ID id) {
        String stringId = id.toString();
        return stringId.substring(stringId.indexOf('=') + 1, 
                stringId.indexOf(']'));
    }
}
