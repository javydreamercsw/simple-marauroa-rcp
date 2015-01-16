package simple.marauroa.application.core.executor;

import java.util.concurrent.ExecutionException;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public interface IMarauroaProcess {

    /**
     * Executes the Marauroa Application
     *
     * @return integer
     */
    public Integer execute() throws ExecutionException, InterruptedException;

    /**
     * Get the process name
     *
     * @return
     */
    public String getProcessName();

    /*
     * Stops the Marauroa Application
     */
    public void stop();
}
