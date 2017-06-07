package auctionclient;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JPAAuctionMgrTest {

    Registration registrationService;
    Auction auctionService;
    
    @Before
    public void setUp() throws Exception {
        registrationService = new RegistrationService().getRegistrationPort();
        auctionService = new AuctionService().getAuctionPort();
        auctionService.cleanDB();
    }

    @Test
    public void getItem() {

        String email = "xx2@nl";
        String omsch = "omsch";

        User seller1 = registrationService.registerUser(email);
        Category cat = new Category();
        cat.setDescription("cat2");
        Item item1 = auctionService.offerItem(seller1, cat, omsch);
        Item item2 = auctionService.getItem(item1.getId());
        assertEquals(omsch, item2.getDescription());
        assertEquals(email, item2.getSeller().getEmail());
    }

    @Test
    public void findItemByDescription() {
        String email3 = "xx3@nl";
        String omsch = "omsch";
        String email4 = "xx4@nl";
        String omsch2 = "omsch2";

        User seller3 = registrationService.registerUser(email3);
        User seller4 = registrationService.registerUser(email4);
        Category cat = new Category();
        cat.setDescription("cat3");
        Item item1 = auctionService.offerItem(seller3, cat, omsch);
        Item item2 = auctionService.offerItem(seller4, cat, omsch);

        List<Item> res = auctionService.findItemByDescription(omsch2);
        assertEquals(0, res.size());

        res = auctionService.findItemByDescription(omsch);
        assertEquals(2, res.size());

    }

    @Test
    public void newBid() {

        String email = "ss2@nl";
        String emailb = "bb@nl";
        String emailb2 = "bb2@nl";
        String omsch = "omsch_bb";

        User seller = registrationService.registerUser(email);
        User buyer = registrationService.registerUser(emailb);
        User buyer2 = registrationService.registerUser(emailb2);
        // eerste bod
        Category cat = new Category();
        cat.setDescription("cat9");
        Item item1 = auctionService.offerItem(seller, cat, omsch);
        Money money = new Money();
        money.cents = 10;
        money.currency = "eur";
        Bid new1 = auctionService.newBid(item1, buyer, money);
        assertEquals(emailb, new1.getBuyer().getEmail());

        money.cents = 9;
        // lager bod
        Bid new2 = auctionService.newBid(item1, buyer2, money);
        assertNull(new2);

        money.cents = 11;
        // hoger bod
        Bid new3 = auctionService.newBid(item1, buyer2, money);
        assertEquals(emailb2, new3.getBuyer().getEmail());
    }
}
