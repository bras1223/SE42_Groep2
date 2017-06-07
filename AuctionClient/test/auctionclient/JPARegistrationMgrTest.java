package auctionclient;

import java.util.List;
import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

public class JPARegistrationMgrTest {

    private Registration registrationService;
    private Auction auctionService;

    @Before
    public void setUp() throws Exception {
        registrationService = new RegistrationService().getRegistrationPort();
        auctionService = new AuctionService().getAuctionPort();
        auctionService.cleanDB();
    }

    @Test
    public void registerUser() {
        User user1 = registrationService.registerUser("xxx1@yyy");
        assertTrue(user1.getEmail().equals("xxx1@yyy"));
        User user2 = registrationService.registerUser("xxx2@yyy2");
        assertTrue(user2.getEmail().equals("xxx2@yyy2"));
        User user2bis = registrationService.registerUser("xxx2@yyy2");
        assertNotSame(user2bis, user2);
        //geen @ in het adres
        assertNull(registrationService.registerUser("abc"));
    }

    @Test
    public void getUser() {
        User user1 = registrationService.registerUser("xxx5@yyy5");
        User userGet = registrationService.getUser("xxx5@yyy5");
        assertNotSame(userGet, user1);
        assertNull(registrationService.getUser("aaa4@bb5"));
        registrationService.registerUser("abc");
        assertNull(registrationService.getUser("abc"));
    }

}
