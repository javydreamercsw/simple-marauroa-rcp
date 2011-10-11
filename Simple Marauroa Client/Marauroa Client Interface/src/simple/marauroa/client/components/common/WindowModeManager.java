package simple.marauroa.client.components.common;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Lookup;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class WindowModeManager {

    private static final Logger logger =
            Logger.getLogger(WindowModeManager.class.getSimpleName());

    private WindowModeManager() {
    }

    public static String getModeList() {
        Set<? extends Mode> modes = WindowManager.getDefault().getModes();
        StringBuilder sb = new StringBuilder("Registered modes: \n");
        Iterator<? extends Mode> iterator = modes.iterator();
        while (iterator.hasNext()) {
            Mode mode = iterator.next();
            sb.append(mode.getName()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Allows to change the location of a {@link TopComponent} programmatically
     *
     * @param component
     * @param mode
     */
    public static void changeMode(TopComponent component, String mode) {
        Mode newMode = WindowManager.getDefault().findMode(mode);
        if (newMode != null) {
            newMode.dockInto(component);
            if (!component.isShowing()) {
                component.setVisible(true);
            }
        } else {
            logger.log(Level.WARNING,
                    "Unable to change location of: {0}. Mode: {1} "
                    + "is not registered.", new Object[]{component, mode});
            logger.warning(getModeList());
        }
    }
}
