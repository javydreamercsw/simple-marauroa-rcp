package simple.marauroa.application.core;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import simple.marauroa.application.api.IMarauroaApplication;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        //This covers all the scenarios you can exit the JVM except Windows Manager (for some reason)
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                for (IMarauroaApplication app : Lookup.getDefault().lookupAll(IMarauroaApplication.class)) {
                    if (app.isRunning()) {
                        try {
                            Logger.getLogger(Installer.class.getName()).log(Level.WARNING,
                                    "Shutting down {0} in emergency mode since the JVM is closing!", app.getName());
                            app.shutdown();
                        } catch (Exception ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                }
            }
        });
    }
}
