package auction.service;

import auction.dao.ItemDAO;
import auction.dao.ItemDAOJPAImpl;
import auction.dao.UserDAO;
import auction.dao.UserDAOJPAImpl;
import auction.domain.Category;
import auction.domain.Furniture;
import auction.domain.Item;
import auction.domain.Painting;
import auction.domain.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class SellerMgr {
   private ItemDAO itemDAO;
   private EntityManager em;
   
   public SellerMgr(EntityManager em) {
       itemDAO = new ItemDAOJPAImpl(em);
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
        Item item = new Item(seller, cat, description);  
        em.getTransaction().begin();
        itemDAO.create(item);
        em.getTransaction().commit();
        return item;
    }
    
    
    public Furniture offerFurniture(User seller, Category cat, String description, String material) {
        Furniture furniture = new Furniture(material, seller, cat, description);
        em.getTransaction().begin();
        itemDAO.create(furniture);
        em.getTransaction().commit();
        return furniture;
    }
    
    public Painting offerPainting(User seller, Category cat, String description, String title, String painter) {
        Painting painting = new Painting(title, painter, seller, cat, description);
        em.getTransaction().begin();
        itemDAO.create(painting);
        em.getTransaction().commit();
        return painting;
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
