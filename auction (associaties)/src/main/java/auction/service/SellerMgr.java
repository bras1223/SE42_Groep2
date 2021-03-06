package auction.service;

import auction.dao.ItemDAO;
import auction.dao.ItemDAOJPAImpl;
import auction.domain.Category;
import auction.domain.Furniture;
import auction.domain.Item;
import auction.domain.Painting;
import auction.domain.User;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class SellerMgr {

   private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("auctionPU");
   private ItemDAO itemDAO;
   
   public SellerMgr() {
       itemDAO = new ItemDAOJPAImpl(emf.createEntityManager());
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
        itemDAO.create(item);
        return item;
    }
    
     /**
     * @param item
     * @return true als er nog niet geboden is op het item. Het item word verwijderd.
     *         false als er al geboden was op het item.
     */
    public boolean revokeItem(Item item) {
        Item foundItem = itemDAO.find(item.getId());
        if(foundItem.getHighestBid() == null) {
            itemDAO.remove(foundItem);
            System.out.println("true");
            return true;
        } 
        return false;
    }
    
    public Furniture offerFurniture(User seller, Category category, String description, String material) {
        Furniture furniture = new Furniture(material, seller, category, description);
        itemDAO.create(furniture);
        return furniture;
    }
    
    public Painting offerPainting(User seller, Category category, String description, String title, String painter) {
        Painting painting = new Painting(title, painter, seller, category, description);
        itemDAO.create(painting);
        
        return painting;
    }
}
