package auction.service;

import auction.dao.ItemDAO;
import auction.dao.ItemDAOJPAImpl;
import auction.dao.UserDAO;
import auction.dao.UserDAOJPAImpl;
import auction.domain.Category;
import auction.domain.Item;
import auction.domain.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class SellerMgr {
   private ItemDAO itemDAO;
   private UserDAO userDAO;
   private EntityManager em;
   
   public SellerMgr(EntityManager em) {
       itemDAO = new ItemDAOJPAImpl(em);
       userDAO = new UserDAOJPAImpl(em);
       this.em = em;
   }
    /**
     * @param seller
     * @param cat
     * @param description
     * @return het item aangeboden door seller, behorende tot de categorie cat
     *         en met de beschrijving description
     */
    public Item offerItem(User seller, Category cat, String description) {
        User sel = userDAO.findByEmail(seller.getEmail());
        Item item = new Item(seller, cat, description);  
        em.getTransaction().begin();
        itemDAO.create(item);
        em.getTransaction().commit();
        return item;
    }
     
     /**
     * @param item
     * @return true als er nog niet geboden is op het item. Het item word verwijderd.
     *         false als er al geboden was op het item.
     */
    public boolean revokeItem(Item item) {
        em.getTransaction().begin();
        Item foundItem = itemDAO.find(item.getId());
        if(foundItem.getHighestBid() == null) {
            itemDAO.remove(foundItem);
            em.getTransaction().commit();
            return true;
        } 
        em.getTransaction().commit();
        return false;
    }
}
