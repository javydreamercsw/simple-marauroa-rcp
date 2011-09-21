package simple.marauroa.application.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import marauroa.common.Log4J;
import marauroa.common.game.IRPZone;
import marauroa.common.game.IRPZone.ID;
import marauroa.common.game.Perception;
import marauroa.common.game.RPObject;
import marauroa.common.game.RPObjectInvalidException;
import org.openide.util.Lookup;
import simple.marauroa.application.api.IDataBase;
import simple.marauroa.application.api.IMarauroaApplication;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class MarauroaApplicationRepository {

    private static MarauroaApplicationRepository instance = null;

    private MarauroaApplicationRepository() {
    }

    public static MarauroaApplicationRepository get() {
        if (instance == null) {
            instance = new MarauroaApplicationRepository();
            Log4J.init();
        }
        return instance;
    }

    public static void register(Object c) throws Exception {
        Logger.getLogger(MarauroaApplicationRepository.class.getSimpleName()).log(Level.FINE,
                "Registering Marauroa Application: {0}", c);
        //Ask the application to do its custom checks
        if (c instanceof IMarauroaApplication) {
            IMarauroaApplication app = (IMarauroaApplication) c;
            app.update();
            //Register in the EventBus
            EventBus.getDefault().add(app);
            Lookup.getDefault().lookup(IDataBase.class).addApplication(app);
        } else {
            throw new Exception("Tried to register non-application object: " + c);
        }
    }

    public static void unregister(Object c) throws Exception {
        Logger.getLogger(MarauroaApplicationRepository.class.getSimpleName()).log(Level.FINE,
                "Unregistering Marauroa Application: {0}", c);
        //Unregister in the EventBus
        EventBus.getDefault().remove(c);
    }

    public static void deleteApplication(IMarauroaApplication app) throws Exception {
        if (app != null) {
            app.shutdown();
            app.deleteAppDirectory();
            MarauroaApplicationRepository.unregister(app);
            //Delete from database as well
            Lookup.getDefault().lookup(IDataBase.class).deleteApplication(app.getName());
        }
    }

    public static Collection<? extends IMarauroaApplication> getIMarauroaApplications() {
        return EventBus.getDefault().lookupAll(IMarauroaApplication.class);
    }

    public static Collection<? extends IRPZone> getZonesForApplication(IMarauroaApplication app) {
        ArrayList<IRPZone> zones = new ArrayList<IRPZone>();
        for (final String key : app.getContents().keySet()) {
            //This is just a dumy zone with the correct name
            IRPZone newZone = new IRPZone() {

                private ArrayList<RPObject> objects = new ArrayList<RPObject>();

                @Override
                public ID getID() {
                    return new ID(key);
                }

                @Override
                public void onInit() throws Exception {
                    //Do nothing
                }

                @Override
                public void onFinish() throws Exception {
                    //Do nothing
                }

                @Override
                public void add(RPObject rpo) throws RPObjectInvalidException {
                    objects.add(rpo);
                }

                @Override
                public void modify(RPObject rpo) throws RPObjectInvalidException {
                    //Do nothing
                }

                @Override
                public RPObject remove(RPObject.ID id) {
                    //Do nothing
                    return null;
                }

                @Override
                public void hide(RPObject rpo) {
                    //Do nothing
                }

                @Override
                public void unhide(RPObject rpo) {
                    //Do nothing
                }

                @Override
                public RPObject get(RPObject.ID id) {
                    //Do nothing
                    return null;
                }

                @Override
                public boolean has(RPObject.ID id) {
                    //Do nothing
                    return false;
                }

                @Override
                public void assignRPObjectID(RPObject rpo) {
                    //Do nothing
                }

                @Override
                public Iterator<RPObject> iterator() {
                    return null;
                }

                @Override
                public long size() {
                    return objects.size();
                }

                @Override
                public Perception getPerception(RPObject rpo, byte b) {
                    return null;
                }

                @Override
                public void nextTurn() {
                    //Do nothing
                }
            };
            //Now add the objects to it
            for (RPObject object : app.getContents().get(key)) {
                newZone.add(object);
            }
            zones.add(newZone);
        }
        return zones;
    }

    public static Collection<? extends RPObject> getRPObjectsForZone(String appName, ID zone) {
        ArrayList<RPObject> objects = new ArrayList<RPObject>();
        RPObject object = new RPObject();
        object.put("id", 1);
        object.put("name", "Object 1");
        objects.add(object);
        //TODO: replace with real implementation
        return objects;
    }

    public static void deleteFolder(File folder) {
        if (folder.exists() && folder.isDirectory()) {
            for (File temp : folder.listFiles()) {
                if (temp.isDirectory()) {
                    deleteFolder(temp);
                } else {
                    temp.delete();
                }
            }
            folder.delete();
        }
    }
        }
