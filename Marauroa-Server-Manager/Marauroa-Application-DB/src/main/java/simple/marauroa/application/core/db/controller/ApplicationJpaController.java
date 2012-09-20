/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.marauroa.application.core.db.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import simple.marauroa.application.core.db.ApplicationType;
import simple.marauroa.application.core.db.ServerPlugin;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import simple.marauroa.application.core.db.Application;
import simple.marauroa.application.core.db.ApplicationPK;
import simple.marauroa.application.core.db.controller.exceptions.NonexistentEntityException;
import simple.marauroa.application.core.db.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class ApplicationJpaController implements Serializable {

    public ApplicationJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Application application) throws PreexistingEntityException, Exception {
        if (application.getApplicationPK() == null) {
            application.setApplicationPK(new ApplicationPK());
        }
        if (application.getServerPluginList() == null) {
            application.setServerPluginList(new ArrayList<ServerPlugin>());
        }
        application.getApplicationPK().setApplicationType(application.getApplicationType().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ApplicationType applicationType = application.getApplicationType();
            if (applicationType != null) {
                applicationType = em.getReference(applicationType.getClass(), applicationType.getId());
                application.setApplicationType(applicationType);
            }
            List<ServerPlugin> attachedServerPluginList = new ArrayList<ServerPlugin>();
            for (ServerPlugin serverPluginListServerPluginToAttach : application.getServerPluginList()) {
                serverPluginListServerPluginToAttach = em.getReference(serverPluginListServerPluginToAttach.getClass(), serverPluginListServerPluginToAttach.getId());
                attachedServerPluginList.add(serverPluginListServerPluginToAttach);
            }
            application.setServerPluginList(attachedServerPluginList);
            em.persist(application);
            if (applicationType != null) {
                applicationType.getApplicationList().add(application);
                applicationType = em.merge(applicationType);
            }
            for (ServerPlugin serverPluginListServerPlugin : application.getServerPluginList()) {
                serverPluginListServerPlugin.getApplicationList().add(application);
                serverPluginListServerPlugin = em.merge(serverPluginListServerPlugin);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findApplication(application.getApplicationPK()) != null) {
                throw new PreexistingEntityException("Application " + application + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Application application) throws NonexistentEntityException, Exception {
        application.getApplicationPK().setApplicationType(application.getApplicationType().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Application persistentApplication = em.find(Application.class, application.getApplicationPK());
            ApplicationType applicationTypeOld = persistentApplication.getApplicationType();
            ApplicationType applicationTypeNew = application.getApplicationType();
            List<ServerPlugin> serverPluginListOld = persistentApplication.getServerPluginList();
            List<ServerPlugin> serverPluginListNew = application.getServerPluginList();
            if (applicationTypeNew != null) {
                applicationTypeNew = em.getReference(applicationTypeNew.getClass(), applicationTypeNew.getId());
                application.setApplicationType(applicationTypeNew);
            }
            List<ServerPlugin> attachedServerPluginListNew = new ArrayList<ServerPlugin>();
            for (ServerPlugin serverPluginListNewServerPluginToAttach : serverPluginListNew) {
                serverPluginListNewServerPluginToAttach = em.getReference(serverPluginListNewServerPluginToAttach.getClass(), serverPluginListNewServerPluginToAttach.getId());
                attachedServerPluginListNew.add(serverPluginListNewServerPluginToAttach);
            }
            serverPluginListNew = attachedServerPluginListNew;
            application.setServerPluginList(serverPluginListNew);
            application = em.merge(application);
            if (applicationTypeOld != null && !applicationTypeOld.equals(applicationTypeNew)) {
                applicationTypeOld.getApplicationList().remove(application);
                applicationTypeOld = em.merge(applicationTypeOld);
            }
            if (applicationTypeNew != null && !applicationTypeNew.equals(applicationTypeOld)) {
                applicationTypeNew.getApplicationList().add(application);
                applicationTypeNew = em.merge(applicationTypeNew);
            }
            for (ServerPlugin serverPluginListOldServerPlugin : serverPluginListOld) {
                if (!serverPluginListNew.contains(serverPluginListOldServerPlugin)) {
                    serverPluginListOldServerPlugin.getApplicationList().remove(application);
                    serverPluginListOldServerPlugin = em.merge(serverPluginListOldServerPlugin);
                }
            }
            for (ServerPlugin serverPluginListNewServerPlugin : serverPluginListNew) {
                if (!serverPluginListOld.contains(serverPluginListNewServerPlugin)) {
                    serverPluginListNewServerPlugin.getApplicationList().add(application);
                    serverPluginListNewServerPlugin = em.merge(serverPluginListNewServerPlugin);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ApplicationPK id = application.getApplicationPK();
                if (findApplication(id) == null) {
                    throw new NonexistentEntityException("The application with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ApplicationPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Application application;
            try {
                application = em.getReference(Application.class, id);
                application.getApplicationPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The application with id " + id + " no longer exists.", enfe);
            }
            ApplicationType applicationType = application.getApplicationType();
            if (applicationType != null) {
                applicationType.getApplicationList().remove(application);
                applicationType = em.merge(applicationType);
            }
            List<ServerPlugin> serverPluginList = application.getServerPluginList();
            for (ServerPlugin serverPluginListServerPlugin : serverPluginList) {
                serverPluginListServerPlugin.getApplicationList().remove(application);
                serverPluginListServerPlugin = em.merge(serverPluginListServerPlugin);
            }
            em.remove(application);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Application> findApplicationEntities() {
        return findApplicationEntities(true, -1, -1);
    }

    public List<Application> findApplicationEntities(int maxResults, int firstResult) {
        return findApplicationEntities(false, maxResults, firstResult);
    }

    private List<Application> findApplicationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Application.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Application findApplication(ApplicationPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Application.class, id);
        } finally {
            em.close();
        }
    }

    public int getApplicationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Application> rt = cq.from(Application.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
