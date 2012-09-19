package simple.marauroa.application.components.map;

import java.awt.Graphics;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Top component which displays maps.
 */
@ConvertAsProperties(
    dtd = "-//simple.marauroa.application.components.map//MapComponent//EN",
autostore = false)
@TopComponent.Description(
    preferredID = "MapComponentTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "simple.marauroa.application.components.map.MapComponentTopComponent")
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_MapComponentAction",
preferredID = "MapComponentTopComponent")
@Messages({
    "CTL_MapComponentAction=Map Component",
    "CTL_MapComponentTopComponent=Map Component Window",
    "HINT_MapComponentTopComponent=This is a Map Component window"
})
public final class MapComponentTopComponent extends TopComponent {

    private MapScene myScene;

    public MapComponentTopComponent() {
        initComponents();
        setName(Bundle.CTL_MapComponentTopComponent());
        setToolTipText(Bundle.HINT_MapComponentTopComponent());
        myScene = new MapScene();
        add(myScene.createView());
        myScene.updateBounds();
        //Show World of Warcraft map
//        TileFactoryInfo info = new TileFactoryInfo(
//                0, //min level
//                8, //max allowed level
//                9, // max level
//                256, //tile size
//                true, true, // x/y orientation is normal
//                "http://wesmilepretty.com/gmap2/", // base url
//                "x", "y", "z" // url args for x, y and z
//                ) {
//            @Override
//            public String getTileUrl(int x, int y, int zoom) {
//                int wow_zoom = 9 - zoom;
//                String url = this.baseURL;
//                if (y <= Math.pow(2, wow_zoom - 1)) {
//                    url = "http://int2e.com/gmapoutland2/";
//                }
//                return url + "zoom" + wow_zoom + "maps/" + x + "_" + y + "_" + wow_zoom + ".jpg";
//            }
//        };
//        myScene.setTileFactory(new DefaultTileFactory(info));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
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

    @Override
    public void paint(Graphics g) {
        myScene.updateBounds();
        super.paint(g);
    }
}
