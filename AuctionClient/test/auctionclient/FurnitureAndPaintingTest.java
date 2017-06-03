package auctionclient;

import java.util.Iterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class FurnitureAndPaintingTest {

    public FurnitureAndPaintingTest() {
    }

    @Before
    public void setUp() throws Exception {
        AuctionMethods.cleanDB();
    }

    
    @Test
    public void newFurniture() {
        String omsch = "omsch1";
        String iemand1 = "iemand1@def";
        User u1 = RegistrationMethods.registerUser(iemand1);
        User u2 = RegistrationMethods.registerUser("iemand2@def");
        Category cat = new Category();
        cat.setDescription("cat2");

        Item furniture1 = AuctionMethods.offerFurniture(u1, cat, "broodkast", "ijzer");
        assertEquals("seller of item correct", furniture1.getSeller(), u1);

        User foundUser = RegistrationMethods.getUser(iemand1);
        Iterator<Item> it = foundUser.getOfferedItems().iterator();
        Item firstItem = it.next();
   //        int xxx = 22;
        assertEquals("item added in offeredItems", furniture1, firstItem);
        Item item2 = AuctionMethods.offerPainting(u1, cat, omsch, "Nachtwacht", "Rembrandt");
        it = RegistrationMethods.getUser(iemand1).getOfferedItems().iterator();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());

        //de volgende code verwijderen als Item abstract is
        Category cat2 = new Category();
        cat2.setDescription("boek");
        Item item3 = AuctionMethods.offerItem(u1, cat2, "The science of Discworld");
        it = RegistrationMethods.getUser(iemand1).getOfferedItems().iterator();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());

        assertNull(furniture1.getHighestBid());
        Bid bid = AuctionMethods.newBid(furniture1, u2, new Money(150000, Money.EURO));
        assertNotNull(furniture1.getHighestBid());

        Item foundFurniture = AuctionMethods.getItem(furniture1.getId());
        int i = 3;
        assertEquals(foundFurniture.getHighestBid(), bid);
        assertTrue(foundFurniture.getClass() == Furniture.class);
    }
}
