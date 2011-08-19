package simple.server.application.db;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import marauroa.common.crypto.Hash;
import marauroa.common.game.RPObject;
import marauroa.server.game.db.AccountDAO;
import marauroa.server.game.db.CharacterDAO;
import marauroa.server.game.db.DAORegister;
import org.junit.BeforeClass;

import org.junit.Test;
import simple.server.core.engine.MockSimpleRPWorld;

/**
 * Test the character related methods of database access.
 * 
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> based on code by miguel
 * 
 */
public class CharacterAccessTest {

    private static AccountDAO accountDAO;
    private static CharacterDAO characterDAO;

    /**
     * Setup one time the database.
     *
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void createDatabase() throws Exception {
        MockSimpleRPWorld.get();

        accountDAO = DAORegister.get().get(AccountDAO.class);
        characterDAO = DAORegister.get().get(CharacterDAO.class);
    }

    /**
     * Add a character to a player account and test it existence with
     * hasCharacter method.
     *
     * @throws SQLException
     * @throws IOException
     */
    @Test
    public void addCharacter() throws SQLException, IOException {
        String username = "testUserCA";
        String character = "testCharacterCA";
        RPObject player = new RPObject();

        try {
            accountDAO.addPlayer(username, Hash.hash("testPassword"),
                    "email@email.com");
            assertTrue(accountDAO.hasPlayer(username));
            characterDAO.addCharacter(username, character, player);
            assertTrue(characterDAO.hasCharacter(username, character));
        } finally {
            characterDAO.removeCharacter(username, character);
            accountDAO.removePlayer(username);
        }
    }

    /**
     * Test that adding two times the same character throws a SQLException
     *
     * @throws SQLException
     * @throws IOException
     */
    @Test(expected = SQLException.class)
    public void doubleAddedCharacter() throws SQLException, IOException {
        String username = "testUserCA";
        String character = "testCharacterCA";
        RPObject player = new RPObject();
        try {
            accountDAO.addPlayer(username, Hash.hash("testPassword"),
                    "email@email.com");
            assertTrue(accountDAO.hasPlayer(username));
            characterDAO.addCharacter(username, character, player);
            assertTrue(characterDAO.hasCharacter(username, character));
            characterDAO.addCharacter(username, character, player);
            fail("Character was added");
        } finally {
            characterDAO.removeCharacter(username, character);
            accountDAO.removePlayer(username);
        }
    }

    /**
     * Test that remove character removed it and assert with hasCharacter.
     *
     * @throws SQLException
     * @throws IOException
     */
    @Test
    public void removeCharacter() throws SQLException, IOException {
        String username = "testUserCA";
        String character = "testCharacterCA";
        RPObject player = new RPObject();
        try {
            accountDAO.addPlayer(username, Hash.hash("testPassword"), "email@email.com");
            assertTrue(accountDAO.hasPlayer(username));
            characterDAO.addCharacter(username, character, player);
            assertTrue(characterDAO.hasCharacter(username, character));
            characterDAO.removeCharacter(username, character);
            assertFalse(characterDAO.hasCharacter(username, character));
        } finally {
            characterDAO.removeCharacter(username, character);
            accountDAO.removePlayer(username);
        }
    }

    /**
     * Check that removing the player does in fact also removes the character
     * that belonged to that player.
     *
     * @throws SQLException
     * @throws IOException
     */
    @Test
    public void removePlayerCharacter() throws SQLException, IOException {
        String username = "testUserCA";
        String character = "testCharacterCA";
        RPObject player = new RPObject();
        try {
            accountDAO.addPlayer(username, Hash.hash("testPassword"), "email@email.com");
            assertTrue(accountDAO.hasPlayer(username));
            characterDAO.addCharacter(username, character, player);
            assertTrue(characterDAO.hasCharacter(username, character));
            accountDAO.removePlayer(username);
            assertFalse(characterDAO.hasCharacter(username, character));
        } finally {
            characterDAO.removeCharacter(username, character);
            accountDAO.removePlayer(username);
        }
    }

    /**
     * Check that getCharacters return a list with all the characters that
     * belong to a player.
     *
     * @throws SQLException
     * @throws IOException
     */
    @Test
    public void getCharacters() throws SQLException, IOException {
        String username = "testUserCA";
        String[] characters = {"testCharacterCA1", "testCharacterCA2", "testCharacterCA3"};
        String[] resultC = new String[characters.length + 1];
        resultC[resultC.length - 1] = "testUserCA";
        System.arraycopy(characters, 0, resultC, 0, characters.length);
        try {
            accountDAO.addPlayer(username, Hash.hash("testPassword"), "email@email.com");
            for (String character : characters) {
                characterDAO.addCharacter(username, character, new RPObject());
            }

            List<String> result = characterDAO.getCharacters(username);
            assertArrayEquals(resultC, result.toArray());
        } finally {
            for (String character : characters) {
                characterDAO.removeCharacter(username, character);
            }
            accountDAO.removePlayer(username);
        }
    }

    /**
     * Check that storing and loading an avatar associated to a character of a
     * player works as expected. This code depends on RPObject and
     * Serialization.
     *
     * @throws SQLException
     * @throws IOException
     */
    @Test
    public void storeAndLoadCharacter() throws SQLException, IOException {
        String username = "testUserCA";
        String character = "testCharacterCA";
        RPObject player = new RPObject();
        player.put("one", "number one");
        player.put("two", 2);
        player.put("three", 3.0);
        try {
            assertFalse(accountDAO.hasPlayer(username));
            accountDAO.addPlayer(username, Hash.hash("testPassword"), "email@email.com");

            assertFalse(characterDAO.hasCharacter(username, character));
            characterDAO.addCharacter(username, character, player);

            RPObject loaded = characterDAO.loadCharacter(username, character);
            loaded.remove("#db_id");
            assertEquals(player, loaded);
        } finally {
            characterDAO.removeCharacter(username, character);
            accountDAO.removePlayer(username);
        }
    }
}
