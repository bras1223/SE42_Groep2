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
    }

    @Override
    public int count() {

            Query q = em.createNamedQuery("User.count", User.class);
            return ((Long) q.getSingleResult()).intValue();
        

    }

    @Override
    public void create(User user) {
        em.getTransaction().begin();

        if (findByEmail(user.getEmail()) != null) {
            throw new EntityExistsException();
        }

            em.persist(user);
            em.getTransaction().commit();
        
    }

    @Override
    public void edit(User user) {
        if (findByEmail(user.getEmail()) == null) {
            throw new IllegalArgumentException();
        }

            em.merge(user);
        
    }

    @Override
    public List<User> findAll() {

            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
            return em.createQuery(cq).getResultList();
       
    }

    @Override
    public User findByEmail(String email) {

            Query q = em.createNamedQuery("User.findByEmail", User.class);
            q.setParameter("email", email);
            return ((User) q.getSingleResult());
        
    }

    @Override
    public void remove(User user) {
        em.getTransaction().begin();

            em.remove(em.merge(user));
            em.getTransaction().commit();
        
    }
}
