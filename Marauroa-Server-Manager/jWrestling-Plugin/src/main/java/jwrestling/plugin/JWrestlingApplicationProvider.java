package jwrestling.plugin;

import org.openide.util.lookup.ServiceProvider;
import simple.marauroa.application.api.IMarauroaApplicationProvider;
import simple.marauroa.application.core.DefaultMarauroaProvider;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = IMarauroaApplicationProvider.class)
public class JWrestlingApplicationProvider extends DefaultMarauroaProvider {

    public JWrestlingApplicationProvider() {
        setTemplate(new JWrestlingApplication());
    }
}
