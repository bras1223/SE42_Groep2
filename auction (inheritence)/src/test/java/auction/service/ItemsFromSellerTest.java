package auction.service;

import org.junit.Ignore;
import javax.persistence.*;
import util.DatabaseCleaner;
import auction.domain.Category;
import auction.domain.Item;
import auction.domain.User;
import java.sql.SQLException;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ItemsFromSellerTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private AuctionMgr auctionMgr;
    private RegistrationMgr registrationMgr;
    private SellerMgr sellerMgr;

    public ItemsFromSellerTest() {
    }

    @Before
    public void setUp() throws Exception {
        emf = Persistence.createEntityManagerFactory("auctionPU");
        em = emf.createEntityManager();    
        
        DatabaseCleaner dbcl = new DatabaseCleaner(em);
        dbcl.clean();
        
        registrationMgr = new RegistrationMgr(em);
        auctionMgr = new AuctionMgr(em);
        sellerMgr = new SellerMgr(em);
    }

    @Test
 //   @Ignore
    public void numberOfOfferdItems() {

        String email = "ifu1@nl";
        String omsch1 = "omsch_ifu1";
        String omsch2 = "omsch_ifu2";

        User user1 = registrationMgr.registerUser(email);
        assertEquals(0, user1.numberOfOfferdItems());

        Category cat = new Category("cat2");
        Item item1 = sellerMgr.offerItem(user1, cat, omsch1);

       
        // test number of items belonging to user1
        //assertEquals(0, user1.numberOfOfferdItems());
        assertEquals(1, user1.numberOfOfferdItems());
        
        /*
         *  expected: First one was expected to fail; there was 1 item in the users list.
         *  QUESTION:
         *    Explain the result in terms of entity manager and persistance context.
        
         */
         
         
        assertEquals(1, item1.getSeller().numberOfOfferdItems());


        User user2 = registrationMgr.getUser(email);
        assertEquals(1, user2.numberOfOfferdItems());
        Item item2 = sellerMgr.offerItem(user2, cat, omsch2);
        assertEquals(2, user2.numberOfOfferdItems()); // Maar 1 item ingevoegd? 

        User user3 = registrationMgr.getUser(email);
        assertEquals(2, user3.numberOfOfferdItems()); // 2 naar 1

        User userWithItem = item2.getSeller();
        assertEquals(2, userWithItem.numberOfOfferdItems()); // 2 naar 1
        //assertEquals(3, userWithItem.numberOfOfferdItems()); De user heeft maar 2 items
        /*
         *  expected: which one of te above two assertions do you expect to be true?
         *  QUESTION:
         *    Explain the result in terms of entity manager and persistance context.
         */
        
        
//        assertNotSame(user3, userWithItem); / ze zijn niet hetzelfde object maar wel equals
        assertEquals(user3, userWithItem);

    }

    @Test
//    @Ignore
    public void getItemsFromSeller() {
        String email = "ifu1@nl";
        String omsch1 = "omsch_ifu1";
        String omsch2 = "omsch_ifu2";

        Category cat = new Category("cat2");

        User user10 = registrationMgr.registerUser(email);
        Item item10 = sellerMgr.offerItem(user10, cat, omsch1);
        Iterator<Item> it = user10.getOfferedItems();
        // testing number of items of java object
        assertTrue(it.hasNext());
        
        // now testing number of items for same user fetched from db.
        User user11 = registrationMgr.getUser(email);
        Iterator<Item> it11 = user11.getOfferedItems();
        assertTrue(it11.hasNext());
        it11.next();
        assertFalse(it11.hasNext());

        // Explain difference in above two tests for te iterator of 'same' user

        
        
        User user20 = registrationMgr.getUser(email);
        Item item20 = sellerMgr.offerItem(user20, cat, omsch2);
        Iterator<Item> it20 = user20.getOfferedItems();
        assertTrue(it20.hasNext());
        it20.next();
        assertTrue(it20.hasNext());


        User user30 = item20.getSeller();
        Iterator<Item> it30 = user30.getOfferedItems();
        assertTrue(it30.hasNext());
        it30.next();
        assertTrue(it30.hasNext());

    }
}
