package simple.marauroa.application.api;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public interface IMarauroaApplicationProvider {

    public void setTemplate(IMarauroaApplication template);

    public IMarauroaApplication getTemplate();

    public IMarauroaApplication convertToIMarauroaApplication(String clazz,
            String name, String version, String path);
}
