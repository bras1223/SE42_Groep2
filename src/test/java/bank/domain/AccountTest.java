package bank.domain;

import bank.dao.AccountDAO;
import bank.dao.AccountDAOJPAImpl;
import org.junit.Before;
import org.junit.Test;
import util.DatabaseCleaner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;

import static org.junit.Assert.*;


/**
 * Created by Luuk on 19-04-17.
 */
public class AccountTest {

    private DatabaseCleaner databaseCleaner;
    private EntityManager em;
    private EntityManagerFactory emf;

    @Before
    public void setupCleaner() {
        emf = Persistence.createEntityManagerFactory("bankPU");
        em = emf.createEntityManager();

        databaseCleaner = new DatabaseCleaner(em);
    }

    /**
     * Waarde asserties en printstatements:
     * AssertNull; Controleert of de waarde van het ID nog null is.
     * Toont aan dat de generation pas gebeurd na de commit.
     *
     * AssertTrue; Controleert of de waarde hoger is dan 0 en dus gegenereerd is.
     * Dit gebeurd pas na de commit.
     *
     * Printstatement: Laat de gegenereerde id zien.
     *
     * SQL statements:
     * DELETE FROM ACCOUNT
     * INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?)
     * SELECT LAST_INSERT_ID()
     * Eindresultaat:
     * Database wordt leeggemaakt, nieuw account met gegenegeerd ID is toegevoegd.
     */
    @Test
    public void testPersistAndCommit_checkGeneratedID () {
        startUp();

        Account account = new Account(111L);
        em.getTransaction().begin();
        em.persist(account);

        assertNull(account.getId());

        em.getTransaction().commit();

        System.out.println("AccountId: " + account.getId());

        assertTrue(account.getId() > 0L);
        em.close();
    }

    /**
     * Waarde asserties en printstatements:
     * AssertNull: Controleert of de waarde van het ID nog null is.
     * Toont aan dat de generatie van het id pas gebeurt na de generatie.
     *
     * AssertEquals: Controlleert of de count gelijk is aan 0.
     * Toont dat de tabel geen waardes er geen in meer heeft staan.
     *
     * SQL statements:
     * DELETE FROM ACCOUNT
     * SELECT COUNT(ID) FROM ACCOUNT
     *
     * Eindresultaat:
     * Database wordt leeggemaakt, account wordt aangemaakt en daarna wordt de actie terug getrokken.
     * Database is weer leeg.
     */
    @Test
    public void testRollback_checkNoRecordsInTable() {
        startUp();

        Account account = new Account(111L);
        em.getTransaction().begin();
        em.persist(account);

        assertNull(account.getId());

        em.getTransaction().rollback();

        AccountDAO accountDAO = new AccountDAOJPAImpl(em);

        assertEquals(0, accountDAO.count());

        em.close();
    }

    /**
     *
     */
    @Test
    public void testFlush_checkGenerationAfterFlush() {
        startUp();

        Long expected = -100L;
        Account account = new Account(111L);
        account.setId(expected);

        em.getTransaction().begin();
        em.persist(account);

        assertEquals(expected, account.getId());
        em.flush();

        em.getTransaction().commit();
        em.close();
    }

    /**
     * Cleans the database & makes a new instance of the EntityManager.
     */
    private void startUp() {
        try {
            databaseCleaner.clean();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        em = emf.createEntityManager();
    }
}
