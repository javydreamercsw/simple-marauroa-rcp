package simple.marauroa.application.core.db.manager;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;
import javax.swing.JDialog;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import simple.marauroa.application.api.IAddApplicationDialogProvider;
import simple.marauroa.application.api.IMarauroaApplication;
import simple.marauroa.application.api.IMarauroaApplicationProvider;
import simple.marauroa.application.core.EventBus;
import simple.marauroa.application.core.EventBusListener;
import simple.marauroa.application.core.MarauroaApplication;
import simple.marauroa.application.core.MarauroaApplicationRepository;
import simple.marauroa.application.core.db.Application;
import simple.marauroa.application.core.db.ApplicationPK;
import simple.marauroa.application.core.db.ApplicationType;
import simple.marauroa.application.core.db.controller.ApplicationJpaController;
import simple.marauroa.application.core.db.controller.ApplicationTypeJpaController;
import simple.marauroa.application.core.db.controller.exceptions.IllegalOrphanException;
import simple.marauroa.application.core.db.controller.exceptions.NonexistentEntityException;
import simple.marauroa.application.core.db.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public final class DataBaseManager implements EventBusListener<IMarauroaApplication> {

    private static HashMap<String, String> parameters = new HashMap<String, String>();
    private static String puName = "Database_LayerPU";
    private static Map<String, Object> properties = null;
    private static DataBaseManager instance = null;
    private static EntityManagerFactory emf = null;
    private static String applicationPath = MarauroaApplication.workingDir
            + "Applications" + System.getProperty("file.separator");
    private static HashMap<String, IMarauroaApplicationProvider> providers =
            new HashMap<String, IMarauroaApplicationProvider>();

    /**
     * @return the properties
     */
    public static Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * @return the applicationPath
     */
    public static String getApplicationPath() {
        return applicationPath;
    }

    /**
     * @param aApplicationPath the applicationPath to set
     */
    public static void setApplicationPath(String aApplicationPath) {
        applicationPath = aApplicationPath;
    }

    private DataBaseManager(String pu) {
        puName = pu;
        emf = Persistence.createEntityManagerFactory(pu);
    }

    public static DataBaseManager get(String pu) {
        if (instance == null
                || (instance != null && !instance.getPersistenceUnitName().equals(pu))) {
            instance = new DataBaseManager(pu);
            EventBus.getDefault().subscribe(IMarauroaApplication.class, instance);
            getEntityManager();
            //SCR 2417: Store the new Persistence Name
            puName = pu;
        }
        return instance;
    }

    public static DataBaseManager get() {
        if (instance == null
                || (instance != null && !instance.getPersistenceUnitName().equals(puName))) {
            instance = new DataBaseManager(puName);
            EventBus.getDefault().subscribe(IMarauroaApplication.class, instance);
            getEntityManager();
        }
        return instance;
    }

    /**
     * @return the emf
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        get();
        return emf;
    }

    private static EntityManager getEntityManager() {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        properties = em.getProperties();
        return em;
    }

    public static List createdQuery(String query) {
        return createdQuery(query, null);
    }

    public static List createdQuery(String query, HashMap parameters) {
        getEntityManager().getTransaction().begin();
        Query q = getEntityManager().createQuery(query);
        if (parameters != null) {
            Iterator entries = parameters.entrySet().iterator();
            while (entries.hasNext()) {
                Entry e = (Entry) entries.next();
                q.setParameter(e.getKey().toString(), e.getValue());
            }
        }
        return q.getResultList();
    }

    public static List namedQuery(String query) {
        return namedQuery(query, null);
    }

    public static List namedQuery(String query, HashMap parameters) {
        getEntityManager().getTransaction().begin();
        Query q = getEntityManager().createNamedQuery(query);
        if (parameters != null) {
            Iterator entries = parameters.entrySet().iterator();
            while (entries.hasNext()) {
                Entry e = (Entry) entries.next();
                q.setParameter(e.getKey().toString(), e.getValue());
            }
        }
        return q.getResultList();
    }

    public void persist(Object object) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            //SCR 2546: PMD suggestions
            Exceptions.printStackTrace(e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    static void close() {
        getEntityManager().close();
        getEntityManagerFactory().close();
    }

    public static EntityTransaction getTransaction() {
        return getEntityManager().getTransaction();
    }

    /**
     * @return the puName
     */
    public String getPersistenceUnitName() {
        return puName;
    }

    public static List<Application> findApplication(IMarauroaApplication app) {
        Logger.getLogger(DataBaseManager.class.getSimpleName()).log(Level.FINE,
                "Looking for application: {0}", app.getName());
        parameters.clear();
        parameters.put("name", app.getName());
        return (List<Application>) DataBaseManager.namedQuery("Application.findByName", parameters);
    }

    public static boolean applicationExists(String name) {
        parameters.clear();
        parameters.put("name", name);
        return !DataBaseManager.namedQuery("Application.findByName", parameters).isEmpty();
    }

    public static void addApplication(IMarauroaApplication app) {
        //Make sure it doesn't exist already
        if (findApplication(app).isEmpty()) {
            try {
                //First make sure the application type exists
                if (findApplicationType(app).isEmpty()) {
                    addApplicationType(app);
                }
                ApplicationType type = findApplicationType(app).get(0);
                ApplicationPK newAppPK = new ApplicationPK(type.getId());
                Logger.getLogger(DataBaseManager.class.getSimpleName()).log(Level.FINE,
                        "Adding application : {0}", app.getName());
                Application newApp = new Application(newAppPK, true, app.getVersion());
                newApp.setName(app.getName());
                newApp.setPath(getApplicationPath() + app.getName());
                newApp.setApplicationType1(type);
                newApp.setVersion(app.getVersion());
                new ApplicationJpaController(getEntityManagerFactory()).create(newApp);
            } catch (PreexistingEntityException ex) {
                Exceptions.printStackTrace(ex);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    private static ApplicationType addApplicationType(IMarauroaApplication app)
            throws PreexistingEntityException, Exception {
        //Make sure it doesn't exist already
        ApplicationType appType = null;
        if (findApplicationType(app).isEmpty()) {
            Logger.getLogger(DataBaseManager.class.getSimpleName()).log(Level.FINE,
                    "Adding application type: {0}", app.getClass().getSimpleName());
            appType = new ApplicationType(app.getClass().getSimpleName(),
                    app.getClass().getCanonicalName());
            new ApplicationTypeJpaController(getEntityManagerFactory()).create(appType);
        }
        return appType;
    }

    public static List<ApplicationType> findApplicationType(IMarauroaApplication app) {
        Logger.getLogger(DataBaseManager.class.getSimpleName()).log(Level.FINE,
                "Looking for application type: {0}", app.getClass().getSimpleName());
        parameters.clear();
        parameters.put("class1", app.getClass().getCanonicalName());
        return (List<ApplicationType>) DataBaseManager.namedQuery("ApplicationType.findByClass1", parameters);
    }

    public static List<Application> getApplications() {
        return (List<Application>) DataBaseManager.namedQuery("Application.findAll");
    }

    public static List<ApplicationType> getApplicationTypes() {
        return (List<ApplicationType>) DataBaseManager.namedQuery("ApplicationType.findAll");
    }

    public static void deleteApplication(Application app) throws NonexistentEntityException {
        new ApplicationJpaController(getEntityManagerFactory()).destroy(app.getApplicationPK());
    }

    public static void deleteApplicationType(ApplicationType appType) {
        try {
            new ApplicationTypeJpaController(getEntityManagerFactory()).destroy(appType.getId());
        } catch (IllegalOrphanException ex) {
            Exceptions.printStackTrace(ex);
        } catch (NonexistentEntityException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /*
     * Load applications already on file system
     */
    public static void loadApplications() {
        File path = null;
        //Load from database
        for (Application app : DataBaseManager.getApplications()) {
            //Check if the application path exists
            path = new File(app.getPath());
            Logger.getLogger(DataBaseManager.class.getSimpleName()).log(Level.FINE,
                    "Looking for application path at: {0}", app.getPath());
            if (!path.exists()) {
                NotifyDescriptor nd = new NotifyDescriptor.Confirmation(
                        "<html><font size=+1 color=red>"
                        + NbBundle.getMessage(
                        MarauroaApplication.class,
                        "acknowledge")
                        + "</font><br>"
                        + "<br>"
                        + NbBundle.getMessage(
                        MarauroaApplication.class,
                        "application.dir.not.exists").replaceAll("%a", app.getName())
                        + "<b><b>" + NbBundle.getMessage(
                        MarauroaApplication.class,
                        "application.dir.not.exists.fix") + "<br>"
                        + "&nbsp;&nbsp;<b>" + NbBundle.getMessage(
                        MarauroaApplication.class,
                        "yes") + "</b> - to Correct<br>"
                        + "&nbsp;&nbsp;<b>" + NbBundle.getMessage(
                        MarauroaApplication.class,
                        "no") + "</b> - to Delete<br>",
                        NotifyDescriptor.YES_NO_OPTION);
                Object result = DialogDisplayer.getDefault().notify(nd);
                if (result == NotifyDescriptor.YES_OPTION) {
                    IAddApplicationDialogProvider dialogProvider =
                            Lookup.getDefault().lookup(IAddApplicationDialogProvider.class);
                    JDialog dialog = dialogProvider.getDialog();
                    dialogProvider.setApplicationName(app.getName());
                    dialogProvider.setEditableApplicationName(false);
                    dialogProvider.setIgnoreFolderCreation(true);
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                } else if (result == NotifyDescriptor.NO_OPTION) {
                    try {
                        //Delete!
                        deleteApplication(app);
                        //Delete the folder as well
                        MarauroaApplicationRepository.deleteFolder(path);
                    } catch (NonexistentEntityException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            } else {
                IMarauroaApplication application = getMarauroaApplication(app);
                if (application != null) {
                    //Notify the EventBus, the listeners will take care of the rest
                    EventBus.getDefault().add(application);
                }
            }
        }
        //Now look in the file system
        path = new File(MarauroaApplication.workingDir + "Applications");
        if (path.exists()) {
            for (File temp : path.listFiles()) {
                //We are looking for directories only
                if (temp.isDirectory()) {
                    //Check if it is on the database
                    if (!DataBaseManager.applicationExists(temp.getName())) {
                        NotifyDescriptor nd = new NotifyDescriptor.Confirmation(
                                "<html><font size=+1 color=red>"
                                + NbBundle.getMessage(
                                MarauroaApplication.class,
                                "acknowledge")
                                + "</font><br>"
                                + "<br>"
                                + NbBundle.getMessage(
                                MarauroaApplication.class,
                                "application.found.orphan.directory").replaceAll("%d",
                                temp.toURI().getPath()) + "<br>"
                                + "<b><b>" + NbBundle.getMessage(
                                MarauroaApplication.class,
                                "application.dir.not.exists.fix") + "<br>"
                                + "&nbsp;&nbsp;<b>" + NbBundle.getMessage(
                                MarauroaApplication.class,
                                "yes") + "</b> - to Correct<br>"
                                + "&nbsp;&nbsp;<b>" + NbBundle.getMessage(
                                MarauroaApplication.class,
                                "no") + "</b> - to Delete<br>",
                                NotifyDescriptor.YES_NO_OPTION);
                        Object result = DialogDisplayer.getDefault().notify(nd);
                        if (result == NotifyDescriptor.YES_OPTION) {
                            IAddApplicationDialogProvider dialogProvider =
                                    Lookup.getDefault().lookup(IAddApplicationDialogProvider.class);
                            JDialog dialog = dialogProvider.getDialog();
                            dialogProvider.setApplicationName(temp.getName());
                            dialogProvider.setEditableApplicationName(false);
                            dialogProvider.setIgnoreFolderCreation(false);
                            dialog.setLocationRelativeTo(null);
                            dialog.setVisible(true);
                        } else if (result == NotifyDescriptor.NO_OPTION) {
                            //Delete the folder as well
                            MarauroaApplicationRepository.deleteFolder(temp);
                        }
                    }
                }
            }
        }
        //Update applications as well
        for (IMarauroaApplication app :
                EventBus.getDefault().lookupAll(IMarauroaApplication.class)) {
            app.update();
        }
    }

    protected static IMarauroaApplication getMarauroaApplication(Application app) {
        IMarauroaApplicationProvider provider = getProviderForClass(app.getApplicationType1().getClass1());
        if (provider != null) {
            return provider.convertToIMarauroaApplication(
                    app.getApplicationType1().getClass1(),
                    app.getName(), app.getVersion(), app.getPath());
        } else {
            return null;
        }
    }

    private static IMarauroaApplicationProvider getProviderForClass(String clazz) {
        if (!providers.containsKey(clazz)) {
            //Update map
            for (IMarauroaApplicationProvider app : Lookup.getDefault().lookupAll(IMarauroaApplicationProvider.class)) {
                if (app.getTemplate().getClass().getCanonicalName().equals(clazz)) {
                    providers.put(clazz, app);
                    break;
                }
            }
        }
        return providers.get(clazz);
    }

    @Override
    public void notify(IMarauroaApplication app) {
        //Check to see if the application is registered, 
        //if not register it in the database
        if (app != null) {
            DataBaseManager.addApplication(app);
        }
    }
}
