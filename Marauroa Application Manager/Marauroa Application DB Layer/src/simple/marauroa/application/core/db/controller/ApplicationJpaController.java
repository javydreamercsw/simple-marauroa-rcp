/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simple.marauroa.application.core.db.controller;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import simple.marauroa.application.core.db.Application;
import simple.marauroa.application.core.db.ApplicationPK;
import simple.marauroa.application.core.db.ApplicationType;
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
        application.getApplicationPK().setApplicationType(application.getApplicationType1().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ApplicationType applicationType1 = application.getApplicationType1();
            if (applicationType1 != null) {
                applicationType1 = em.getReference(applicationType1.getClass(), applicationType1.getId());
                application.setApplicationType1(applicationType1);
            }
            em.persist(application);
            if (applicationType1 != null) {
                applicationType1.getApplicationCollection().add(application);
                applicationType1 = em.merge(applicationType1);
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
        application.getApplicationPK().setApplicationType(application.getApplicationType1().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Application persistentApplication = em.find(Application.class, application.getApplicationPK());
            ApplicationType applicationType1Old = persistentApplication.getApplicationType1();
            ApplicationType applicationType1New = application.getApplicationType1();
            if (applicationType1New != null) {
                applicationType1New = em.getReference(applicationType1New.getClass(), applicationType1New.getId());
                application.setApplicationType1(applicationType1New);
            }
            application = em.merge(application);
            if (applicationType1Old != null && !applicationType1Old.equals(applicationType1New)) {
                applicationType1Old.getApplicationCollection().remove(application);
                applicationType1Old = em.merge(applicationType1Old);
            }
            if (applicationType1New != null && !applicationType1New.equals(applicationType1Old)) {
                applicationType1New.getApplicationCollection().add(application);
                applicationType1New = em.merge(applicationType1New);
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
            ApplicationType applicationType1 = application.getApplicationType1();
            if (applicationType1 != null) {
                applicationType1.getApplicationCollection().remove(application);
                applicationType1 = em.merge(applicationType1);
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
