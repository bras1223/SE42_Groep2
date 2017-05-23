
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.Before;

public class AccountTest {

    final EntityManagerFactory emf = TestUtil.getEMF();
    EntityManager em, em1, em2;
    private static final Logger LOG = Logger.getLogger(AccountTest.class.getName());

    public AccountTest() {
    }

    @Before
    public void setUp() {
        em = emf.createEntityManager();
        em1 = emf.createEntityManager();
        em2 = emf.createEntityManager();
        try {
            new DatabaseCleaner(emf.createEntityManager()).clean();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }


}