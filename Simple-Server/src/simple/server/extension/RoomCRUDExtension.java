package simple.server.extension;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import marauroa.common.Log4J;
import marauroa.common.Logger;
import marauroa.common.game.IRPZone.ID;
import marauroa.common.game.RPAction;
import marauroa.common.game.RPObject;
import simple.common.NotificationType;
import simple.common.game.ClientObjectInterface;
import simple.server.core.action.ActionListener;
import simple.server.core.action.CommandCenter;
import simple.server.core.engine.SimpleRPWorld;
import simple.server.core.engine.SimpleRPZone;
import simple.server.core.engine.SimpleSingletonRepository;
import simple.server.core.event.TextEvent;
import simple.server.core.event.ZoneEvent;

/**
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 * # load SimpleServerExtension(s).
 * RoomCRUD=simple.server.extension.RoomCRUDExtension
 * server_extension=RoomCRUD
 */
public class RoomCRUDExtension extends SimpleServerExtension implements ActionListener {
//Create, Read, Update or Remove action

    /** the logger instance. */
    private static final Logger logger = Log4J.getLogger(RoomCRUDExtension.class);
    public static final String TYPE = "CRUDRoom", ROOM = "room", DESC = "description", OPERATION = "operation", PASSWORD = "password";
    public static final int CREATE = 1, UPDATE = 2, DELETE = 3, LISTZONES = 4, LISTPLAYERS = 5, JOIN = 6;

    @Override
    public void init() {
        CommandCenter.register(TYPE, this);
        ZoneEvent.generateRPClass();
    }

    @Override
    public void onAction(RPObject rpo, RPAction action) {
        if (rpo instanceof ClientObjectInterface) {
            ClientObjectInterface player = (ClientObjectInterface) rpo;
            int op = action.getInt(OPERATION);
            try {
                switch (op) {
                    case CREATE:
                        create(player, action);
                        break;
                    case UPDATE:
                        update(action);
                        break;
                    case DELETE:
                        remove(player, action);
                        break;
                    case LISTZONES:
                        list(player, op, action);
                        break;
                    case LISTPLAYERS:
                        list(player, op, action);
                        break;
                    case JOIN:
                        join(player, action);
                        break;
                    default:
                        logger.warn("Invalid CRUD operation: " + op);
                }
            } catch (Exception e) {
                logger.error("Error processing CRUD room operation: " + op + "."
                        + " Action: " + action, e);
            }
        }
    }

    private void create(ClientObjectInterface player, RPAction action) {
        SimpleRPWorld world = (SimpleRPWorld) SimpleSingletonRepository.get().get(SimpleRPWorld.class);
        //Make sure the zone doesn't exists!
        if (!world.hasRPZone(new ID(action.get(ROOM)))) {
            SimpleRPZone zone = new SimpleRPZone(action.get(ROOM));
            if (action.get(DESC) != null && !action.get(DESC).isEmpty()) {
                zone.setDescription(action.get(DESC));
            }
            if (action.get(PASSWORD) != null && !action.get(PASSWORD).isEmpty()) {
                try {
                    zone.setPassword(action.get(PASSWORD));
                } catch (IOException ex) {
                    logger.error(ex);
                }
            }
            world.addRPZone(zone);
            world.changeZone(action.get(ROOM), (RPObject) player);
        } else {
            player.sendPrivateText(NotificationType.PRIVMSG, "Sorry, that room already exists!");
        }
    }

    private void join(ClientObjectInterface player, RPAction action) {
        //If in same room, tell the player. The client should handle this but just in case...
        if (action.get(ROOM).equals(((RPObject) player).get("zoneid"))) {
            player.sendPrivateText("You already are in " + action.get(ROOM) + " room.");
        } //Make sure the zone exists...
        else if (SimpleSingletonRepository.get().get(SimpleRPWorld.class).hasRPZone(new ID(action.get(ROOM)))) {
            SimpleRPZone jZone = (SimpleRPZone) SimpleSingletonRepository.get().get(SimpleRPWorld.class).getRPZone(((RPObject) player).get("zoneid"));
            //If it's locked it means you need a password, you better have it...
            if (jZone.isLocked()) {
                if (action.get(PASSWORD) != null) {
                    logger.debug("Room is locked but password is provided...");
                    if (jZone.isPassword(action.get(PASSWORD))) {
                        logger.debug("Password correct, changing zone...");
                        SimpleSingletonRepository.get().get(SimpleRPWorld.class).changeZone(action.get(ROOM), (RPObject) player);
                    } else {
                        ZoneEvent re = new ZoneEvent(action, ZoneEvent.NEEDPASS);
                        logger.debug("Room is locked. " + re);
                        logger.debug("Wrong password, requesting again...");
                        ((RPObject) player).addEvent(re);
                        player.notifyWorldAboutChanges();
                    }
                }
            } else {
                //The room is open so just join it.
                SimpleSingletonRepository.get().get(SimpleRPWorld.class).changeZone(action.get(ROOM), (RPObject) player);
            }
        }
    }

    private void update(RPAction action) {
        SimpleRPWorld world = (SimpleRPWorld) SimpleSingletonRepository.get().get(SimpleRPWorld.class);
        world.updateRPZoneDescription(action.get(ROOM), action.get(DESC));
        world.applyPublicEvent(null,
                new ZoneEvent((SimpleRPZone) world.getRPZone(action.get(ROOM)),
                ZoneEvent.UPDATE));
    }

    private void remove(ClientObjectInterface player, RPAction action) {
        SimpleRPWorld world = (SimpleRPWorld) SimpleSingletonRepository.get().get(SimpleRPWorld.class);
        SimpleRPZone zone = (SimpleRPZone) world.getRPZone(new ID(action.get(ROOM)));
        Collection<ClientObjectInterface> players = zone.getPlayers();
        for (ClientObjectInterface clientObject : players) {
            world.changeZone(SimpleRPWorld.getDefaultRoom(), (RPObject) clientObject);
        }
        if (zone != null) {
            try {
                world.removeRPZone(new ID(action.get(ROOM)));
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(RoomCRUDExtension.class.getSimpleName()).log(Level.SEVERE, null, ex);
            }
        }
        world.applyPublicEvent(null,
                new ZoneEvent(new SimpleRPZone(action.get(ROOM)),
                ZoneEvent.REMOVE));
        for (ClientObjectInterface p : zone.getPlayers()) {
            p.notifyWorldAboutChanges();
        }
        if (player != null) {
            ((RPObject) player).addEvent(new TextEvent("Command completed", "System"));
            player.notifyWorldAboutChanges();
        }
    }

    private void list(ClientObjectInterface player, int option, RPAction a) {
        try {
            if (option == LISTZONES) {
                ((RPObject) player).addEvent(new ZoneEvent(SimpleSingletonRepository.get().get(SimpleRPWorld.class).listZones().toString(), option));
            }
            if (option == LISTPLAYERS) {
                ((RPObject) player).addEvent(new ZoneEvent(SimpleSingletonRepository.get().get(SimpleRPWorld.class).getZone(((RPObject) player).get("zoneid")).getPlayersInString(), option));
            }
            player.notifyWorldAboutChanges();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(RoomCRUDExtension.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
    }
}