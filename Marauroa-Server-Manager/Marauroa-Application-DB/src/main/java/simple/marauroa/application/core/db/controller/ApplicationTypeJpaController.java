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
import simple.marauroa.application.core.db.ApplicationType;
import simple.marauroa.application.core.db.controller.exceptions.IllegalOrphanException;
import simple.marauroa.application.core.db.controller.exceptions.NonexistentEntityException;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class ApplicationTypeJpaController implements Serializable {

    public ApplicationTypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ApplicationType applicationType) {
        if (applicationType.getApplicationList() == null) {
            applicationType.setApplicationList(new ArrayList<>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Application> attachedApplicationList = new ArrayList<>();
            for (Application applicationListApplicationToAttach : applicationType.getApplicationList()) {
                applicationListApplicationToAttach = em.getReference(applicationListApplicationToAttach.getClass(), applicationListApplicationToAttach.getApplicationPK());
                attachedApplicationList.add(applicationListApplicationToAttach);
            }
            applicationType.setApplicationList(attachedApplicationList);
            em.persist(applicationType);
            for (Application applicationListApplication : applicationType.getApplicationList()) {
                ApplicationType oldApplicationTypeOfApplicationListApplication = applicationListApplication.getApplicationType();
                applicationListApplication.setApplicationType(applicationType);
                applicationListApplication = em.merge(applicationListApplication);
                if (oldApplicationTypeOfApplicationListApplication != null) {
                    oldApplicationTypeOfApplicationListApplication.getApplicationList().remove(applicationListApplication);
                    oldApplicationTypeOfApplicationListApplication = em.merge(oldApplicationTypeOfApplicationListApplication);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ApplicationType applicationType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ApplicationType persistentApplicationType = em.find(ApplicationType.class, applicationType.getId());
            List<Application> applicationListOld = persistentApplicationType.getApplicationList();
            List<Application> applicationListNew = applicationType.getApplicationList();
            List<String> illegalOrphanMessages = null;
            for (Application applicationListOldApplication : applicationListOld) {
                if (!applicationListNew.contains(applicationListOldApplication)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<>();
                    }
                    illegalOrphanMessages.add("You must retain Application " + applicationListOldApplication + " since its applicationType field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Application> attachedApplicationListNew = new ArrayList<>();
            for (Application applicationListNewApplicationToAttach : applicationListNew) {
                applicationListNewApplicationToAttach = em.getReference(applicationListNewApplicationToAttach.getClass(), applicationListNewApplicationToAttach.getApplicationPK());
                attachedApplicationListNew.add(applicationListNewApplicationToAttach);
            }
            applicationListNew = attachedApplicationListNew;
            applicationType.setApplicationList(applicationListNew);
            applicationType = em.merge(applicationType);
            for (Application applicationListNewApplication : applicationListNew) {
                if (!applicationListOld.contains(applicationListNewApplication)) {
                    ApplicationType oldApplicationTypeOfApplicationListNewApplication = applicationListNewApplication.getApplicationType();
                    applicationListNewApplication.setApplicationType(applicationType);
                    applicationListNewApplication = em.merge(applicationListNewApplication);
                    if (oldApplicationTypeOfApplicationListNewApplication != null && !oldApplicationTypeOfApplicationListNewApplication.equals(applicationType)) {
                        oldApplicationTypeOfApplicationListNewApplication.getApplicationList().remove(applicationListNewApplication);
                        oldApplicationTypeOfApplicationListNewApplication = em.merge(oldApplicationTypeOfApplicationListNewApplication);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (IllegalOrphanException ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = applicationType.getId();
                if (findApplicationType(id) == null) {
                    throw new NonexistentEntityException("The applicationType with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ApplicationType applicationType;
            try {
                applicationType = em.getReference(ApplicationType.class, id);
                applicationType.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The applicationType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Application> applicationListOrphanCheck = applicationType.getApplicationList();
            for (Application applicationListOrphanCheckApplication : applicationListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<>();
                }
                illegalOrphanMessages.add("This ApplicationType (" + applicationType + ") cannot be destroyed since the Application " + applicationListOrphanCheckApplication + " in its applicationList field has a non-nullable applicationType field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(applicationType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ApplicationType> findApplicationTypeEntities() {
        return findApplicationTypeEntities(true, -1, -1);
    }

    public List<ApplicationType> findApplicationTypeEntities(int maxResults, int firstResult) {
        return findApplicationTypeEntities(false, maxResults, firstResult);
    }

    private List<ApplicationType> findApplicationTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ApplicationType.class));
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

    public ApplicationType findApplicationType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ApplicationType.class, id);
        } finally {
            em.close();
        }
    }

    public int getApplicationTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ApplicationType> rt = cq.from(ApplicationType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
