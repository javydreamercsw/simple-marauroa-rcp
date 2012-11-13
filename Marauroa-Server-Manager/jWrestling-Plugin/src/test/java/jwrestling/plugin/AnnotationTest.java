/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwrestling.plugin;

import jwrestling.plugin.JWrestlingApplicationProvider;
import static org.junit.Assert.*;
import org.junit.Test;
import org.openide.util.Lookup;
import simple.marauroa.application.api.IMarauroaApplicationProvider;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class AnnotationTest {

    public AnnotationTest() {
    }

    @Test
    public void checkLookup() {
        assertFalse(Lookup.getDefault().lookupAll(
                IMarauroaApplicationProvider.class).isEmpty());
        boolean found = false;
        for (IMarauroaApplicationProvider p : Lookup.getDefault().lookupAll(
                IMarauroaApplicationProvider.class)) {
            if (p instanceof JWrestlingApplicationProvider) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
}
