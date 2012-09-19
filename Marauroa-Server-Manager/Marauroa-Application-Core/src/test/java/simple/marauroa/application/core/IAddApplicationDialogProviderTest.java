/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simple.marauroa.application.core;

import simple.marauroa.application.api.IAddApplicationDialogProvider;
import javax.swing.JDialog;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class IAddApplicationDialogProviderTest {
    
    public IAddApplicationDialogProviderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getDialog method, of class IAddApplicationDialogProvider.
     */
    @Test
    public void testGetDialog() {
        System.out.println("getDialog");
        IAddApplicationDialogProvider instance = new IAddApplicationDialogProviderImpl();
        assertTrue(instance.getDialog() instanceof JDialog);
    }

    /**
     * Test of setApplicationName method, of class IAddApplicationDialogProvider.
     */
    @Test
    public void testSetApplicationName() {
        System.out.println("setApplicationName");
        String name = "test";
        IAddApplicationDialogProvider instance = new IAddApplicationDialogProviderImpl();
        instance.setApplicationName(name);
    }

    /**
     * Test of setEditableApplicationName method, of class IAddApplicationDialogProvider.
     */
    @Test
    public void testSetEditableApplicationName() {
        System.out.println("setEditableApplicationName");
        IAddApplicationDialogProvider instance = new IAddApplicationDialogProviderImpl();
        instance.setEditableApplicationName(true);
    }

    /**
     * Test of setIgnoreFolderCreation method, of class IAddApplicationDialogProvider.
     */
    @Test
    public void testSetIgnoreFolderCreation() {
        System.out.println("setIgnoreFolderCreation");
        IAddApplicationDialogProvider instance = new IAddApplicationDialogProviderImpl();
        instance.setIgnoreFolderCreation(true);
    }

    /**
     * Test of isFolderCreationIgnored method, of class IAddApplicationDialogProvider.
     */
    @Test
    public void testIsFolderCreationIgnored() {
        System.out.println("isFolderCreationIgnored");
        IAddApplicationDialogProvider instance = new IAddApplicationDialogProviderImpl();
        assertTrue(instance.isFolderCreationIgnored());
    }

    public class IAddApplicationDialogProviderImpl implements IAddApplicationDialogProvider {

        @Override
        public JDialog getDialog() {
            return new JDialog();
        }

        @Override
        public void setApplicationName(String name) {
            assertTrue(name.equals("test"));
        }

        @Override
        public void setEditableApplicationName(boolean enabled) {
            assertTrue(enabled);
        }

        @Override
        public void setIgnoreFolderCreation(boolean ignore) {
            assertTrue(ignore);
        }

        @Override
        public boolean isFolderCreationIgnored() {
            return true;
        }
    }  
}
