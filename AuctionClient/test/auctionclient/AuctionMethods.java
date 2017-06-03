/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionclient;

import java.util.List;

/**
 *
 * @author Luuk
 */
public class AuctionMethods {
    
    private static final AuctionService service = new AuctionService();
    private static final Auction port = service.getAuctionPort();

    public static List<Item> findItemByDescription(String description) {
        return port.findItemByDescription(description);
    }

    public static Item getItem(Long id) {
        return port.getItem(id);
    }

    public static Bid newBid(Item item, User bidder, Money costs) {
        return port.newBid(item, bidder, costs);
    }

    public static Item offerItem(User user, Category cat, String description) {
        return port.offerItem(user, cat, description);
    }

    public static boolean revokeItem(Item item) {
        return port.revokeItem(item);
    }
    
    public static void cleanDB() {
        port.cleanDB();
    }
    
}
