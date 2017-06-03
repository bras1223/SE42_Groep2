package auctionclient;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JPAAuctionMgrTest {

    RegistrationMethods registrationMgr;
    AuctionMethods sellerMgr;
    AuctionMethods auctionMgr;
    
    @Before
    public void setUp() throws Exception {
        AuctionMethods.cleanDB();
    }

    @Test
    public void getItem() {

        String email = "xx2@nl";
        String omsch = "omsch";

        User seller1 = RegistrationMethods.registerUser(email);
        Category cat = new Category();
        cat.setDescription("cat2");
        Item item1 = sellerMgr.offerItem(seller1, cat, omsch);
        Item item2 = auctionMgr.getItem(item1.getId());
        assertEquals(omsch, item2.getDescription());
        assertEquals(email, item2.getSeller().getEmail());
    }

    @Test
    public void findItemByDescription() {
        String email3 = "xx3@nl";
        String omsch = "omsch";
        String email4 = "xx4@nl";
        String omsch2 = "omsch2";

        User seller3 = registrationMgr.registerUser(email3);
        User seller4 = registrationMgr.registerUser(email4);
        Category cat = new Category();
        cat.setDescription("cat3");
        Item item1 = sellerMgr.offerItem(seller3, cat, omsch);
        Item item2 = sellerMgr.offerItem(seller4, cat, omsch);

        List<Item> res = auctionMgr.findItemByDescription(omsch2);
        assertEquals(0, res.size());

        res = auctionMgr.findItemByDescription(omsch);
        assertEquals(2, res.size());

    }

    @Test
    public void newBid() {

        String email = "ss2@nl";
        String emailb = "bb@nl";
        String emailb2 = "bb2@nl";
        String omsch = "omsch_bb";

        User seller = registrationMgr.registerUser(email);
        User buyer = registrationMgr.registerUser(emailb);
        User buyer2 = registrationMgr.registerUser(emailb2);
        // eerste bod
        Category cat = new Category();
        cat.setDescription("cat9");
        Item item1 = sellerMgr.offerItem(seller, cat, omsch);
        Money money = new Money();
        money.cents = 10;
        money.currency = "eur";
        Bid new1 = auctionMgr.newBid(item1, buyer, money);
        assertEquals(emailb, new1.getBuyer().getEmail());

        money.cents = 9;
        // lager bod
        Bid new2 = auctionMgr.newBid(item1, buyer2, money);
        assertNull(new2);

        money.cents = 11;
        // hoger bod
        Bid new3 = auctionMgr.newBid(item1, buyer2, money);
        assertEquals(emailb2, new3.getBuyer().getEmail());
    }
}
