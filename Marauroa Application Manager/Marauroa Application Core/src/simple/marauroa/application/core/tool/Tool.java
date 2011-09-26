package simple.marauroa.application.core.tool;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import org.openide.modules.InstalledFileLocator;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public class Tool {

    private Tool() {
    }

    /**
     * Center dialog in screen
     * @param dialog
     */
    public static void centerDialog(JDialog dialog) {

        Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension window = dialog.getSize();

        dialog.setLocation((s.width - window.width) / 2,
                (s.height - window.height) / 2);

    }
    
    //Obtain the image URL
    public static Image createImage(String module_id ,String path, String description)
            throws MalformedURLException, Exception {
        File icon = InstalledFileLocator.getDefault().locate(path,
                "simple.marauroa.application.gui", false);
        URL imageURL = icon.toURI().toURL();

        if (imageURL == null) {
            throw new Exception("Resource not found: " + path);
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
}
