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
import java.util.ArrayList;
import java.util.Collection;
import simple.marauroa.application.core.db.ApplicationType;
import simple.marauroa.application.core.db.controller.exceptions.IllegalOrphanException;
import simple.marauroa.application.core.db.controller.exceptions.NonexistentEntityException;
import simple.marauroa.application.core.db.controller.exceptions.PreexistingEntityException;

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

    public void create(ApplicationType applicationType) throws PreexistingEntityException, Exception {
        if (applicationType.getApplicationCollection() == null) {
            applicationType.setApplicationCollection(new ArrayList<Application>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Application> attachedApplicationCollection = new ArrayList<Application>();
            for (Application applicationCollectionApplicationToAttach : applicationType.getApplicationCollection()) {
                applicationCollectionApplicationToAttach = em.getReference(applicationCollectionApplicationToAttach.getClass(), applicationCollectionApplicationToAttach.getApplicationPK());
                attachedApplicationCollection.add(applicationCollectionApplicationToAttach);
            }
            applicationType.setApplicationCollection(attachedApplicationCollection);
            em.persist(applicationType);
            for (Application applicationCollectionApplication : applicationType.getApplicationCollection()) {
                ApplicationType oldApplicationType1OfApplicationCollectionApplication = applicationCollectionApplication.getApplicationType1();
                applicationCollectionApplication.setApplicationType1(applicationType);
                applicationCollectionApplication = em.merge(applicationCollectionApplication);
                if (oldApplicationType1OfApplicationCollectionApplication != null) {
                    oldApplicationType1OfApplicationCollectionApplication.getApplicationCollection().remove(applicationCollectionApplication);
                    oldApplicationType1OfApplicationCollectionApplication = em.merge(oldApplicationType1OfApplicationCollectionApplication);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findApplicationType(applicationType.getId()) != null) {
                throw new PreexistingEntityException("ApplicationType " + applicationType + " already exists.", ex);
            }
            throw ex;
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
            Collection<Application> applicationCollectionOld = persistentApplicationType.getApplicationCollection();
            Collection<Application> applicationCollectionNew = applicationType.getApplicationCollection();
            List<String> illegalOrphanMessages = null;
            for (Application applicationCollectionOldApplication : applicationCollectionOld) {
                if (!applicationCollectionNew.contains(applicationCollectionOldApplication)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Application " + applicationCollectionOldApplication + " since its applicationType1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Application> attachedApplicationCollectionNew = new ArrayList<Application>();
            for (Application applicationCollectionNewApplicationToAttach : applicationCollectionNew) {
                applicationCollectionNewApplicationToAttach = em.getReference(applicationCollectionNewApplicationToAttach.getClass(), applicationCollectionNewApplicationToAttach.getApplicationPK());
                attachedApplicationCollectionNew.add(applicationCollectionNewApplicationToAttach);
            }
            applicationCollectionNew = attachedApplicationCollectionNew;
            applicationType.setApplicationCollection(applicationCollectionNew);
            applicationType = em.merge(applicationType);
            for (Application applicationCollectionNewApplication : applicationCollectionNew) {
                if (!applicationCollectionOld.contains(applicationCollectionNewApplication)) {
                    ApplicationType oldApplicationType1OfApplicationCollectionNewApplication = applicationCollectionNewApplication.getApplicationType1();
                    applicationCollectionNewApplication.setApplicationType1(applicationType);
                    applicationCollectionNewApplication = em.merge(applicationCollectionNewApplication);
                    if (oldApplicationType1OfApplicationCollectionNewApplication != null && !oldApplicationType1OfApplicationCollectionNewApplication.equals(applicationType)) {
                        oldApplicationType1OfApplicationCollectionNewApplication.getApplicationCollection().remove(applicationCollectionNewApplication);
                        oldApplicationType1OfApplicationCollectionNewApplication = em.merge(oldApplicationType1OfApplicationCollectionNewApplication);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
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
            Collection<Application> applicationCollectionOrphanCheck = applicationType.getApplicationCollection();
            for (Application applicationCollectionOrphanCheckApplication : applicationCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ApplicationType (" + applicationType + ") cannot be destroyed since the Application " + applicationCollectionOrphanCheckApplication + " in its applicationCollection field has a non-nullable applicationType1 field.");
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
