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

    public static List<Item> findItemByDescription(String description) {
        Auction port = service.getAuctionPort();
        return port.findItemByDescription(description);
    }

    public static Item getItem(Long id) {
        Auction port = service.getAuctionPort();
        return port.getItem(id);
    }

    public static Bid newBid(Item item, User bidder, Money costs) {
        Auction port = service.getAuctionPort();
        return port.newBid(item, bidder, costs);
    }

    public static Item offerItem(User user, Category cat, String description) {
        System.out.println(service.getAuctionPort());
        Auction port = service.getAuctionPort();
        return port.offerItem(user, cat, description);
    }

    public static boolean revokeItem(Item item) {
        Auction port = service.getAuctionPort();
        return port.revokeItem(item);
    }
    
}
