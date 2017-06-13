package auctionclient2;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import webservice.Auction;
import webservice.AuctionService;
import webservice.Category;
import webservice.Item;
import webservice.Money;
import webservice.Registration;
import webservice.RegistrationService;
import webservice.User;

/**
 *
 * @author Luuk
 */
public class JPASellerMgrTest {

    private Registration registration;
    private Auction auction;

    @Before
    public void setUp() {
        registration = new RegistrationService().getRegistrationPort();
        auction = new AuctionService().getAuctionPort();
        auction.cleanDB();
    }

    /**
     * Test of offerItem method, of class SellerMgr.
     */
    @Test
    public void testOfferItem() {
        String omsch = "omsch";

        User user1 = registration.registerUser("xx@nl");
        Category cat = new Category();
        cat.setDescription("cat1");
        Item item1 = auction.offerItem(user1, cat, omsch);
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

        User seller = registration.registerUser("sel@nl");
        User buyer = registration.registerUser("buy@nl");
        Category cat = new Category();
        cat.setDescription("cat1");

        // revoke before bidding
        Item item1 = auction.offerItem(seller, cat, omsch);
        boolean res = auction.revokeItem(item1);
        assertTrue(res);
        int count = auction.findItemByDescription(omsch).size();
        assertEquals(0, count);

        // revoke after bid has been made
        Item item2 = auction.offerItem(seller, cat, omsch2);
        Money money = new Money();
        money.setCents(100);
        money.setCurrency("Euro");
        auction.newBid(item2, buyer, money);
        item2 = auction.findItemById(item2.getId()); // de state van item moet worden bijgewerkt omdat op het highest bid op de server inmiddels is bijgewerkt
        boolean res2 = auction.revokeItem(item2);
        assertFalse(res2);
        int count2 = auction.findItemByDescription(omsch2).size();
        assertEquals(1, count2);
    }

}
