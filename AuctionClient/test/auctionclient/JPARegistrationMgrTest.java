package auctionclient;

import java.util.List;
import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

public class JPARegistrationMgrTest {

    private RegistrationMethods registrationMgr;

    @Before
    public void setUp() throws Exception {
       cleanDB();
    }

    @Test
    public void registerUser() {
        User user1 = registrationMgr.registerUser("xxx1@yyy");
        assertTrue(user1.getEmail().equals("xxx1@yyy"));
        User user2 = registrationMgr.registerUser("xxx2@yyy2");
        assertTrue(user2.getEmail().equals("xxx2@yyy2"));
        User user2bis = registrationMgr.registerUser("xxx2@yyy2");
        assertNotSame(user2bis, user2);
        //geen @ in het adres
        assertNull(registrationMgr.registerUser("abc"));
    }

    @Test
    public void getUser() {
        User user1 = registrationMgr.registerUser("xxx5@yyy5");
        User userGet = registrationMgr.getUser("xxx5@yyy5");
        assertNotSame(userGet, user1);
        assertNull(registrationMgr.getUser("aaa4@bb5"));
        registrationMgr.registerUser("abc");
        assertNull(registrationMgr.getUser("abc"));
    }

    private static void cleanDB() {
        auctionclient.AuctionService service = new auctionclient.AuctionService();
        auctionclient.Auction port = service.getAuctionPort();
        port.cleanDB();
    }

}
