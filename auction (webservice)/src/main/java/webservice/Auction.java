package webservice;


import auction.domain.Bid;
import auction.domain.Category;
import auction.domain.Furniture;
import auction.domain.Item;
import auction.domain.Painting;
import auction.domain.User;
import auction.service.AuctionMgr;
import auction.service.RegistrationMgr;
import auction.service.SellerMgr;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import nl.fontys.util.Money;
import util.DatabaseCleaner;

/**
 *
 * @author Luuk
 */
@WebService
public class Auction {
    
    private AuctionMgr auctionMgr;
    private SellerMgr sellerMgr;
    private EntityManager em;
    
    public Auction(EntityManager em) {
        this.em = em;
        auctionMgr = new AuctionMgr(em);
        sellerMgr = new SellerMgr(em);
    }
    
    public Item getItem(Long id) {
        return auctionMgr.getItem(id);
    }
    
    public List<Item> findItemByDescription(String description) {
        return auctionMgr.findItemByDescription(description);
    }
    
    public Bid newBid(Item item, User buyer, Money amount) {
        return auctionMgr.newBid(item, buyer, amount);
    }
    
    public Item offerItem(User seller, Category cat, String description) {
        return sellerMgr.offerItem(seller, cat, description);
    }
    
    public Furniture offerFurniture(User seller, Category cat, String description, String material) {
        return sellerMgr.offerFurniture(seller, cat, description, material);
    }
    
    public Painting offerPainting(User seller, Category cat, String description, String title, String painter) {
        return offerPainting(seller, cat, description, title, painter);
    }
    
    public boolean revokeItem(Item item) {
        return sellerMgr.revokeItem(item);
    }
    
    public void cleanDB() {
        DatabaseCleaner dbcln = new DatabaseCleaner(em);
        try {
            dbcln.clean();
        } catch (SQLException ex) {
            Logger.getLogger(Auction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
