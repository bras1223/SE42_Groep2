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
        em.getTransaction().begin();
    }
    
    @Override
    public int count() {
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }

            Query q = em.createNamedQuery("Item.count", Item.class);
            em.getTransaction().commit();
            return ((Long) q.getSingleResult()).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return -1;
        }
    }

    @Override
    public void create(Item item) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }

        if (find(item.getId()) != null) {
            throw new EntityExistsException();
        }

        try {
            em.persist(item);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    @Override
    public void edit(Item item) {
        if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
        if (find(item.getId()) == null) {
            throw new IllegalArgumentException();
        }
        try {
            
            em.merge(item);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    @Override
    public Item find(Long id) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        try {
            return em.find(Item.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Item> findAll() {
        if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Item.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return null;
        }
    }

    @Override
    public List<Item> findByDescription(String description) {
         if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        try {
            Query q = em.createNamedQuery("Item.findByDescription", Item.class);
            q.setParameter("description", description);
            return (List<Item>) q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void remove(Item item) {
        if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
        try {
            em.remove(em.merge(item));
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }
    
}
