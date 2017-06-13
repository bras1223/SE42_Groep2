package auctionclient2;

import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import webservice.Auction;
import webservice.AuctionService;
import webservice.User;

/**
 *
 * @author Luuk
 */
public class JPARegistrationMgrTest {


    private webservice.Registration registration;
    private Auction auction;

    @Before
    public void setUp() {
        registration = new webservice.RegistrationService().getRegistrationPort();
        auction = new AuctionService().getAuctionPort();
        auction.cleanDB();
    }

    @Test
    public void registerUser() {
        User user1 = registration.registerUser("xxx1@yyy");
        assertTrue(user1.getEmail().equals("xxx1@yyy"));
        User user2 = registration.registerUser("xxx2@yyy2");
        assertTrue(user2.getEmail().equals("xxx2@yyy2"));
        User user2bis = registration.registerUser("xxx2@yyy2");
        assertNotEquals(user2bis.getId(), user2.getId());
        //geen @ in het adres
        assertNull(registration.registerUser("abc"));
    }

    @Test
    public void getUser() {
        User user1 = registration.registerUser("xxx5@yyy5");
        User userGet = registration.getUser("xxx5@yyy5");
        assertEquals(userGet.getEmail(), user1.getEmail());
        assertNull(registration.getUser("aaa4@bb5"));
        registration.registerUser("abc");
        assertNull(registration.getUser("abc"));
    }
    
}
