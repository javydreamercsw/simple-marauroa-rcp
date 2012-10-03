package simple.marauroa.client.components.api;

import java.util.List;
import simple.marauroa.application.core.MarauroaActionProvider;
import simple.marauroa.client.components.api.actions.UserListAction;

/**
 * This interface provides an API to customize the actions assigned for the
 * players in the list.
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public interface IUserListActionProvider extends MarauroaActionProvider {

    @Override
    List<UserListAction> getActions();
}
