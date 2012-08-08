/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.marauroa.application.components.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.painter.Painter;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ComponentWidget;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Charles Edward Bedon Cortazar <charles.bedon@kuwaiba.org>
 */
public final class MapScene extends GraphScene<String, String> {

    /**
     * The widget to embed the map
     */
    private ComponentWidget mapWidget;
    /**
     * Icon size
     */
    private final int ICON_RADIUS = 8;
    /**
     * Sample endpoint coordinates
     */
    private final GeoPosition endpointA = new GeoPosition(2.440372, -76.612151);
    private final GeoPosition endpointB = new GeoPosition(2.441895, -76.60566);
    private LayerWidget mapLayer;
    private LayerWidget nodesLayer;
    private LayerWidget routesLayer;
    /**
     * Sample control points
     */
    private final GeoPosition[] controPoints = new GeoPosition[]{
        new GeoPosition(2.439633, -76.609619),
        new GeoPosition(2.442977, -76.608353)};
    /**
     * Map center coordinate
     */
    private final GeoPosition mapCenter = new GeoPosition(2.441466, -76.609619);
    /**
     * Path to the icon to be used to mark the waypoints
     */
    public static final String GENERIC_ICON_PATH = "org/neotropic/tests/gis/res/default.png";
    /**
     * The map component
     */
    private JXMapKit myMap;
    /**
     * Endpoint widgets
     */
    private IconNodeWidget icnw1;
    private IconNodeWidget icnw2;
    /**
     * Connection widget
     */
    private ConnectionWidget conWidget;

    public MapScene() {
        //The layers
        mapLayer = new LayerWidget(this);
        routesLayer = new LayerWidget(this);
        nodesLayer = new LayerWidget(this);
        addChild(mapLayer);
        addChild(routesLayer);
        addChild(nodesLayer);

        //The map. We use JXMapKit instead of JXMapViewer because the kit includes the zoom slider, a satellite view and
        //and a JXMapViewer
        myMap = new JXMapKit();
        myMap.setDefaultProvider(JXMapKit.DefaultProviders.OpenStreetMaps);
        myMap.setCenterPosition(mapCenter);
        myMap.getMainMap().setZoom(2);
        mapWidget = new ComponentWidget(this, myMap);
        mapLayer.addChild(mapWidget);

        //The nodes
        icnw1 = (IconNodeWidget) addNode("widget1");
        icnw2 = (IconNodeWidget) addNode("widget2");
        icnw1.setImage(ImageUtilities.loadImage(GENERIC_ICON_PATH));
        icnw2.setImage(ImageUtilities.loadImage(GENERIC_ICON_PATH));

        //The route
        conWidget = (ConnectionWidget) addEdge("con1");
        conWidget.setControlPointShape(PointShape.SQUARE_FILLED_BIG);
        conWidget.setLineColor(Color.RED);
        conWidget.setStroke(new BasicStroke(2));

        setPainter(new Painter() {
            @Override
            public void paint(Graphics2D gd, Object t, int i, int i1) {
                Rectangle realViewport = myMap.getMainMap().getViewportBounds();
                Point2D endpointA2D = myMap.getMainMap().getTileFactory().geoToPixel(endpointA, myMap.getMainMap().getZoom());
                Point2D endpointB2D = myMap.getMainMap().getTileFactory().geoToPixel(endpointB, myMap.getMainMap().getZoom());
                icnw1.setPreferredLocation(new Point((int) endpointA2D.getX() - realViewport.x - ICON_RADIUS, (int) endpointA2D.getY() - realViewport.y - ICON_RADIUS));
                icnw2.setPreferredLocation(new Point((int) endpointB2D.getX() - realViewport.x - ICON_RADIUS, (int) endpointB2D.getY() - realViewport.y - ICON_RADIUS));
                List<Point> controlPoints2D = new ArrayList<Point>();
                controlPoints2D.add(new Point((int) endpointA2D.getX() - realViewport.x, (int) endpointA2D.getY() - realViewport.y));
                for (GeoPosition controlPoint : controPoints) {
                    Point2D controlPoint2D = myMap.getMainMap().getTileFactory().geoToPixel(controlPoint, myMap.getMainMap().getZoom());
                    controlPoints2D.add(new Point((int) controlPoint2D.getX() - realViewport.x, (int) controlPoint2D.getY() - realViewport.y));
                }

                controlPoints2D.add(new Point((int) endpointB2D.getX() - realViewport.x, (int) endpointB2D.getY() - realViewport.y));

                //For some reason, it's necessary to set the anchors everytime. It's a rendering issue
                setAnchors();
                conWidget.setControlPoints(controlPoints2D, true);
                validate();
            }
        });

        //If this is not done, an IllegalStateException will be raised when reositioning the conection widget
        routesLayer.bringToFront();
        nodesLayer.bringToFront();
    }
    
    public void setPainter(Painter painter){
        myMap.getMainMap().setOverlayPainter(painter);
        updateBounds();
        repaint();
    }

    public void setTileFactory(TileFactory factory) {
        myMap.setTileFactory(factory);
        updateBounds();
        repaint();
    }
    
    public void setMapCenter(GeoPosition mapCenter){
        myMap.setCenterPosition(mapCenter);
        updateBounds();
        repaint();
    }

    @Override
    protected Widget attachNodeWidget(String node) {
        IconNodeWidget myNode = new IconNodeWidget(this);
        nodesLayer.addChild(myNode);
        return myNode;
    }

    @Override
    protected Widget attachEdgeWidget(String edge) {
        ConnectionWidget myEdge = new ConnectionWidget(this);
        routesLayer.addChild(myEdge);
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

    protected void setAnchors() {
        conWidget.setSourceAnchor(AnchorFactory.createFreeRectangularAnchor(icnw1, true));
        conWidget.setTargetAnchor(AnchorFactory.createFreeRectangularAnchor(icnw2, true));
    }
}
