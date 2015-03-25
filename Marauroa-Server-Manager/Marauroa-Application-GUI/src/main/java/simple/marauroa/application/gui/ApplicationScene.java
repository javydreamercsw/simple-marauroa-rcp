package simple.marauroa.application.gui;

import java.awt.Image;
import java.awt.Point;
import java.net.MalformedURLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import marauroa.common.game.IRPZone;
import marauroa.common.game.IRPZone.ID;
import marauroa.common.game.RPObject;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorFactory.DirectionalAnchorKind;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import simple.marauroa.application.api.IDataBase;
import simple.marauroa.application.api.IDiagramManager;
import simple.marauroa.application.api.IMarauroaApplication;
import simple.marauroa.application.core.MarauroaApplicationRepository;
import simple.marauroa.application.core.tool.Tool;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = IDiagramManager.class)
public class ApplicationScene extends GraphScene implements IDiagramManager {

    private ApplicationScene instance;
    private LayerWidget mainLayer;
    private LayerWidget connectionLayer;
    private LayerWidget interactionLayer;
    private ArrayList<LayerWidget> layersToClear = new ArrayList<LayerWidget>();
    private HashMap<String, Widget> widgets = new HashMap<String, Widget>();
    public final String SERVER = "Server", ZONE = "Zone", OBJECT = "Object";
    private Image serverImg;
    private Image zoneImg;
    private Image objectImg;
    private WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditor());
    private final HashMap<String, Image> mapping;

    {
        mapping = new HashMap<String, Image>();
        try {
            serverImg = ImageUtilities.icon2Image(
                    new ImageIcon(Tool.createImage(
                                    "simple.marauroa.application.gui",
                                    "resources/images/arianne.png", "Arianne icon")));
            zoneImg = ImageUtilities.icon2Image(
                    new ImageIcon(Tool.createImage(
                                    "simple.marauroa.application.gui",
                                    "resources/images/zone.png", "Zone icon")));
            objectImg = ImageUtilities.icon2Image(
                    new ImageIcon(Tool.createImage(
                                    "simple.marauroa.application.gui",
                                    "resources/images/entity.png", "Entity icon")));
            mapping.put(SERVER, serverImg);
            mapping.put(ZONE, zoneImg);
            mapping.put(OBJECT, objectImg);
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public GraphScene get() {
        if (instance == null) {
            instance = new ApplicationScene();
        }
        return instance;
    }

    public ApplicationScene() {
        mainLayer = new LayerWidget(this);
        layersToClear.add(mainLayer);
        addChild(mainLayer);
        connectionLayer = new LayerWidget(this);
        layersToClear.add(connectionLayer);
        addChild(connectionLayer);
        interactionLayer = new LayerWidget(this);
        addChild(interactionLayer);
        layersToClear.add(interactionLayer);
        //Enable zoom
        getActions().addAction(ActionFactory.createZoomAction());
        //Enable pan
        getActions().addAction(ActionFactory.createPanAction());
        //Enable wheel pan
        getActions().addAction(ActionFactory.createWheelPanAction());
        validate();
    }

    public void addMarauroaApplication(IMarauroaApplication application) {
        Logger.getLogger(ApplicationScene.class.getSimpleName()).fine(
                NbBundle.getMessage(ApplicationScene.class,
                        "Adding _0_ to the scene.",
                        new Object[]{String.valueOf(application.getName())}));
        widgets.put(application.getName(), attachNodeWidget(application));
    }

    public Widget getWidget(String appName) {
        return widgets.get(appName);
    }

    public Collection<Widget> getWidgets() {
        return widgets.values();
    }

    public void clear() {
        for (LayerWidget lw : layersToClear) {
            lw.removeChildren();
        }
        widgets.clear();
        validate();
    }

    @Override
    protected Widget attachNodeWidget(Object node) {
        Image image = mapping.get(node);
        if (image != null) {
            return createNewApplicationWidget(node, image);
        } else {
            //Use default image
            return createNewApplicationWidget(node, mapping.get(SERVER));
        }
    }

    @Override
    protected Widget attachEdgeWidget(Object edge) {
        return null;
    }

    @Override
    protected void attachEdgeSourceAnchor(Object edge, Object oldSourceNode, Object sourceNode) {
    }

    @Override
    protected void attachEdgeTargetAnchor(Object edge, Object oldTargetNode, Object targetNode) {
    }

    private class LabelTextFieldEditor implements TextFieldInplaceEditor {

        @Override
        public boolean isEnabled(Widget widget) {
            return true;
        }

        @Override
        public String getText(Widget widget) {
            return ((LabelWidget) widget).getLabel();
        }

        @Override
        public void setText(Widget widget, String text) {
            ((LabelWidget) widget).setLabel(text);
        }
    }

    protected Widget createNewApplicationWidget(Object label, Image image) {
        boolean addZones = false;
        IconNodeWidget widget = new IconNodeWidget(this);
        widget.setImage(image);
        widget.setPreferredLocation(new Point(10, 20));
        if (label instanceof String) {
            widget.setLabel(label.toString());
        } else if (label instanceof IMarauroaApplication) {
            widget.setLabel(((IMarauroaApplication) label).getName());
            addZones = true;
        }
        widget.getLabelWidget().getActions().addAction(editorAction);
        widget.getActions().addAction(ActionFactory.createMoveAction());
        mainLayer.addChild(widget);
        if (addZones) {
            //Add zones
            addZones(widget, ((IMarauroaApplication) label).getName());
        }
        //------------
        addWidgetActions(mainLayer, connectionLayer);
        validate();
        return widget;
    }

    public void redrawApplication(IMarauroaApplication app) {
        if (widgets.containsKey(app.getName())) {
            Widget widget = getWidget(app.getName());
            //Clear all children to be redrawn
            widget.removeChildren();
            //Clear the connection layer
            connectionLayer.removeChildren();
            addZones(widget, app.getName());
        } else {
            //Is not there, just add it
            Logger.getLogger(ApplicationScene.class.getSimpleName()).log(Level.WARNING,
                    "Got a request to redraw a non existent application {0}."
                    + " Will add it instead.", app.getName());
            addMarauroaApplication(app);
        }
    }

    private void addZones(Widget widget, String appName) {
        int counter = 1;
        for (IRPZone zone : MarauroaApplicationRepository.getZonesForApplication(
                Lookup.getDefault().lookup(IDataBase.class).getApplication(appName))) {
            IconNodeWidget zoneW = new IconNodeWidget(this);
            zoneW.setImage(mapping.get(ZONE));
            zoneW.setPreferredLocation(new Point(widget.getPreferredLocation().x,
                    widget.getPreferredLocation().y + 100 * counter));
            zoneW.setLabel(zone.getID().toString());
            zoneW.getActions().addAction(ActionFactory.createMoveAction());
            addChildWidget(widget, zoneW);
            addRPObjects(zoneW, appName, zone.getID());
            counter++;
        }
        validate();
    }

    private void addRPObjects(Widget widget, String appName, ID zoneId) {
        int counter = 1;
        for (RPObject object : MarauroaApplicationRepository.getRPObjectsForZone(appName, zoneId)) {
            IconNodeWidget objectW = new IconNodeWidget(this);
            objectW.setImage(mapping.get(OBJECT));
            objectW.setPreferredLocation(new Point(widget.getPreferredLocation().x,
                    widget.getPreferredLocation().y + 100 * counter));
            objectW.setLabel(object.get("name"));
            objectW.getActions().addAction(ActionFactory.createMoveAction());
            addChildWidget(widget, objectW);
            counter++;
        }
        validate();
    }

    private void addChildWidget(Widget parent, Widget child) {
        mainLayer.addChild(child);
        Logger.getLogger(ApplicationScene.class.getSimpleName()).log(Level.WARNING,
                "Parent has now {0} children.", parent.getChildren().size());
        //Now link them
        ConnectionWidget conw = new ConnectionWidget(this);
        setSource(conw, parent, ANCHOR_TYPE.RECTANGULAR);
        setTarget(conw, child, ANCHOR_TYPE.CENTER);
        conw.setSourceAnchorShape(AnchorShape.NONE);
        conw.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
        connectionLayer.addChild(conw);
        validate();
    }

    private void setSource(ConnectionWidget conw, Widget source, ANCHOR_TYPE type) {
        switch (type) {
            case CENTER:
                conw.setSourceAnchor(AnchorFactory.createCenterAnchor(source));
                break;
            case CIRCULAR:
                conw.setSourceAnchor(AnchorFactory.createCircularAnchor(source, 5));
                break;
            case DIRECTIONAL:
                conw.setSourceAnchor(AnchorFactory.createDirectionalAnchor(source,
                        DirectionalAnchorKind.HORIZONTAL));
                break;
            case RECTANGULAR:
                conw.setSourceAnchor(AnchorFactory.createRectangularAnchor(source));
                break;
            default:
                Logger.getLogger(ApplicationScene.class.getSimpleName()).log(Level.WARNING,
                        "Unexpected anchor: {0}", type.name());
        }
    }

    private void setTarget(ConnectionWidget conw, Widget target, ANCHOR_TYPE type) {
        switch (type) {
            case CENTER:
                conw.setTargetAnchor(AnchorFactory.createCenterAnchor(target));
                break;
            case CIRCULAR:
                conw.setTargetAnchor(AnchorFactory.createCircularAnchor(target, 5));
                break;
            case DIRECTIONAL:
                conw.setTargetAnchor(AnchorFactory.createDirectionalAnchor(target,
                        DirectionalAnchorKind.HORIZONTAL));
                break;
            case RECTANGULAR:
                conw.setTargetAnchor(AnchorFactory.createRectangularAnchor(target));
                break;
            default:
                Logger.getLogger(ApplicationScene.class.getSimpleName()).log(Level.WARNING,
                        "Unexpected anchor: {0}", type.name());
        }
    }

    public static void addWidgetActions(LayerWidget mainLayer, LayerWidget connectionLayer) {
        List<Widget> children = mainLayer.getChildren();
        for (Widget widget : children) {
            // the order is important to not consume events
            widget.getActions().addAction(ActionFactory.createSelectAction(
                    new SelProvider()));
            widget.getActions().addAction(ActionFactory.createMoveAction());
            widget.getActions().addAction(ActionFactory.createResizeAction());
        }
    }

    private static class SelProvider implements SelectProvider {

        @Override
        public boolean isAimingAllowed(Widget widget, Point localLocation,
                boolean invertSelection) {
            Logger.getLogger(ApplicationScene.class.getSimpleName()).log(Level.INFO,
                    "sel.isAimingAllowed {0}", localLocation);
            return false;
        }

        @Override
        public boolean isSelectionAllowed(Widget widget, Point localLocation,
                boolean invertSelection) {
            Logger.getLogger(ApplicationScene.class.getSimpleName()).log(Level.INFO,
                    "sel.isSelectionAllowed {0}", localLocation);
            return true;
        }

        @Override
        public void select(Widget widget, Point localLocation,
                boolean invertSelection) {

            Logger.getLogger(ApplicationScene.class.getSimpleName()).log(Level.INFO,
                    "sel.select {0}", localLocation);
            widget.getScene().setFocusedWidget(widget);
        }
    }

    /*
     * Update the diagram
     */
    @Override
    public void update(IMarauroaApplication app) {
        redrawApplication(app);
    }
}
