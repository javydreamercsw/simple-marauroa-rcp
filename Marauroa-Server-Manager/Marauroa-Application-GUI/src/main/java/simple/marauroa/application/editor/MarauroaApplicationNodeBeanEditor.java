package simple.marauroa.application.editor;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import simple.marauroa.application.gui.MarauroaApplicationNode;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class MarauroaApplicationNodeBeanEditor implements PropertyEditor, 
        ExPropertyEditor {

    private MarauroaApplicationNode value;

    @Override
    public void setValue(Object value) {
        this.value = (MarauroaApplicationNode) value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean isPaintable() {
        return false;
    }

    @Override
    public void paintValue(Graphics gfx, Rectangle box) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getJavaInitializationString() {
        return "new MarauroaApplication()";
    }

    @Override
    public String getAsText() {
        return value.getMarauroaApplication().toStringForDisplay();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] getTags() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Component getCustomEditor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean supportsCustomEditor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void attachEnv(PropertyEnv env) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
