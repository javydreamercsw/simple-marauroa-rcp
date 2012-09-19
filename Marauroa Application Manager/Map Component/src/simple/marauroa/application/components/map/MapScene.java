package simple.marauroa.application.components.map;

import java.util.List;
import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.painter.Painter;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ComponentWidget;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Charles Edward Bedon Cortazar <charles.bedon@kuwaiba.org>
 */
public final class MapScene extends GraphScene<String, String> {

    /**
     * The widget to embed the map
     */
    private ComponentWidget mapWidget;
    private LayerWidget mapLayer;
    private List<LayerWidget> layers;
    private int defaultZoom = 1;
    /**
     * Map center coordinate
     */
    private final GeoPosition mapCenter = new GeoPosition(2.441466, -76.609619);
    /**
     * The map component
     */
    private JXMapKit myMap;

    public MapScene() {
        //The layers
        mapLayer = new LayerWidget(this);
        addChild(mapLayer);
        /**
         * 
         * The map. We use JXMapKit instead of JXMapViewer because the kit
         * includes the zoom slider, a satellite view and and a JXMapViewer
         */
        myMap = new JXMapKit();
        myMap.setDefaultProvider(JXMapKit.DefaultProviders.OpenStreetMaps);
        if (mapCenter != null) {
            myMap.setCenterPosition(mapCenter);
        }
        if (defaultZoom > 0) {
            myMap.getMainMap().setZoom(defaultZoom);
        }
        mapWidget = new ComponentWidget(this, myMap);
        mapLayer.addChild(mapWidget);
    }

    public void refresh() {
        //If this is not done, an IllegalStateException will be raised when repositioning the widgets
        for (LayerWidget layer : layers) {
            layer.bringToFront();
        }
    }

    public void setPainter(Painter painter) {
        myMap.getMainMap().setOverlayPainter(painter);
        refresh();
    }

    public void setTileFactory(TileFactory factory) {
        myMap.setTileFactory(factory);
        refresh();
    }

    public void setMapCenter(GeoPosition mapCenter) {
        myMap.setCenterPosition(mapCenter);
        refresh();
    }

    @Override
    protected Widget attachNodeWidget(String node) {
        ConnectionWidget myEdge = null;
        if (layers.size() >= 1) {
            myEdge = new ConnectionWidget(this);
            layers.get(0).addChild(myEdge);
        }
        return myEdge;
    }

    @Override
    protected Widget attachEdgeWidget(String edge) {
        ConnectionWidget myEdge = null;
        if (layers.size() >= 2) {
            myEdge = new ConnectionWidget(this);
            layers.get(1).addChild(myEdge);
        }
        return myEdge;
    }

    @Override
    protected void attachEdgeSourceAnchor(String edge, String oldSourceNode, String sourceNode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void attachEdgeTargetAnchor(String edge, String oldTargetNode, String targetNode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Used to update the map widget bounds to match the container size since
     * this is not possible to do it using a layout
     */
    protected void updateBounds() {
        mapWidget.setPreferredSize(this.getBounds().getSize());
    }

    /**
     * @param defaultZoom the defaultZoom to set
     */
    public void setDefaultZoom(int defaultZoom) {
        this.defaultZoom = defaultZoom;
    }

    /**
     * @return the defaultZoom
     */
    public int getDefaultZoom() {
        return defaultZoom;
    }
}
