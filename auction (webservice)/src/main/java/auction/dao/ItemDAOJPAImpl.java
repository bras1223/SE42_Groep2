/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auction.dao;

import auction.domain.Item;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

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
        if (find(item.getId()) != null) {
            return;
        }
        em.persist(item);
    }

    @Override
    public void edit(Item item) {
        if (find(item.getId()) == null) {
            return;
        }
        em.merge(item);
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
        Query query = em.createNamedQuery("Item.getAll", Item.class);

        return query.getResultList();
    }

    @Override
    public List<Item> findByDescription(String description) {
        Query query = em.createNamedQuery("Item.findByDescription", Item.class);
        query.setParameter("description", description);
        List<Item> items = query.getResultList();
        return items;
    }

    @Override
    public void remove(Item item) {
        em.remove(item);
    }

}
