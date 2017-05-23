
import bank.domain.Account;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestUtil {

    static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bankPU");

    /**
     *
     * @return the number of Accounts stored in the database
     */
    static public int getNrOfAccountRecordsInDB() {
        EntityManager em = getEMF().createEntityManager();
        return ((Number) em.createNamedQuery("Account.count").getSingleResult()).intValue();
    }

    /**
     * Search for an entity of the specified class and primary key.
     *
     * @param id
     * @return the found Account instance or null if the entity does not exist
     */
    static public Account getAccountById(Long id) {
        EntityManager em = getEMF().createEntityManager();
        return em.find(Account.class, id);
    }

    static public EntityManagerFactory getEMF() {
        return emf;
    }

}
