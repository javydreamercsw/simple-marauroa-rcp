package simple.marauroa.client.components.userlist;

import java.awt.Image;
import java.net.MalformedURLException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Exceptions;
import simple.marauroa.application.core.tool.Tool;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class ClientRootNode extends AbstractNode {

    private ClientRPObjectChildFactory childFactory;

    /**
     * Creates a new instance of RootNode
     */
    public ClientRootNode(ClientRPObjectChildFactory childFactory) {
        super(Children.create(childFactory, true));
        this.childFactory = childFactory;
    }
    
    @Override
    public Image getIcon(int type) {
        try {
            return Tool.createImage(
                        "simple.marauroa.application.gui",
                        "resources/images/right.png",
                        "Closed icon");
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    @Override
    public Image getOpenedIcon(int type) {
        try {
            return Tool.createImage(
                        "simple.marauroa.application.gui",
                        "resources/images/down.png",
                        "Open icon");
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    public void refresh() {
        this.childFactory.refresh();
    }
}
