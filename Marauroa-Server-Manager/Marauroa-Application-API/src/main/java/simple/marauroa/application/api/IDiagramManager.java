package simple.marauroa.application.api;

import org.netbeans.api.visual.graph.GraphScene;

/**
 * Interface for common operations on the graphical display
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public interface IDiagramManager {
    /*
     * Update the diagram
     */
    public void update(IMarauroaApplication app);
    
    public GraphScene get();
}
