package utilities.RPClass;

import simple.server.core.entity.Entity;
import marauroa.common.game.RPClass;

public class EntityTestHelper {

    public static void generateRPClasses() {

        if (!RPClass.hasRPClass("entity")) {
            Entity.generateRPClass();
        }
    }
}
