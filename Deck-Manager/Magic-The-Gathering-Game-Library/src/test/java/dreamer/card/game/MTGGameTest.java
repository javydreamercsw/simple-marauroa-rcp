package dreamer.card.game;

import com.reflexit.magiccards.core.cache.ICardCache;
import com.reflexit.magiccards.core.model.storage.db.IDataBaseCardStorage;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.*;
import static org.junit.Assert.*;
import org.openide.util.Lookup;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class MTGGameTest {

    private static final Logger LOG =
            Logger.getLogger(MTGGameTest.class.getName());

    @BeforeClass
    public static void setUpClass() throws Exception {
        Lookup.getDefault().lookup(IDataBaseCardStorage.class)
                .setPU("Card_Game_Interface_TestPU");
        Lookup.getDefault().lookup(IDataBaseCardStorage.class).initialize();
    }

    public MTGGameTest() {
    }

    /**
     * Test of updateDatabase method, of class MTGGame.
     */
    @Test
    public void testUpdateDatabase() {
        try {
            final MTGGame instance = new MTGGame();
            instance.init();
            for (Iterator<ICardCache> it = 
                    instance.getCardCacheImplementations().iterator(); 
                    it.hasNext();) {
                ICardCache cache = it.next();
                cache.getCacheLocationFile().deleteOnExit();
            }
            assertNotNull(instance.getUpdateRunnable());
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
