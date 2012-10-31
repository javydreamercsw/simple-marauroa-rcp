/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import org.junit.Test;
import static org.junit.Assert.*;
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
    }
}
