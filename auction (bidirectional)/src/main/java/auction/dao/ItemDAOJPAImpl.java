/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auction.dao;

import auction.domain.Item;
import auction.domain.User;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author Luuk
 */
public class ItemDAOJPAImpl implements ItemDAO {

    private EntityManager em;

    public ItemDAOJPAImpl(EntityManager em) {
        this.em = em;
    }
    
    @Override
    public int count() {
            Query q = em.createNamedQuery("Item.count", Item.class);
            em.getTransaction().commit();
            return ((Long) q.getSingleResult()).intValue();
      
    }

    @Override
    public void create(Item item) {
        em.getTransaction().begin();

        if (find(item.getId()) != null) {
            throw new EntityExistsException();
        }


            if (find(item.getId()) == null) {
            em.persist(item);
            em.getTransaction().commit();
            }
        
    }

    @Override
    public void edit(Item item) {
        em.getTransaction().begin();
        
        if (find(item.getId()) == null) {
            throw new IllegalArgumentException();
        }

        em.merge(item);
            em.getTransaction().commit();
        
    }

    @Override
    public Item find(Long id) {

                Query query = em.createNamedQuery("Item.findByID", Item.class);
                query.setParameter("id", id);
                List<Item> items = query.getResultList();
                
                if (items.size() != 1) {
                    return null;
                }
            return items.get(0);
       
    }

    @Override
    public List<Item> findAll() {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Item.class));
            return em.createQuery(cq).getResultList();
        
    }

    @Override
    public List<Item> findByDescription(String description) {
            Query q = em.createNamedQuery("Item.findByDescription", Item.class);
            q.setParameter("description", description);
            return (List<Item>) q.getResultList();
    }

    @Override
    public void remove(Item item) {
        em.getTransaction().begin();
            em.remove(em.merge(item));
            em.getTransaction().commit();
    }
    
}
