package simple.marauroa.application.gui;

import java.awt.BorderLayout;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ActionMap;
import javax.swing.text.DefaultEditorKit;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
//import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.filesystems.FileUtil;
import simple.marauroa.application.api.IMarauroaApplication;
import simple.marauroa.application.core.EventBus;
import simple.marauroa.application.core.EventBusListener;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//simple.marauroa.application.gui//ApplicationExplorer//EN",
autostore = false)
@TopComponent.Description(preferredID = "ApplicationExplorerTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "simple.marauroa.application.gui.ApplicationExplorerTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_ApplicationExplorerAction",
preferredID = "ApplicationExplorerTopComponent")
public final class ApplicationExplorerTopComponent extends TopComponent
        implements ExplorerManager.Provider, EventBusListener<IMarauroaApplication> {

    private static ApplicationExplorerTopComponent instance;
    private final ExplorerManager explorerManager = new ExplorerManager();

    @Messages({"HINT_ApplicationExplorerTopComponent=This is a ApplicationExplorer window",
        "CTL_ApplicationExplorerTopComponent=ApplicationExplorer Window"})
    public ApplicationExplorerTopComponent() {
        initComponents();
        add (new BeanTreeView(), BorderLayout.CENTER);
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        ActionMap map = getActionMap();
        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(getExplorerManager()));
        map.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(getExplorerManager()));
        map.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(getExplorerManager()));
        map.put("delete", ExplorerUtils.actionDelete(getExplorerManager(), true));

        associateLookup(ExplorerUtils.createLookup(getExplorerManager(), getActionMap()));
        getExplorerManager().setRootContext(new RootNode(new MarauroaAppChildFactory()));
        getExplorerManager().getRootContext().setDisplayName("Registered Applications");
    }

    // It is good idea to switch all listeners on and off when the
    // component is shown or hidden. In the case of TopComponent use:
    @Override
    protected void componentActivated() {
        ExplorerUtils.activateActions(getExplorerManager(), true);
    }

    @Override
    protected void componentDeactivated() {
        ExplorerUtils.activateActions(getExplorerManager(), false);
    }

    /** 
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized ApplicationExplorerTopComponent getDefault() {
        if (instance == null) {
            instance = new ApplicationExplorerTopComponent();
        }
        return instance;
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        EventBus.getDefault().subscribe(IMarauroaApplication.class,
                (ApplicationExplorerTopComponent) this);
    }

    @Override
    public void componentClosed() {
        EventBus.getDefault().unsubscribe(IMarauroaApplication.class,
                (ApplicationExplorerTopComponent) this);
    }

    void writeProperties(java.util.Properties p) {
        try {
            // better to version settings since initial version as advocated at
            // http://wiki.apidesign.org/wiki/PropertyFiles
            p.setProperty("version", "1.0");
            if (FileUtil.getConfigRoot().getFileObject(
                    "Config/Marauroa/ApplicationExplorer.properties") == null) {
                FileUtil.getConfigRoot().createFolder("Config");
                FileUtil.getConfigRoot().getFileObject("Config").createFolder("Marauroa");
                FileUtil.getConfigRoot().getFileObject("Config/Marauroa").createData("ApplicationExplorer.properties");
            }
            p.store(FileUtil.getConfigRoot().getFileObject(
                    "Config/Marauroa/ApplicationExplorer.properties").getOutputStream(),
                    new Date(System.currentTimeMillis()).toString());
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    private void update() throws IntrospectionException {
        ((RootNode) getExplorerManager().getRootContext()).refresh();
    }

    @Override
    public void notify(final IMarauroaApplication application) {
        if (application != null) {
            Logger.getLogger(ApplicationExplorerTopComponent.class.getSimpleName()).log(Level.FINE,
                    "A new Marauroa Application has been "
                    + "registered: {0}", application.toStringForDisplay());
            try {
                update();
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            try {
                update();
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
