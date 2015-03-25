package simple.marauroa.application.core;

import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;
import simple.marauroa.application.api.IMarauroaApplication;
import simple.marauroa.application.api.IMarauroaApplicationProvider;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = IMarauroaApplicationProvider.class)
public class DefaultMarauroaProvider implements IMarauroaApplicationProvider {

    protected IMarauroaApplication template = new DefaultMarauroaApplication();

    @Override
    public IMarauroaApplication getTemplate() {
        return template;
    }

    @Override
    public void setTemplate(IMarauroaApplication template) {
        this.template = template;
    }

    @Override
    public IMarauroaApplication convertToIMarauroaApplication(String clazz,
            String name, String version, String path) {
        //Recreate the IMarauroaApplication
        IMarauroaApplication application = null;
        try {
            application
                    = (IMarauroaApplication) getTemplate().getClass().newInstance();
            application.setName(name);
            application.setVersion(version);
            application.setApplicationPath(path);
        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
        return application;
    }
}
