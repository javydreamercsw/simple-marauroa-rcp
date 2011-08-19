package simple.server.core.event;

import marauroa.common.game.RPEvent;
import simple.server.core.event.api.IRPEvent;

/**
 * This class just wraps Marauroa's RPEvent to implement IRPEvent.
 * This should be considered a hack until the interfaces are within the 
 * Marauroa package (if they finally agree)
 * 
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class SimpleRPEvent extends RPEvent implements IRPEvent {

    public SimpleRPEvent(RPEvent event) {
        fill(event);
    }

    protected SimpleRPEvent(String name) {
        super(name);
    }
}
