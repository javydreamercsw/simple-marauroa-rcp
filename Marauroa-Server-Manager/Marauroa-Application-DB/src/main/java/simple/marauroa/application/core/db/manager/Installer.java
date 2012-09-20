package simple.marauroa.application.core.db.manager;

import org.openide.modules.ModuleInstall;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        //Initialize database
        DataBaseManager.get();
    }
}
