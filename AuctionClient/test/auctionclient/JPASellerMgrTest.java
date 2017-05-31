package auctionclient;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

public class JPASellerMgrTest {

    private AuctionMethods auctionMgr;
    private RegistrationMethods registrationMgr;
    private AuctionMethods sellerMgr;
    
  
    
    @Before
    public void setUp() throws Exception {
       cleanDB();
    }

    /**
     * Test of offerItem method, of class SellerMgr.
     */
    @Test
    public void testOfferItem() {
        String omsch = "omsch";

        User user1 = registrationMgr.registerUser("xx@nl");
        Category cat = new Category();
        cat.setDescription("cat1");
        Item item1 = sellerMgr.offerItem(user1, cat, omsch);
        assertEquals(omsch, item1.getDescription());
        assertNotNull(item1.getId());
    }

    /**
     * Test of revokeItem method, of class SellerMgr.
     */
    @Test
    public void testRevokeItem() {
        String omsch = "omsch";
        String omsch2 = "omsch2";
        
    
        User seller = registrationMgr.registerUser("sel@nl");
        User buyer = registrationMgr.registerUser("buy@nl");
        Category cat = new Category();
        cat.setDescription("cat1");
        
            // revoke before bidding
        Item item1 = sellerMgr.offerItem(seller, cat, omsch);
        boolean res = sellerMgr.revokeItem(item1);
        assertTrue(res);
        int count = auctionMgr.findItemByDescription(omsch).size();
        assertEquals(0, count);
        
            // revoke after bid has been made
        Item item2 = sellerMgr.offerItem(seller, cat, omsch2);
        Money money = new Money();
        money.cents = 100;
        money.currency = "Euro";
        auctionMgr.newBid(item2, buyer, money);
        boolean res2 = sellerMgr.revokeItem(item2);
        assertFalse(res2);
        int count2 = auctionMgr.findItemByDescription(omsch2).size();
        assertEquals(1, count2);
    }

    private static void cleanDB() {
        auctionclient.AuctionService service = new auctionclient.AuctionService();
        auctionclient.Auction port = service.getAuctionPort();
        port.cleanDB();
    }

}
