package simple.marauroa.client.components.api;

import simple.marauroa.application.core.EventBusListener;
import simple.server.core.event.api.IPrivateChatEvent;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public interface IPrivateChatComponent extends 
        EventBusListener<IPrivateChatEvent>, IChatInterface {
}