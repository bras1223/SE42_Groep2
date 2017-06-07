package auctionclient;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

public class JPASellerMgrTest {

    private Auction auctionService;
    private Registration registrationService;
    
  
    
    @Before
    public void setUp() throws Exception {
        registrationService = new RegistrationService().getRegistrationPort();
        auctionService = new AuctionService().getAuctionPort();
        auctionService.cleanDB();
    }

    /**
     * Test of offerItem method, of class SellerMgr.
     */
    @Test
    public void testOfferItem() {
        String omsch = "omsch";

        User user1 = registrationService.registerUser("xx@nl");
        Category cat = new Category();
        cat.setDescription("cat1");
        Item item1 = auctionService.offerItem(user1, cat, omsch);
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
        
    
        User seller = registrationService.registerUser("sel@nl");
        User buyer = registrationService.registerUser("buy@nl");
        Category cat = new Category();
        cat.setDescription("cat1");
        
            // revoke before bidding
        Item item1 = auctionService.offerItem(seller, cat, omsch);
        boolean res = auctionService.revokeItem(item1);
        assertTrue(res);
        int count = auctionService.findItemByDescription(omsch).size();
        assertEquals(0, count);
        
            // revoke after bid has been made
        Item item2 = auctionService.offerItem(seller, cat, omsch2);
        Money money = new Money();
        money.cents = 100;
        money.currency = "Euro";
        auctionService.newBid(item2, buyer, money);
        boolean res2 = auctionService.revokeItem(item2);
        assertFalse(res2);
        int count2 = auctionService.findItemByDescription(omsch2).size();
        assertEquals(1, count2);
    }
}
