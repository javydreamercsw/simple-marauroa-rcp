package simple.marauroa.application.gui;

//import org.openide.util.ImageUtilities;
import java.awt.BorderLayout;
import java.util.Collection;
import javax.swing.JComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.explorer.view.BeanTreeView;
import org.openide.util.*;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;
import simple.marauroa.application.api.IDiagramManager;
import simple.marauroa.application.api.IMarauroaApplication;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//simple.marauroa.application.gui//Visualizer//EN",
autostore = false)
@TopComponent.Description(preferredID = "VisualizerTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@TopComponent.OpenActionRegistration(displayName = "#CTL_VisualizerAction",
preferredID = "VisualizerTopComponent")
public final class VisualizerTopComponent extends TopComponent
        implements LookupListener {

    private Lookup.Result result = null;
    private static VisualizerTopComponent instance;
    private final JComponent myView;
    private IMarauroaApplication lastSelected = null;
    private final ApplicationScene scene;

    @Messages({"CTL_VisualizerTopComponent=Visualizer Window",
        "HINT_VisualizerTopComponent=This is a Visualizer window"})
    public VisualizerTopComponent() {
        initComponents();
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        scene = (ApplicationScene) Lookup.getDefault().lookup(IDiagramManager.class).get();
        myView = scene.createView();

        serverPane.setViewportView(myView);

        add(scene.createSatelliteView(), BorderLayout.WEST);

        associateLookup(Lookups.fixed(
                // exposed TopComponent, 
                //   and SatelliteViewProvider, BirdViewProvider interfaces, too
                (VisualizerTopComponent) this, getActionMap() // do not forget expose ActionMap
                , scene // if some object needs it and if it is final
                ));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        serverPane = new BeanTreeView();

        setLayout(new java.awt.BorderLayout());
        add(serverPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane serverPane;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files
     * only, i.e. deserialization routines; otherwise you could get a
     * non-deserialized instance. To obtain the singleton instance, use
     * {@link #findInstance}.
     */
    public static synchronized VisualizerTopComponent getDefault() {
        if (instance == null) {
            instance = new VisualizerTopComponent();
        }
        return instance;
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public void componentOpened() {
        result = Utilities.actionsGlobalContext().lookupResult(IMarauroaApplication.class);
        result.addLookupListener(this);
    }

    @Override
    public void componentClosed() {
        result.removeLookupListener(this);
        result = null;
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
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
    public void resultChanged(LookupEvent ev) {
        Lookup.Result r = (Lookup.Result) ev.getSource();
        Collection c = r.allInstances();
        //Repopulate
        if (!c.isEmpty()) {
            //Reset
            try {
                IMarauroaApplication app = (IMarauroaApplication) c.iterator().next();
                if (lastSelected == null || (lastSelected != null && !app.getName().equals(lastSelected.getName()))) //Draw the selected one
                {
                    scene.clear();
                    scene.addMarauroaApplication(app);
                    lastSelected = app;
                }
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        scene.validate();
    }
}
