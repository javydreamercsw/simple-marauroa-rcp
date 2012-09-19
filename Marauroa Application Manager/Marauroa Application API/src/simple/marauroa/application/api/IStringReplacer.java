package simple.marauroa.application.api;

import javax.swing.ImageIcon;

/**
 * This is mostly meant to replace strings in chat or other user generated text
 * for other stuff. (i.e. hide bad words, use emoticons)
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public interface IStringReplacer {

    /**
     * Indicate if this replacer has a replacement
     *
     * @param text String to replace
     * @return true if replacement is available.
     */
    public boolean hasReplacement(String text);

    /**
     * Get replacement for the text. This method is mostly an extension point 
     * for not handled replacement objects.
     * 
     * @param text String to replace
     * @return Replacement Object
     */
    public Object getReplacementObject(String text);

    /**
     * Get replacement for the text.
     * @param text String to replace
     * @return Replacement String
     */
    public String getReplacementString(String text);

    /**
     * Get replacement for the text.
     * @param text String to replace
     * @return Replacement ImageIcon
     */
    public ImageIcon getReplacementImageIcon(String text);
    
    /**
     * Support status
     * @return true it it has a String replacement
     */
    public boolean supportsString();
    
    /**
     * Support status
     * @return true it it has an Object replacement.
     * This should only be true for return types not supported.
     * It's really an extension point.
     */
    public boolean supportsObject();
    
    /**
     * Support status
     * @return true it it has an ImageIcon replacement
     */
    public boolean supportsImageIcon();
}
