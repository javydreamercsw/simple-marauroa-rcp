package marauroa.server.game.container;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;

import marauroa.common.crypto.Hash;
import marauroa.common.crypto.RSAKey;
import marauroa.server.game.db.AccountDAO;
import marauroa.server.game.db.DAORegister;
import org.junit.Before;

import org.junit.Test;

/**
 * Test the secure login procedure in the same way.
 * 
 * @author miguel
 * 
 */
public class SecureLoginTest{

    private static RSAKey key;

    /**
     * Initialize the container.
     *
     * @throws IOException
     *
     */
    @Before
    public void setUp() throws IOException {
        loadRSAKey();
    }

    public static void loadRSAKey() {
        key = new RSAKey(new BigInteger("24083767696329668268912537536174127468"
                + "626867947407231757744234300439278504980856392206847956297473"
                + "269498385017779266693371714954218185638245393292249278991792"
                + "37"),
                new BigInteger("22478183183241023717651701700429185637385076750"
                + "913416307227952013743326604648798383322370040762528496545279"
                + "63214772652641735279016325354691167883850414929419335"),
                new BigInteger("15"));
    }

    /**
     * This method supposes that you have an account already created with
     * username testUsername and password password.
     *
     * It test if verify works correctly with a correct account.
     *
     * @throws SQLException
     * @throws UnknownHostException
     */
    @Test
    public void testLogin() throws SQLException, UnknownHostException {
        String username = "testUsername3z23798";
        String password = "password";
        if (!DAORegister.get().get(AccountDAO.class).hasPlayer(username)) {
            DAORegister.get().get(AccountDAO.class).addPlayer(username, Hash.hash(password),
                    "example@example.com");
        }
        SecuredLoginInfo login = simulateSecureLogin(username,
                password);
        assertTrue("Unable to verify login", login.verify());
    }

    /**
     * This method suppose that you have an account already created with
     * username testUsername and password password. It test if verify works
     * correctly with a bad password.
     *
     * @throws SQLException
     * @throws UnknownHostException
     */
    @Test
    public void testLoginFailure() throws SQLException, UnknownHostException {
        String password = "badpassword";

        SecuredLoginInfo login = simulateSecureLogin("testUsername",
                password);
        assertFalse(login.verify());
    }

    public static SecuredLoginInfo simulateSecureLogin(
            String username, String password) throws UnknownHostException {
        byte[] serverNonce = Hash.random(Hash.hashLength());
        byte[] clientNonce = Hash.random(Hash.hashLength());

        byte[] clientNonceHash = Hash.hash(clientNonce);

        SecuredLoginInfo login = new SecuredLoginInfo(
                key, clientNonceHash,
                serverNonce, InetAddress.getLocalHost());

        byte[] b1 = Hash.xor(clientNonce, serverNonce);
        if (b1 == null) {
            fail("B1 is null");
        }

        byte[] b2 = Hash.xor(b1, Hash.hash(password));
        if (b2 == null) {
            fail("B2 is null");
        }

        byte[] cryptedPassword = key.encodeByteArray(b2);

        login.username = username;
        login.clientNonce = clientNonce;
        login.password = cryptedPassword;

        return login;
    }
}
