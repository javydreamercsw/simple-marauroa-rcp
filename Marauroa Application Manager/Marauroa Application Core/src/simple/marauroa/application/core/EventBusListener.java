package simple.marauroa.application.core;

/**
 * This code is taken from OpenBlueSky project. For some reason I'm unable to 
 * retrieve the original files so I ended up copying code from:
 * http://netbeans.dzone.com/news/publish-subscribe-netbeans-pla?page=0,1
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public interface EventBusListener<T> {

    public void notify(T object);
}
