package simple.marauroa.event.manager.zone;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import marauroa.client.ClientFramework;
import marauroa.common.game.IRPZone;
import marauroa.common.game.RPAction;
import marauroa.common.game.RPEvent;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import org.openide.windows.TopComponent;
import simple.marauroa.application.core.Zone;
import simple.marauroa.application.core.tool.Tool;
import simple.marauroa.client.components.api.IClientFramework;
import simple.marauroa.client.components.api.IZoneListActionProvider;
import simple.marauroa.client.components.api.IZoneListManager;
import simple.marauroa.client.components.api.actions.ZoneListAction;
import simple.marauroa.client.components.common.MCITool;
import simple.marauroa.event.manager.zone.dialog.ZoneDialog;
import simple.server.core.engine.SimpleRPZone;
import simple.server.extension.ZoneEvent;
import simple.server.extension.ZoneExtension;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@ServiceProviders({
    @ServiceProvider(service = IZoneListManager.class),
    @ServiceProvider(service = IZoneListActionProvider.class)})
public class ZoneEventManager implements IZoneListManager, IZoneListActionProvider {

    private static final Logger logger =
            Logger.getLogger(ZoneEventManager.class.getSimpleName());
    private static ZoneListTopComponent instance;
    private ZoneDialog zd;

    private void processAdd(ZoneEvent event) {
        logger.log(Level.FINE, "Adding zone: {0}", event.get(ZoneEvent.FIELD));
        addZone(event.get(ZoneEvent.FIELD)
                + (event.get(ZoneEvent.DESC) == null ? "" : ": " + event.get(ZoneEvent.DESC)));
    }

    private void processUpdate(ZoneEvent event) {
        logger.log(Level.FINE, "Updating zone: {0}", event.get(ZoneEvent.FIELD));
        updateZone(event.get(ZoneEvent.FIELD),
                event.get(ZoneEvent.DESC));
    }

    private void processRemove(ZoneEvent event) {
        logger.log(Level.FINE, "Removing zone: {0}", event.get(ZoneEvent.FIELD));
        removeZone(event.get(ZoneEvent.FIELD));
    }

    private void processListZones(ZoneEvent event) {
        StringTokenizer st = new StringTokenizer(event.get(ZoneEvent.FIELD), "#");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            logger.log(Level.FINE, "Adding zone: {0}", token);
            addZone(token);
        }
    }

    private void processNeedPass(ZoneEvent event) {
        //TODO: Fully implement
        MCITool.getZoneListManager().requestPassword();
    }

    @Override
    public ZoneListTopComponent getComponent() {
        if (instance == null) {
            for (TopComponent tc : TopComponent.getRegistry().getOpened()) {
                if (tc instanceof ZoneListTopComponent) {
                    instance = (ZoneListTopComponent) tc;
                    break;
                }
            }
        }
        return instance;
    }

    /**
     * Get the appropriate dialog for the action
     *
     * @param mode Mode for the dialog
     * @return the ZoneDialog
     */
    public ZoneDialog getRoomDialog(int mode) {
        if (zd == null) {
            zd = new ZoneDialog(true, mode);
        } else {
            zd.update(mode);
        }
        Tool.centerDialog(zd);
        zd.setVisible(true);
        return zd;
    }

    @Override
    public void addZone(String zone) {
        Zone newZone;
        String desc = null;
        if (zone.indexOf(':') > 0) {
            desc = zone.substring(zone.indexOf(':') + 2);
            zone = zone.substring(0, zone.indexOf(':'));
        }
        newZone = new Zone(zone);
        if (desc != null) {
            newZone.setDescription(desc);
        }
        Lookup.getDefault().lookup(IClientFramework.class).addToLookup(newZone);
    }

    @Override
    public void removeZone(String zone) {
        for (Iterator<? extends IRPZone> it = Utilities.actionsGlobalContext().lookupAll(IRPZone.class).iterator(); it.hasNext();) {
            IRPZone z = it.next();
            String zoneName = Tool.getZoneName(z.getID());
            if (zoneName.indexOf(':') > 0) {
                //Remove the description
                zoneName = zoneName.substring(0, zoneName.indexOf(':'));
            }
            if (zoneName.equals(zone)) {
                Lookup.getDefault().lookup(IClientFramework.class).removeFromLookup(z);
            }
        }
    }

    @Override
    public void updateZone(String zone, String modified) {
        for (Iterator<? extends IRPZone> it = Utilities.actionsGlobalContext().lookupAll(IRPZone.class).iterator(); it.hasNext();) {
            IRPZone z = it.next();
            String zoneName = Tool.getZoneName(z.getID());
            if (zoneName.indexOf(':') > 0) {
                //Remove the description
                zoneName = zoneName.substring(0, zoneName.indexOf(':'));
            }
            if (zoneName.equals(zone)) {
                if (z instanceof SimpleRPZone) {
                    ((SimpleRPZone) z).setDescription(modified);
                    IRPZone copy = z;
                    Lookup.getDefault().lookup(IClientFramework.class).removeFromLookup(z);
                    Lookup.getDefault().lookup(IClientFramework.class).addToLookup((Zone) copy);
                    break;
                } else {
                    logger.warning("Zone is not an instance of SimpleRPZone.");
                }
            }
        }
    }

    @Override
    public void requestPassword() {
        getComponent().requestPassword();
    }

    @Override
    public List<ZoneListAction> getActions() {
        ArrayList<ZoneListAction> actions = new ArrayList<ZoneListAction>();
        actions.add(new CreateZoneAction());
        actions.add(new EditZoneAction());
        actions.add(new DeleteZoneAction());
        actions.add(new JoinZoneAction());
        return actions;
    }

    @Override
    public void onRPEventReceived(RPEvent eventIn) throws Exception {
        if (eventIn != null && eventIn.getName().equals(ZoneEvent.RPCLASS_NAME)) {
            ZoneEvent event = new ZoneEvent();
            event.fill(eventIn);
            logger.log(Level.INFO, "Got Zone Event: {0}", event);
            //Default Zone Event
            switch (event.getInt(ZoneEvent.ACTION)) {
                case ZoneEvent.ADD:
                    processAdd(event);
                    break;
                case ZoneEvent.UPDATE:
                    processUpdate(event);
                    break;
                case ZoneEvent.REMOVE:
                    processRemove(event);
                    break;
                case ZoneEvent.LISTZONES:
                    processListZones(event);
                    break;
                case ZoneEvent.NEEDPASS:
                    processNeedPass(event);
                    break;
                default:
            }
        } else if (eventIn != null) {
            logger.log(Level.WARNING,
                    "Received the following event, "
                    + "but don't know how to handle it. \n{0}", eventIn);
        }
    }

    @Override
    public void open() {
        getComponent().open();
    }

    @Override
    public boolean close() {
        return getComponent().close();
    }

    private class JoinZoneAction extends ZoneListAction {

        public JoinZoneAction() {
            super(100, NbBundle.getMessage(ZoneEventManager.class,
                    "join.zone"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            RPAction action = new RPAction();
            action.put("type", ZoneExtension.TYPE);
            action.put(ZoneExtension.ROOM, Tool.getZoneName(
                    Utilities.actionsGlobalContext().lookup(IRPZone.class).getID()));
            action.put(ZoneExtension.OPERATION, ZoneEvent.JOIN);
            ((ClientFramework) MCITool.getClient()).send(action);
            //Need to clear the player list
            if (MCITool.getUserListManager() != null) {
                MCITool.getUserListManager().clearList();
            }
        }

        @Override
        public void updateStatus() {
            setEnabled(true);
        }
    }

    private class CreateZoneAction extends ZoneListAction {

        public CreateZoneAction() {
            super(200, NbBundle.getMessage(ZoneEventManager.class,
                    "create.zone"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getRoomDialog(ZoneEvent.ADD);
            //Request zone update
            RPAction action = new RPAction();
            action.put("type", ZoneExtension.TYPE);
            action.put(ZoneExtension.OPERATION, ZoneEvent.LISTZONES);
            action.put(ZoneExtension.SEPARATOR, "#");
            ((ClientFramework) MCITool.getClient()).send(action);
        }

        @Override
        public void updateStatus() {
            setEnabled(true);
        }
    }

    private class EditZoneAction extends ZoneListAction {

        public EditZoneAction() {
            super(300, NbBundle.getMessage(ZoneEventManager.class,
                    "edit.zone"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getRoomDialog(ZoneEvent.UPDATE);
        }

        @Override
        public void updateStatus() {
            setEnabled(true);
        }
    }

    private class DeleteZoneAction extends ZoneListAction {

        public DeleteZoneAction() {
            super(400, NbBundle.getMessage(ZoneEventManager.class,
                    "delete.zone"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            RPAction action = new RPAction();
            action.put("type", ZoneExtension.TYPE);
            action.put(ZoneExtension.ROOM, Tool.getZoneName(
                    Utilities.actionsGlobalContext().lookup(IRPZone.class).getID()));
            action.put(ZoneExtension.OPERATION, ZoneEvent.REMOVE);
            ((ClientFramework) MCITool.getClient()).send(action);
        }

        @Override
        public void updateStatus() {
            setEnabled(true);
        }
    }
}
