package games.jwrestling.application;

import org.openide.util.lookup.ServiceProvider;
import simple.marauroa.application.api.IMarauroaApplication;
import simple.marauroa.application.api.IMarauroaApplicationProvider;
import simple.marauroa.application.core.DefaultMarauroaProvider;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = IMarauroaApplicationProvider.class)
public class jWrestlingProvider extends DefaultMarauroaProvider {

    private jWrestlingApplication myTemplate = new jWrestlingApplication();

    @Override
    public void setTemplate(IMarauroaApplication template) {
        this.myTemplate = (jWrestlingApplication) template;
    }

    @Override
    public IMarauroaApplication getTemplate() {
        return myTemplate;
    }
}
