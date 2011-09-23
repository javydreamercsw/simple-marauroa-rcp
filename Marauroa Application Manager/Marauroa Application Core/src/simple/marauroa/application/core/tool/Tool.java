package simple.marauroa.application.core.tool;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDialog;

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
}
