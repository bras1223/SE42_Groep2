package auctionclient2;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import webservice.Auction;
import webservice.AuctionService;
import webservice.Bid;
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
public class JPAAuctionMgrTest {

    private Registration registration;
    private Auction auction;

    @Before
    public void setUp() {
        registration = new RegistrationService().getRegistrationPort();
        auction = new AuctionService().getAuctionPort();
        auction.cleanDB();
    }
    
    @Test
    public void getItem() {
        String email = "xx2@nl";
        String omsch = "omsch";

        User seller1 = registration.registerUser(email);
        Category cat = new Category();
        cat.setDescription("cat2");
        Item item1 = auction.offerItem(seller1, cat, omsch);
        Item item2 = auction.findItemById(item1.getId());
        assertEquals(omsch, item2.getDescription());
        assertEquals(email, item2.getSeller().getEmail());
    }

    @Test
    public void findItemByDescription() {
        String email3 = "xx3@nl";
        String omsch = "omsch";
        String email4 = "xx4@nl";
        String omsch2 = "omsch2";

        User seller3 = registration.registerUser(email3);
        User seller4 = registration.registerUser(email4);
        Category cat = new Category();
        cat.setDescription("cat3");
        Item item1 = auction.offerItem(seller3, cat, omsch);
        Item item2 = auction.offerItem(seller4, cat, omsch);

        List<Item> res = auction.findItemByDescription(omsch2); // Cast naar ArrayList weggehaald omdat een een JPQL-query een lijst teruggeeft van het List-implementatietype Vector.
        assertEquals(0, res.size());

        res = auction.findItemByDescription(omsch);
        assertEquals(2, res.size());
    }

    @Test
    public void newBid() {
        String email = "ss2@nl";
        String emailb = "bb@nl";
        String emailb2 = "bb2@nl";
        String omsch = "omsch_bb";

        User seller = registration.registerUser(email);
        User buyer = registration.registerUser(emailb);
        User buyer2 = registration.registerUser(emailb2);
        // eerste bod
        Category cat = new Category();
        cat.setDescription("cat9");
        Item item1 = auction.offerItem(seller, cat, omsch);
        Money money = new Money();
        money.setCents(10);
        money.setCurrency("eur");
        Bid new1 = auction.newBid(item1, buyer, money);
        item1 = auction.findItemById(item1.getId()); // de state van item moet worden bijgewerkt omdat op het highest bid op de server inmiddels is bijgewerkt
        assertEquals(emailb, new1.getBuyer().getEmail());

        // lager bod
        money = new Money();
        money.setCents(9);
        money.setCurrency("eur");
        Bid new2 = auction.newBid(item1, buyer2, money);
        item1 = auction.findItemById(item1.getId()); // de state van item moet worden bijgewerkt omdat op het highest bid op de server inmiddels is bijgewerkt
        assertNull(new2);

        // hoger bod
        money = new Money();
        money.setCents(11);
        money.setCurrency("eur");
        Bid new3 = auction.newBid(item1, buyer2, money);
        assertEquals(emailb2, new3.getBuyer().getEmail());
    }

}
