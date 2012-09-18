package simple.marauroa.application.api;

import javax.swing.JDialog;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public interface IAddApplicationDialogProvider {

    /*
     * Get the dialog actually shown to the user
     */

    public JDialog getDialog();

    /*
     * Pre-set the application name
     */
    public void setApplicationName(String name);

    /*
     * Set the field editable or not
     */
    public void setEditableApplicationName(boolean enabled);

    /*
     * Ignore folder creation. Used when trying to recover from file system.
     */
    public void setIgnoreFolderCreation(boolean ignore);
    
    /*
     * The value previously set. Should be false by default.
     */
    public boolean isFolderCreationIgnored();
}
