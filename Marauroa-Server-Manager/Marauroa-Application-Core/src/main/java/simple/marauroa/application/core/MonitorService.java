package simple.marauroa.application.core;

import simple.marauroa.application.api.IApplicationMonitor;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public interface MonitorService {

    void addMonitor(String name) throws Exception;

    IApplicationMonitor getMonitor(String name);

    void removeMonitor(String name);

    void shutdown();
}
