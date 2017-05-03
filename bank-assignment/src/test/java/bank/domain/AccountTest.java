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
    private AccountDAO accountDAO;

    @Before
    public void setupCleaner() {
        emf = Persistence.createEntityManagerFactory("bankPU");
        em = emf.createEntityManager();

        databaseCleaner = new DatabaseCleaner(em);

        try {
            databaseCleaner.clean();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        em = emf.createEntityManager();
        accountDAO = new AccountDAOJPAImpl(em);
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
     * SQL statements:
     * DELETE FROM ACCOUNT
     * INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?)
     * SELECT LAST_INSERT_ID()
     */
    @Test
    public void testFlush_checkGenerationAfterFlush() {

        Long expected = -100L;
        Account account = new Account(111L);

        em.getTransaction().begin();
        em.persist(account);


        account.setId(expected);
        System.out.println(account.getId());
        assertEquals(expected, account.getId()); //ID wordt door ons zelf gezet, en is dus lokaal hetzelfde.
        em.flush();

        em.getTransaction().commit();
        assertNotEquals(expected, account.getId()); //ID wordt random gegenereerd bij database insert.
        em.close();
    }

    /**
     * Waarde asserties en printstatements:
     * AssertEquals; wijzigingen voor de commit worden nog doorgevoerd, ook in de database.
     * Waarde zal dus gelijk zijn aan de gezette waarde.
     *
     * AssertEquals; Het account wordt uit de database gehaald, en in het nieuwe object gezet.
     *
     * SQL Statements:
     * DELETE FROM ACCOUNT
     * INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?)
     *
     * SELECT LAST_INSERT_ID()
     * SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM ACCOUNT WHERE (ID = ?)
     */
    @Test
    public void testChangeAfterPersist_() {

        Long expectedBalance = 400L;
        Account account = new Account(114L);
        em.getTransaction().begin();
        em.persist(account);
        account.setBalance(expectedBalance);
        em.getTransaction().commit();

        assertEquals(expectedBalance, account.getBalance());

        Long cid = account.getId();
        account = null;

        EntityManager em2 = emf.createEntityManager();
        em2.getTransaction().begin();
        Account found = em2.find(Account.class, cid);

        assertEquals(expectedBalance, found.getBalance());
        em.close();
    }

    /**
     * SQL Statements:
     * DELETE FROM ACCOUNT
     * INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?)
     *
     * SELECT LAST_INSERT_ID()
     * UPDATE ACCOUNT SET BALANCE = ? WHERE (ID = ?)
     *
     * SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM ACCOUNT WHERE (ID = ?)
     *
     * Eindresultaat:
     * Account met balance van 200L wordt toegevoegd.
     */
    @Test
    public void testChangeObjectAndRefresh_checkUpdateOtherObject() {

        Long expectedBalance = 400L;
        Account account = new Account(114L);
        em.getTransaction().begin();
        em.persist(account);
        account.setBalance(expectedBalance);
        em.getTransaction().commit();

        Long cid = account.getId();
        Account found = em.find(Account.class, cid);

        Long newBalance = 200L;

        found.setBalance(newBalance);
        em.getTransaction().begin();
        em.flush();
        em.getTransaction().commit();

        em.refresh(account);
        assertEquals(newBalance, account.getBalance());
        em.close();
    }

    /**
     * SQL Statement:
     * DELETE FROM ACCOUNT
     * INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?)
     *
     * SELECT LAST_INSERT_ID()
     * INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?)
     *
     * SELECT LAST_INSERT_ID()
     * SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM ACCOUNT WHERE (ACCOUNTNR = ?)
     *
     * INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?)
     *
     * SELECT LAST_INSERT_ID()
     * INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?)
     *
     * SELECT LAST_INSERT_ID()
     * UPDATE ACCOUNT SET BALANCE = ? WHERE (ID = ?)
     *
     * Eindresultaat:
     * 4 accounts worden toegevoegd, met ieder een verschillend id. Balance wordt zoals hij lokaal staat toegevoegd.
     */
    @Test
    public void testMerge_() {
        Account acc = new Account(1L);
        Account acc2 = new Account(2L);
        Account acc9 = new Account(9L);

        //scenerio 1
        Long balance1 = 100L;
        em.getTransaction().begin();
        em.persist(acc);

        acc.setBalance(balance1);
        em.getTransaction().commit();

        Long expected = 1L;

        assertEquals(balance1, acc.getBalance()); // 'acc' wordt op normale manier persist, en nog voor de commit gewijzigd.
        //Deze wijzigingen worden dus nog meegenomen naar de database.
        assertEquals(expected, acc.getAccountNr()); // Accountnr wordt gezet, zoals in de constructor. Deze is dus nog gelijk.

        Long cid = acc.getId();

        Account found = em.find(Account.class, cid);

        assertEquals(balance1, found.getBalance()); // Account 'found' krijgt dezelfde data als 'acc' uit de db. Balance is dus gelijk.
        assertEquals(expected, found.getAccountNr()); //Account 'found' krijgt dezelfde data als 'acc' uit de db. AccountNr is dus gelijk.


        //scenario 2
        Long balance2a = 211L;

        acc = new Account(2L);
        em.getTransaction().begin();

        acc9 = em.merge(acc);
        acc.setBalance(balance2a);

        acc9.setBalance(balance2a+balance2a);
        em.getTransaction().commit();

        assertNotEquals(acc9.getBalance(), acc.getBalance()); // Na merge wordt de balance nog gewijzigd. Deze is dus niet gelijk.
        assertEquals(acc9.getAccountNr(), acc.getAccountNr()); // Accountnr wordt bij merge gelijk getrokken.

        acc2 = accountDAO.findByAccountNr(acc.getAccountNr());

        assertNotEquals(acc2.getBalance(), acc.getBalance()); // Balance is niet gelijk, doordat de balance van acc9 er nu in gemerged is.

        assertEquals(acc2.getBalance(), acc9.getBalance()); // Balance is gelijk aan acc9, doordat de balance in de database gemerged is.
        assertEquals(acc2.getAccountNr(), acc.getAccountNr()); // Accountnummer is gelijk, door merge.


        //scenario 3
        Long balance3b = 322L;
        Long balance3c = 333L;
        acc = new Account(3L);

        em.getTransaction().begin();

        acc2 = em.merge(acc);

        assertFalse(em.contains(acc)); // Gegevens uit acc worden enkel gemerged met object met hetzelfde id.
        assertTrue(em.contains(acc2)); // Object wordt bij merge teruggegeven, en bestaat dus.
        assertNotEquals(acc,acc2);  // Account is, zoals hierboven te beredeneren, niet gelijk. acc2 is het nieuwe (final) object.

        acc2.setBalance(balance3b);
        acc.setBalance(balance3c);
        em.getTransaction().commit();

        assertEquals(balance3b, acc2.getBalance()); // Balance van 'acc2' wordt hierboven gelijk gezet aan 'balance3b'.
        assertEquals(balance3c, acc.getBalance()); // Balance van 'acc' wordt hierboven gelijk gezet aan 'balance3c'.


        // scenario 4
        Account account = new Account(114L);
        account.setBalance(450L);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(account);
        em.getTransaction().commit();

        Account account2 = new Account(114L);
        Account tweedeAccountObject = account2;
        tweedeAccountObject.setBalance(650l);

        assertEquals((Long)650L,account2.getBalance());  //Verwijzing naar het account2 object wordt in 'tweedeAccountObject' gezet.
        // De methoden worden dus op hetzelfde object uitgevoerd.

        account2.setId(account.getId());
        em.getTransaction().begin();
        account2 = em.merge(account2);
        assertSame(account,account2);  //Doordat 'account' en 'account2' hetzelfde AccountNr hebben, worden deze bij merge
        // gelijk gesteld. De objecten in de EntityManager worden dus gelijk.

        assertTrue(em.contains(account2));  //Door de merge word ook 'account2' in de EntityManager opgenomen.
        assertFalse(em.contains(tweedeAccountObject));  // 'account2' is gewijzigd, door de merge. 'tweedeAcocuntObject'
        // is dus niet meer gelijk aan het bekende object in de EntityManager

        tweedeAccountObject.setBalance(850l);
        assertEquals((Long)650L,account.getBalance());  // Zoals hierboven beschreven, heeft 'tweedeAccountObject' geem referentie
        // meer naar het 'account2' object. Dit heeft dus geen invloed meer op objecten in de EntityManager.
        assertEquals((Long)650L,account2.getBalance());  //idem hierboven.
        em.getTransaction().commit();
        em.close();
    }

    /**
     * SQL Statements:
     * DELETE FROM ACCOUNT
     * INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?)
     *
     * SELECT LAST_INSERT_ID()
     * SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM ACCOUNT WHERE (ID = ?)
     *
     * Eindresultaat:
     * Account 'acc1' wordt toegevoegd.
     */
    @Test
    public void testFindAndClear_checkDifference2Scenarios(){
        Account acc1 = new Account(77L);
        em.getTransaction().begin();
        em.persist(acc1);
        em.getTransaction().commit();

        Account accF1;
        Account accF2;

        //scenario 1
        accF1 = em.find(Account.class, acc1.getId());
        accF2 = em.find(Account.class, acc1.getId());
        assertSame(accF1, accF2); //Referenties worden naar hetzelfde object gezet. Hetzelfde object word uit de EntityManager gehaald.

        //scenario 2
        accF1 = em.find(Account.class, acc1.getId());
        em.clear();
        accF2 = em.find(Account.class, acc1.getId());
        assertNotSame(accF1, accF2); // Door clear word het entitymanager cache leeggemaakt. Bij een find word
        //er een nieuw object aangemaakt, met de data uit de database. Dit object is dus niet gelijk.

    }

    /**
     * SQL Statements:
     * DELETE FROM ACCOUNT
     * INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?)
     *
     * SELECT LAST_INSERT_ID()
     *
     * Eindresultaat:
     * Tabel is leeg.
     */
    @Test
    public void testRemove_deletedFromDatabase() {
        Account acc1 = new Account(88L);
        em.getTransaction().begin();
        em.persist(acc1);
        em.getTransaction().commit();
        Long id = acc1.getId();
        //Database bevat nu een account.

        em.remove(acc1);
        assertEquals(id, acc1.getId()); // 'acc1' staat nog in de cache van de EntityManager, en kan dus lokaal nog gevonden worden.
        Account accFound = em.find(Account.class, id);
        assertNull(accFound); // Bij find word het object uit de database gehaald. Door de remove is deze niet meer beschikbaar en wordt hij dus null.
    }

    /**
     * Opdracht 9; Verschillende Generation-Types:
     * GenerationType.IDENTITY:
     * De database kiest zelf een nextVal voor het id.
     *
     * GenerationType.SEQUENCE:
     * Bij Sequence wordt een sequence gebruikt, waarbinnen een id gekozen wordt. Deze is dus niet gelijk aan het initiele id.
     * Deze sequence is zelf te defineren.
     *
     * GenerationType.TABLE:
     * Data van lokaal object wordt niet meer gegenereerd. Deze wordt letterlijk overgenomen.
     * Sommige tests falen, doordat deze uitgaan van het wijzigen van het ID. Dit wordt bij deze niet gedaan.
     */
}
