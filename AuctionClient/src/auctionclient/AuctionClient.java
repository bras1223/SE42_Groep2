/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionclient;

/**
 *
 * @author Luuk
 */
public class AuctionClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    public static User registerUser(java.lang.String arg0) {
        auctionclient.RegistrationService service = new auctionclient.RegistrationService();
        auctionclient.Registration port = service.getRegistrationPort();
        return port.registerUser(arg0);
    }

    public static User getUser(java.lang.String arg0) {
        auctionclient.RegistrationService service = new auctionclient.RegistrationService();
        auctionclient.Registration port = service.getRegistrationPort();
        return port.getUser(arg0);
    }

    public static Item offerItem(auctionclient.User arg0, auctionclient.Category arg1, java.lang.String arg2) {
        auctionclient.AuctionService service = new auctionclient.AuctionService();
        auctionclient.Auction port = service.getAuctionPort();
        return port.offerItem(arg0, arg1, arg2);
    }

    public static boolean revokeItem(auctionclient.Item arg0) {
        auctionclient.AuctionService service = new auctionclient.AuctionService();
        auctionclient.Auction port = service.getAuctionPort();
        return port.revokeItem(arg0);
    }

    public static Item getItem(java.lang.Long arg0) {
        auctionclient.AuctionService service = new auctionclient.AuctionService();
        auctionclient.Auction port = service.getAuctionPort();
        return port.getItem(arg0);
    }

    public static java.util.List<auctionclient.Item> findItemByDescription(java.lang.String arg0) {
        auctionclient.AuctionService service = new auctionclient.AuctionService();
        auctionclient.Auction port = service.getAuctionPort();
        return port.findItemByDescription(arg0);
    }

    public static Bid newBid(auctionclient.Item arg0, auctionclient.User arg1, auctionclient.Money arg2) {
        auctionclient.AuctionService service = new auctionclient.AuctionService();
        auctionclient.Auction port = service.getAuctionPort();
        return port.newBid(arg0, arg1, arg2);
    }

    public static void cleanDB() {
        auctionclient.AuctionService service = new auctionclient.AuctionService();
        auctionclient.Auction port = service.getAuctionPort();
        port.cleanDB();
    }
    
}
