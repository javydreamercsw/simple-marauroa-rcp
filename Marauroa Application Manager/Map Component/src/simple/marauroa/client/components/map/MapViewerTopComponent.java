package simple.marauroa.client.components.map;

import com.bbn.openmap.LayerHandler;
import com.bbn.openmap.MapBean;
import com.bbn.openmap.MapHandler;
import com.bbn.openmap.MouseDelegator;
import com.bbn.openmap.MultipleSoloMapComponentException;
import com.bbn.openmap.event.MapMouseEvent;
import com.bbn.openmap.event.MapMouseListener;
import com.bbn.openmap.event.SelectMouseMode;
import com.bbn.openmap.gui.BasicMapPanel;
import com.bbn.openmap.gui.MapPanel;
import com.bbn.openmap.layer.shape.ShapeLayer;
import java.awt.BorderLayout;
import java.util.Collections;
import java.util.Properties;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
    dtd = "-//simple.marauroa.client.components.map//MapViewer//EN",
autostore = false)
@TopComponent.Description(
    preferredID = "MapViewerTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "simple.marauroa.client.components.map.MapViewerTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_MapViewerAction",
preferredID = "MapViewerTopComponent")
@Messages({
    "CTL_MapViewerAction=Map Viewer",
    "CTL_MapViewerTopComponent=Map Viewer Window",
    "HINT_MapViewerTopComponent=This is a MapViewer window"
})
public final class MapViewerTopComponent extends TopComponent {

    private final InstanceContent ic = new InstanceContent();

    public MapViewerTopComponent() {
        initComponents();
        setName(Bundle.CTL_MapViewerTopComponent());
        setToolTipText(Bundle.HINT_MapViewerTopComponent());
        setLayout(new BorderLayout());

        try {

            //MapPanel:
            MapPanel mapPanel = new BasicMapPanel();

            //MapHandler:
            MapHandler mapHandler = mapPanel.getMapHandler();
            mapHandler.add(new LayerHandler());
            mapHandler.add(this);

            //MapBean:
            MapBean mapBean = mapPanel.getMapBean();
            mapBean.setScale(120000000f);

            //Selection:
            MouseDelegator mouseDelegator = new MouseDelegator();
            mapHandler.add(mouseDelegator);
            SelectMouseMode selectMouseMode = new SelectMouseMode();
            mapHandler.add(selectMouseMode);
            mouseDelegator.setActive(selectMouseMode);

            //MapMouseListener:
            final MyMapMouseListener myMapMouseListener = new MyMapMouseListener();

            //ShapeLayer: 
            ShapeLayer shapeLayer = new ShapeLayer() {
                @Override
                public synchronized MapMouseListener getMapMouseListener() {
                    return myMapMouseListener;
                }
            };

            //Properties:
            Properties shapeLayerProps = new Properties();
            shapeLayerProps.put("lineColor", "000000");
            shapeLayerProps.put("fillColor", "BDDE83");
            shapeLayerProps.put("shapeFile", "org/myatc/initializer/dcwpo-browse.shp");
            shapeLayerProps.put("spatialIndex", "org/myatc/initializer/dcwpo-browse.ssx");

            //Assign properties to ShapeLayer: 
            shapeLayer.setProperties(shapeLayerProps);
            shapeLayer.setVisible(true);

            //Assign ShapeLayer to MapHandler:
            mapHandler.add(shapeLayer);

            add(mapPanel.getMapBean(), BorderLayout.CENTER);

        } catch (MultipleSoloMapComponentException msmce) {
        }

        associateLookup(new AbstractLookup(ic));
    }

    public class MyMapMouseListener implements MapMouseListener {

        @Override
        public String[] getMouseModeServiceList() {
            return new String[]{SelectMouseMode.modeID};
        }

        @Override
        public boolean mouseClicked(java.awt.event.MouseEvent e) {
            MapMouseEvent mme = (MapMouseEvent) e;
            //Optionally:
            //LatLonPoint latLonPoint = LatLonPoint.getFloat(mme.getLatLon());
            //UTMPoint utmPoint = UTMPoint.LLtoUTM(latLonPoint);
            //Publish something into Lookup:
            ic.set(Collections.singleton(mme.getLatLon()), null);
            return true;
        }

        @Override
        public boolean mousePressed(java.awt.event.MouseEvent e) {
            return true;
        }

        @Override
        public boolean mouseReleased(java.awt.event.MouseEvent e) {
            return true;
        }

        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
        }

        @Override
        public boolean mouseDragged(java.awt.event.MouseEvent e) {
            return true;
        }

        @Override
        public boolean mouseMoved(java.awt.event.MouseEvent e) {
            return true;
        }

        @Override
        public void mouseMoved() {
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
