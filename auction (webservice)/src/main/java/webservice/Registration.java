package webservice;


import auction.domain.User;
import auction.service.AuctionMgr;
import auction.service.RegistrationMgr;
import auction.service.SellerMgr;
import javax.jws.WebService;
import javax.persistence.EntityManager;

/**
 *
 * @author Luuk
 */
@WebService
public class Registration {
    private RegistrationMgr registrationMgr;
    
    public Registration(EntityManager em) {
        registrationMgr = new RegistrationMgr(em);
    }
    
    public User registerUser(String email) {
        return registrationMgr.registerUser(email);
    }
    
    public User getUser(String email) {
        return registrationMgr.getUser(email);
    }
    
}
