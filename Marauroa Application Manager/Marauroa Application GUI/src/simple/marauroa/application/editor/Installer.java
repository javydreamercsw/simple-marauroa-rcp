package simple.marauroa.application.editor;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditorManager;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import javax.swing.ImageIcon;
import org.openide.modules.InstalledFileLocator;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;
import org.openide.windows.WindowManager;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall implements ActionListener {

    private MenuItem showItem = new MenuItem("Show");
    private MenuItem hideItem = new MenuItem("Hide");
    private boolean initialized = false;

    @Override
    public void restored() {
        //Get editors ready
        String[] editorSearchPath = PropertyEditorManager.getEditorSearchPath();
        String[] editorSearchPathList =
                Arrays.copyOf(editorSearchPath, editorSearchPath.length + 1);
        editorSearchPathList[editorSearchPath.length] =
                "simple.marauroa.application.editor";
        PropertyEditorManager.setEditorSearchPath(editorSearchPathList);

        if (!initialized) {
            WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

                @Override
                public void run() {
                    //Check the SystemTray is supported
                    if (!SystemTray.isSupported()) {
                        try {
                            throw new Exception("SystemTray is not supported");
                        } catch (Exception ex) {
                            initialized = false;
                            Exceptions.printStackTrace(ex);
                        }
                    } else {
                        try {
                            final PopupMenu popup = new PopupMenu();
                            final TrayIcon trayIcon = new TrayIcon(createImage("taskbarIcon.png",
                                    "tray icon"));
                            final SystemTray tray = SystemTray.getSystemTray();

                            showItem.addActionListener(Installer.this);
                            hideItem.addActionListener(Installer.this);

                            //Add components to pop-up menu
                            popup.add(showItem);
                            popup.addSeparator();
                            popup.add(hideItem);

                            trayIcon.setPopupMenu(popup);
                            try {
                                tray.add(trayIcon);
                            } catch (AWTException e) {
                                throw new Exception("TrayIcon could not be added.");
                            }
                            initialized = true;
                        } catch (Exception ex) {
                            initialized = false;
                            Exceptions.printStackTrace(ex);
                        }
                    }
                }
            });
        }
    }

    //Obtain the image URL
    protected static Image createImage(String path, String description)
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

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(hideItem)) {
            WindowManager.getDefault().getMainWindow().setVisible(false);
        } else if (ae.getSource().equals(showItem)) {
            WindowManager.getDefault().getMainWindow().setVisible(true);
            restored();
        }
    }
}
