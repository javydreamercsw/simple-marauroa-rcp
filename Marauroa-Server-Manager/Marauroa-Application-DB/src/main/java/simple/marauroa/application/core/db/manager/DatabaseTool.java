package simple.marauroa.application.core.db.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;
import simple.marauroa.application.api.IDataBase;
import simple.marauroa.application.api.IMarauroaApplication;
import simple.marauroa.application.core.db.Application;
import simple.marauroa.application.core.db.controller.exceptions.NonexistentEntityException;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = IDataBase.class)
public class DatabaseTool implements IDataBase {

    private HashMap<String, String> parameters = new HashMap<>();

    @Override
    public boolean applicationExists(String name) {
        return DataBaseManager.applicationExists(name);
    }

    @Override
    public boolean deleteApplication(String name) {
        parameters.clear();
        parameters.put("name", name);
        List apps = DataBaseManager.namedQuery("Application.findByName", parameters);
        try {
            DataBaseManager.deleteApplication((Application) apps.get(0));
        } catch (NonexistentEntityException ex) {
            Exceptions.printStackTrace(ex);
            return false;
        }
        return true;
    }

    @Override
    public void addApplication(IMarauroaApplication app) {
        if (!applicationExists(app.getName())) {
            DataBaseManager.addApplication(app);
        }
    }
    
    @Override
    public IMarauroaApplication getApplication(String name){
        parameters.clear();
        parameters.put("name", name);
        List apps = DataBaseManager.namedQuery("Application.findByName", parameters);
        if(!apps.isEmpty()){
            return DataBaseManager.getMarauroaApplication((Application) apps.get(0));
        }else{
            return null;
        }
    }

    @Override
    public Collection<? extends IMarauroaApplication> getIMarauroaApplications() {
        return DataBaseManager.getMarauroaApplications();
    }
}
