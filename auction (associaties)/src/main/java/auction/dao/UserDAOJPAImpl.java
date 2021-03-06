package auction.dao;

import auction.domain.User;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

public class UserDAOJPAImpl implements UserDAO {

    private EntityManager em;

    public UserDAOJPAImpl(EntityManager em) {
        this.em = em;
        em.getTransaction().begin();
    }

    @Override
    public int count() {
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }

            Query q = em.createNamedQuery("User.count", User.class);
            em.getTransaction().commit();
            return ((Long) q.getSingleResult()).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return -1;
        }

    }

    @Override
    public void create(User user) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }

        if (findByEmail(user.getEmail()) != null) {
            throw new EntityExistsException();
        }

        try {
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    @Override
    public void edit(User user) {
        if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
        if (findByEmail(user.getEmail()) == null) {
            throw new IllegalArgumentException();
        }

        if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
        try {
            
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    @Override
    public List<User> findAll() {
        if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return null;
        }
    }

    @Override
    public User findByEmail(String email) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        try {
            Query q = em.createNamedQuery("User.findByEmail", User.class);
            q.setParameter("email", email);
            return ((User) q.getSingleResult());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void remove(User user) {
        if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
        try {
            em.remove(em.merge(user));
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }
}
