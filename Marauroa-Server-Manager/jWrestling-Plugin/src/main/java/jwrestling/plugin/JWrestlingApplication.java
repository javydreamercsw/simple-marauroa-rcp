/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwrestling.plugin;

import java.awt.Image;
import simple.marauroa.application.core.MarauroaApplication;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class JWrestlingApplication extends MarauroaApplication{
    public JWrestlingApplication() {
        super("jWrestling Application");
        //TODO: Read from POM?
        setVersion("1.0");
    }

    public JWrestlingApplication(String name) {
        super(name);
        //TODO: Read from POM?
        setVersion("1.0");
    }

    @Override
    public Image getIcon(int type) {
        //TODO: Use jWrestling icon
        return null;
    }
}
