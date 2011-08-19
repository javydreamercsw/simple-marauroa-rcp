package simple.server.application.db;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;

import marauroa.common.TimeoutConf;
import marauroa.common.crypto.Hash;
import marauroa.server.game.db.AccountDAO;
import marauroa.server.game.db.DAORegister;
import marauroa.server.game.db.LoginEventDAO;
import marauroa.server.game.container.SecureLoginTest;
import marauroa.server.game.container.SecuredLoginInfo;

import org.junit.BeforeClass;
import org.junit.Test;
import simple.server.core.engine.MockSimpleRPWorld;

/**
 * Test database methods that are related with player like adding a player,
 * removing it, changing its status, etc...
 * 
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> based on code by miguel
 * 
 */
public class PlayerAccessTest{

    private static AccountDAO accountDAO;
    private static LoginEventDAO loginEventDAO;

    /**
     * Setup one time the accountDAO.
     *
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void createDatabase() throws Exception {
        MockSimpleRPWorld.get();

        accountDAO = DAORegister.get().get(AccountDAO.class);
        loginEventDAO = DAORegister.get().get(LoginEventDAO.class);
    }

    /**
     * Test if create a player account works by adding it and making sure that
     * the account is there using has method.
     *
     * @throws SQLException
     */
    @Test
    public void addPlayer() throws SQLException {
        String username = "testUser";


        try {
            accountDAO.addPlayer(username, Hash.hash("testPassword"), "email@email.com");
            assertTrue(accountDAO.hasPlayer(username));
        } finally {
            accountDAO.removePlayer(username);
        }
    }

    /**
     * We test the change password method by changing the password of a existing
     * account There is right now no simple way of checking the value, as we
     * would need the RSA key of the server to encript the password. ( and this
     * is stored at marauroa.ini )
     *
     * @throws SQLException
     * @throws IOException
     */
    @Test
    public void changePassword() throws SQLException, IOException {
        String username = "testUser";

        SecureLoginTest.loadRSAKey();


        try {
            accountDAO.addPlayer(username, Hash.hash("testPassword"), "email@email.com");
            assertTrue(accountDAO.hasPlayer(username));

            SecuredLoginInfo login = SecureLoginTest.simulateSecureLogin(username,
                    "testPassword");
            assertTrue(accountDAO.verify(login));

            accountDAO.changePassword(username, "anewtestPassword");

            /*
             * To test if password is correct we need to use the Secure login
             * test unit
             */
            login = SecureLoginTest.simulateSecureLogin(username, "anewtestPassword");
            assertTrue(accountDAO.verify(login));

        } finally {
            accountDAO.removePlayer(username);
        }
    }

    /**
     * Test if adding two times the same player throw a SQLException
     *
     * @throws SQLException
     */
    @Test(expected = SQLException.class)
    public void doubleAddedPlayer() throws SQLException {
        String username = "testUser";


        try {
            if (accountDAO.hasPlayer(username)) {
                fail("Player was not expected");
            }
            accountDAO.addPlayer(username, Hash.hash("testPassword"), "email@email.com");

            if (!accountDAO.hasPlayer(username)) {
                fail("Player was expected");
            }
            accountDAO.addPlayer(username, Hash.hash("testPassword"), "email@email.com");

            fail("Player was added");
        } finally {
            accountDAO.removePlayer(username);
        }
    }

    /**
     * Remove a player and check that it is not anymore at database with has
     * method.
     *
     * @throws SQLException
     */
    @Test
    public void removePlayer() throws SQLException {
        String username = "testUser";


        try {
            accountDAO.addPlayer(username, Hash.hash("testPassword"), "email@email.com");
            assertTrue(accountDAO.hasPlayer(username));
            accountDAO.removePlayer(username);
            assertFalse(accountDAO.hasPlayer(username));
        } finally {
            accountDAO.removePlayer(username);
        }
    }

    /**
     * Check get status method. Every account is active by default.
     *
     * @throws SQLException
     */
    @Test
    public void getStatus() throws SQLException {
        String username = "testUser";



        try {
            accountDAO.addPlayer(username, Hash.hash("testPassword"), "email@email.com");
            assertTrue("active".equals(accountDAO.getAccountStatus(username)));
        } finally {
            accountDAO.removePlayer(username);
        }
    }

    /**
     * Check the set status method, it uses getStatus to check the set value.
     *
     * @throws SQLException
     */
    @Test
    public void setStatus() throws SQLException {
        String username = "testUser";



        try {
            accountDAO.addPlayer(username, Hash.hash("testPassword"), "email@email.com");
            assertTrue("active".equals(accountDAO.getAccountStatus(username)));
            accountDAO.setAccountStatus(username, "banned");
            assertTrue("banned".equals(accountDAO.getAccountStatus(username)));
        } finally {
            accountDAO.removePlayer(username);
        }
    }

    /**
     * Test if create a player account works by adding it and making sure that
     * the account is there using has method.
     *
     * @throws SQLException
     * @throws UnknownHostException
     */
    @Test
    public void blockAccountPlayer() throws SQLException, UnknownHostException {
        String username = "testUser";


        try {
            accountDAO.addPlayer(username, Hash.hash("testPassword"), "email@email.com");
            assertTrue(accountDAO.hasPlayer(username));

            InetAddress address = InetAddress.getLocalHost();

            assertFalse(loginEventDAO.isAccountBlocked(username));

            for (int i = 0; i < TimeoutConf.FAILED_LOGIN_ATTEMPTS_ACCOUNT + 1; i++) {
                DAORegister.get().get(LoginEventDAO.class).addLoginEvent(username, address, null, null, false);
            }

            assertTrue(loginEventDAO.isAccountBlocked(username));
        } finally {
            accountDAO.removePlayer(username);
        }
    }
}
