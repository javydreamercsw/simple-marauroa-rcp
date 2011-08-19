package simple.marauroa.application.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

@ActionID(category = "File",
id = "simple.marauroa.application.editor.Hide")
@ActionRegistration(iconBase = "simple/marauroa/application/gui/resource/app.png",
displayName = "#CTL_Hide")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 2550, separatorAfter = 2575),
    @ActionReference(path = "Shortcuts", name = "D-H")
})
@Messages("CTL_Hide=Minimize to Tray")
public final class Hide implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        WindowManager.getDefault().getMainWindow().setVisible(false);
    }
}
