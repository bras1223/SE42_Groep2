package auction.dao;

import auction.domain.User;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

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
        Query query = em.createNamedQuery("User.getAll", User.class);

        return query.getResultList();   
    }

    @Override
    public User findByEmail(String email) {
        Query query = em.createNamedQuery("User.findByEmail", User.class);
        query.setParameter("email", email);
        List<User> users = query.getResultList();

        return users.size() == 1 ? users.get(0) : null;
    }

    @Override
    public void remove(User user) {
        em.remove(user);
        em.getTransaction().begin();
        em.getTransaction().commit();      
    }
}
