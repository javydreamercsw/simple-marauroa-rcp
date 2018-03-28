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
import simple.marauroa.application.core.db.Application;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import simple.marauroa.application.core.db.ServerPlugin;
import simple.marauroa.application.core.db.controller.exceptions.NonexistentEntityException;
import simple.marauroa.application.core.db.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class ServerPluginJpaController implements Serializable {

    public ServerPluginJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ServerPlugin serverPlugin) throws PreexistingEntityException, Exception {
        if (serverPlugin.getApplicationList() == null) {
            serverPlugin.setApplicationList(new ArrayList<>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Application> attachedApplicationList = new ArrayList<>();
            for (Application applicationListApplicationToAttach : serverPlugin.getApplicationList()) {
                applicationListApplicationToAttach = em.getReference(applicationListApplicationToAttach.getClass(), applicationListApplicationToAttach.getApplicationPK());
                attachedApplicationList.add(applicationListApplicationToAttach);
            }
            serverPlugin.setApplicationList(attachedApplicationList);
            em.persist(serverPlugin);
            for (Application applicationListApplication : serverPlugin.getApplicationList()) {
                applicationListApplication.getServerPluginList().add(serverPlugin);
                applicationListApplication = em.merge(applicationListApplication);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findServerPlugin(serverPlugin.getId()) != null) {
                throw new PreexistingEntityException("ServerPlugin " + serverPlugin + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ServerPlugin serverPlugin) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ServerPlugin persistentServerPlugin = em.find(ServerPlugin.class, serverPlugin.getId());
            List<Application> applicationListOld = persistentServerPlugin.getApplicationList();
            List<Application> applicationListNew = serverPlugin.getApplicationList();
            List<Application> attachedApplicationListNew = new ArrayList<>();
            for (Application applicationListNewApplicationToAttach : applicationListNew) {
                applicationListNewApplicationToAttach = em.getReference(applicationListNewApplicationToAttach.getClass(), applicationListNewApplicationToAttach.getApplicationPK());
                attachedApplicationListNew.add(applicationListNewApplicationToAttach);
            }
            applicationListNew = attachedApplicationListNew;
            serverPlugin.setApplicationList(applicationListNew);
            serverPlugin = em.merge(serverPlugin);
            for (Application applicationListOldApplication : applicationListOld) {
                if (!applicationListNew.contains(applicationListOldApplication)) {
                    applicationListOldApplication.getServerPluginList().remove(serverPlugin);
                    applicationListOldApplication = em.merge(applicationListOldApplication);
                }
            }
            for (Application applicationListNewApplication : applicationListNew) {
                if (!applicationListOld.contains(applicationListNewApplication)) {
                    applicationListNewApplication.getServerPluginList().add(serverPlugin);
                    applicationListNewApplication = em.merge(applicationListNewApplication);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = serverPlugin.getId();
                if (findServerPlugin(id) == null) {
                    throw new NonexistentEntityException("The serverPlugin with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ServerPlugin serverPlugin;
            try {
                serverPlugin = em.getReference(ServerPlugin.class, id);
                serverPlugin.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The serverPlugin with id " + id + " no longer exists.", enfe);
            }
            List<Application> applicationList = serverPlugin.getApplicationList();
            for (Application applicationListApplication : applicationList) {
                applicationListApplication.getServerPluginList().remove(serverPlugin);
                applicationListApplication = em.merge(applicationListApplication);
            }
            em.remove(serverPlugin);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ServerPlugin> findServerPluginEntities() {
        return findServerPluginEntities(true, -1, -1);
    }

    public List<ServerPlugin> findServerPluginEntities(int maxResults, int firstResult) {
        return findServerPluginEntities(false, maxResults, firstResult);
    }

    private List<ServerPlugin> findServerPluginEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ServerPlugin.class));
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

    public ServerPlugin findServerPlugin(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ServerPlugin.class, id);
        } finally {
            em.close();
        }
    }

    public int getServerPluginCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ServerPlugin> rt = cq.from(ServerPlugin.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
