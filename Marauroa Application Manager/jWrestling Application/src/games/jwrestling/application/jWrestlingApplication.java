package games.jwrestling.application;

import java.awt.Image;
import simple.marauroa.application.core.MarauroaApplication;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class jWrestlingApplication extends MarauroaApplication {

    public jWrestlingApplication() {
        super("jWrestling");
        setVersion(games.jwrestling.common.Debug.VERSION);
        setRelativeToClass(getClass());
    }

    @Override
    public Image getIcon(int type) {
        //Use default icon
        return null;
    }
}
