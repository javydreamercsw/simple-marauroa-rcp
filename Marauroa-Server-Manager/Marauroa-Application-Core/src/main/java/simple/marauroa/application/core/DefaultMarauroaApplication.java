package simple.marauroa.application.core;

import java.awt.Image;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class DefaultMarauroaApplication extends MarauroaApplication {

    public DefaultMarauroaApplication() {
        super("Default Marauroa Application");
        setVersion("1.0");
    }

    public DefaultMarauroaApplication(String name) {
        super(name);
        setVersion("1.0");
    }

    @Override
    public Image getIcon(int type) {
        //Use default icon
        return null;
    }
}
