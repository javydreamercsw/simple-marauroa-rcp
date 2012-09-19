package simple.marauroa.client.components.api;

import java.util.List;
import simple.marauroa.application.core.MarauroaActionProvider;
import simple.marauroa.client.components.api.actions.ZoneListAction;

/**
 * This interface provides an API to customize the actions assigned for the
 * zones in the list.
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public interface IZoneListActionProvider extends MarauroaActionProvider {

    @Override
    List<ZoneListAction> getActions();
}
