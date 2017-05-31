package webservice;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.ws.Endpoint;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Luuk
 */
public class Application {
    
    private final static String ADRESS_STRING_AUCTION = "http://localhost:8080/auction";
    private final static String ADRESS_STRING_REGISTRATION = "http://localhost:8080/registration";
    
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("auctionPU");
        EntityManager em = emf.createEntityManager();
        
        Endpoint.publish(ADRESS_STRING_AUCTION, new Auction(em));
        Endpoint.publish(ADRESS_STRING_REGISTRATION, new Registration(em));
    }
    
}
