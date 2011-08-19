package utilities.RPClass;

import simple.server.core.entity.item.Item;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import marauroa.common.game.RPClass;

import org.junit.Test;

public class ItemTestHelperTest {

    @org.junit.Test
    public void testcreateItem() throws Exception {
        ItemTestHelper.createItem();
        final Item item = ItemTestHelper.createItem("blabla");
        assertEquals("blabla", item.getName());

    }

    @Test
    public void testGenerateRPClasses() {
        ItemTestHelper.generateRPClasses();
        assertTrue(RPClass.hasRPClass("item"));

    }
}
