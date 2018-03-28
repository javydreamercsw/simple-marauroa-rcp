package simple.marauroa.application.core.db.manager;

import org.openide.modules.ModuleInstall;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {
    private static final long serialVersionUID = 3_075_153_710_072_580_133L;

    @Override
    public void restored() {
        //Initialize database
        DataBaseManager.get();
    }
}
