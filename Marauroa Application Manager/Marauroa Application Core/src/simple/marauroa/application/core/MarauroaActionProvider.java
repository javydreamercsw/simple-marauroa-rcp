package simple.marauroa.application.core;

import java.util.List;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public interface MarauroaActionProvider {

    List<? extends MarauroaAction> getActions();
}
