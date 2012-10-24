package simple.marauroa.application.gui;

import java.beans.*;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class RPZoneNodeBeanInfo extends SimpleBeanInfo {

    // Bean descriptor//GEN-FIRST:BeanDescriptor
    /*lazy BeanDescriptor*/
    private static BeanDescriptor getBdescriptor(){
        BeanDescriptor beanDescriptor = new BeanDescriptor  ( simple.marauroa.application.gui.RPZoneNode.class , null ); // NOI18N//GEN-HEADEREND:BeanDescriptor

        // Here you can add code for customizing the BeanDescriptor.

        return beanDescriptor;     }//GEN-LAST:BeanDescriptor
    // Property identifiers//GEN-FIRST:Properties
    private static final int PROPERTY_actions = 0;
    private static final int PROPERTY_children = 1;
    private static final int PROPERTY_contextActions = 2;
    private static final int PROPERTY_contextMenu = 3;
    private static final int PROPERTY_customizer = 4;
    private static final int PROPERTY_defaultAction = 5;
    private static final int PROPERTY_displayName = 6;
    private static final int PROPERTY_expert = 7;
    private static final int PROPERTY_handle = 8;
    private static final int PROPERTY_helpCtx = 9;
    private static final int PROPERTY_hidden = 10;
    private static final int PROPERTY_htmlDisplayName = 11;
    private static final int PROPERTY_icon = 12;
    private static final int PROPERTY_iconBase = 13;
    private static final int PROPERTY_iconBaseWithExtension = 14;
    private static final int PROPERTY_leaf = 15;
    private static final int PROPERTY_lookup = 16;
    private static final int PROPERTY_name = 17;
    private static final int PROPERTY_newTypes = 18;
    private static final int PROPERTY_openedIcon = 19;
    private static final int PROPERTY_parentNode = 20;
    private static final int PROPERTY_preferred = 21;
    private static final int PROPERTY_preferredAction = 22;
    private static final int PROPERTY_propertySets = 23;
    private static final int PROPERTY_shortDescription = 24;

    // Property array 
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[25];
    
        try {
            properties[PROPERTY_actions] = new PropertyDescriptor ( "actions", simple.marauroa.application.gui.RPZoneNode.class, "getActions", null ); // NOI18N
            properties[PROPERTY_children] = new PropertyDescriptor ( "children", simple.marauroa.application.gui.RPZoneNode.class, "getChildren", null ); // NOI18N
            properties[PROPERTY_contextActions] = new PropertyDescriptor ( "contextActions", simple.marauroa.application.gui.RPZoneNode.class, "getContextActions", null ); // NOI18N
            properties[PROPERTY_contextMenu] = new PropertyDescriptor ( "contextMenu", simple.marauroa.application.gui.RPZoneNode.class, "getContextMenu", null ); // NOI18N
            properties[PROPERTY_customizer] = new PropertyDescriptor ( "customizer", simple.marauroa.application.gui.RPZoneNode.class, "getCustomizer", null ); // NOI18N
            properties[PROPERTY_defaultAction] = new PropertyDescriptor ( "defaultAction", simple.marauroa.application.gui.RPZoneNode.class, "getDefaultAction", "setDefaultAction" ); // NOI18N
            properties[PROPERTY_displayName] = new PropertyDescriptor ( "displayName", simple.marauroa.application.gui.RPZoneNode.class, "getDisplayName", "setDisplayName" ); // NOI18N
            properties[PROPERTY_expert] = new PropertyDescriptor ( "expert", simple.marauroa.application.gui.RPZoneNode.class, "isExpert", "setExpert" ); // NOI18N
            properties[PROPERTY_handle] = new PropertyDescriptor ( "handle", simple.marauroa.application.gui.RPZoneNode.class, "getHandle", null ); // NOI18N
            properties[PROPERTY_helpCtx] = new PropertyDescriptor ( "helpCtx", simple.marauroa.application.gui.RPZoneNode.class, "getHelpCtx", null ); // NOI18N
            properties[PROPERTY_hidden] = new PropertyDescriptor ( "hidden", simple.marauroa.application.gui.RPZoneNode.class, "isHidden", "setHidden" ); // NOI18N
            properties[PROPERTY_htmlDisplayName] = new PropertyDescriptor ( "htmlDisplayName", simple.marauroa.application.gui.RPZoneNode.class, "getHtmlDisplayName", null ); // NOI18N
            properties[PROPERTY_icon] = new IndexedPropertyDescriptor ( "icon", simple.marauroa.application.gui.RPZoneNode.class, null, null, "getIcon", null ); // NOI18N
            properties[PROPERTY_iconBase] = new PropertyDescriptor ( "iconBase", simple.marauroa.application.gui.RPZoneNode.class, null, "setIconBase" ); // NOI18N
            properties[PROPERTY_iconBaseWithExtension] = new PropertyDescriptor ( "iconBaseWithExtension", simple.marauroa.application.gui.RPZoneNode.class, null, "setIconBaseWithExtension" ); // NOI18N
            properties[PROPERTY_leaf] = new PropertyDescriptor ( "leaf", simple.marauroa.application.gui.RPZoneNode.class, "isLeaf", null ); // NOI18N
            properties[PROPERTY_lookup] = new PropertyDescriptor ( "lookup", simple.marauroa.application.gui.RPZoneNode.class, "getLookup", null ); // NOI18N
            properties[PROPERTY_name] = new PropertyDescriptor ( "name", simple.marauroa.application.gui.RPZoneNode.class, "getName", "setName" ); // NOI18N
            properties[PROPERTY_newTypes] = new PropertyDescriptor ( "newTypes", simple.marauroa.application.gui.RPZoneNode.class, "getNewTypes", null ); // NOI18N
            properties[PROPERTY_openedIcon] = new IndexedPropertyDescriptor ( "openedIcon", simple.marauroa.application.gui.RPZoneNode.class, null, null, "getOpenedIcon", null ); // NOI18N
            properties[PROPERTY_parentNode] = new PropertyDescriptor ( "parentNode", simple.marauroa.application.gui.RPZoneNode.class, "getParentNode", null ); // NOI18N
            properties[PROPERTY_preferred] = new PropertyDescriptor ( "preferred", simple.marauroa.application.gui.RPZoneNode.class, "isPreferred", "setPreferred" ); // NOI18N
            properties[PROPERTY_preferredAction] = new PropertyDescriptor ( "preferredAction", simple.marauroa.application.gui.RPZoneNode.class, "getPreferredAction", null ); // NOI18N
            properties[PROPERTY_propertySets] = new PropertyDescriptor ( "propertySets", simple.marauroa.application.gui.RPZoneNode.class, "getPropertySets", null ); // NOI18N
            properties[PROPERTY_shortDescription] = new PropertyDescriptor ( "shortDescription", simple.marauroa.application.gui.RPZoneNode.class, "getShortDescription", "setShortDescription" ); // NOI18N
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Properties

        // Here you can add code for customizing the properties array.

        return properties;     }//GEN-LAST:Properties
    // EventSet identifiers//GEN-FIRST:Events
    private static final int EVENT_nodeListener = 0;
    private static final int EVENT_propertyChangeListener = 1;

    // EventSet array
    /*lazy EventSetDescriptor*/
    private static EventSetDescriptor[] getEdescriptor(){
        EventSetDescriptor[] eventSets = new EventSetDescriptor[2];
    
        try {
            eventSets[EVENT_nodeListener] = new EventSetDescriptor ( simple.marauroa.application.gui.RPZoneNode.class, "nodeListener", org.openide.nodes.NodeListener.class, new String[] {"childrenAdded", "childrenRemoved", "childrenReordered", "nodeDestroyed"}, "addNodeListener", "removeNodeListener" ); // NOI18N
            eventSets[EVENT_propertyChangeListener] = new EventSetDescriptor ( simple.marauroa.application.gui.RPZoneNode.class, "propertyChangeListener", java.beans.PropertyChangeListener.class, new String[] {"propertyChange"}, "addPropertyChangeListener", "removePropertyChangeListener" ); // NOI18N
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Events

        // Here you can add code for customizing the event sets array.

        return eventSets;     }//GEN-LAST:Events
    // Method identifiers//GEN-FIRST:Methods
    private static final int METHOD_attributeNames0 = 0;
    private static final int METHOD_canCopy1 = 1;
    private static final int METHOD_canCut2 = 2;
    private static final int METHOD_canDestroy3 = 3;
    private static final int METHOD_canRename4 = 4;
    private static final int METHOD_clipboardCopy5 = 5;
    private static final int METHOD_clipboardCut6 = 6;
    private static final int METHOD_cloneNode7 = 7;
    private static final int METHOD_computeProperties8 = 8;
    private static final int METHOD_destroy9 = 9;
    private static final int METHOD_drag10 = 10;
    private static final int METHOD_equals11 = 11;
    private static final int METHOD_getActions12 = 12;
    private static final int METHOD_getCookie13 = 13;
    private static final int METHOD_getDropType14 = 14;
    private static final int METHOD_getPasteTypes15 = 15;
    private static final int METHOD_getValue16 = 16;
    private static final int METHOD_hasCustomizer17 = 17;
    private static final int METHOD_hashCode18 = 18;
    private static final int METHOD_setValue19 = 19;
    private static final int METHOD_toString20 = 20;

    // Method array 
    /*lazy MethodDescriptor*/
    private static MethodDescriptor[] getMdescriptor(){
        MethodDescriptor[] methods = new MethodDescriptor[21];
    
        try {
            methods[METHOD_attributeNames0] = new MethodDescriptor(java.beans.FeatureDescriptor.class.getMethod("attributeNames", new Class[] {})); // NOI18N
            methods[METHOD_attributeNames0].setDisplayName ( "" );
            methods[METHOD_canCopy1] = new MethodDescriptor(org.openide.nodes.BeanNode.class.getMethod("canCopy", new Class[] {})); // NOI18N
            methods[METHOD_canCopy1].setDisplayName ( "" );
            methods[METHOD_canCut2] = new MethodDescriptor(org.openide.nodes.BeanNode.class.getMethod("canCut", new Class[] {})); // NOI18N
            methods[METHOD_canCut2].setDisplayName ( "" );
            methods[METHOD_canDestroy3] = new MethodDescriptor(org.openide.nodes.BeanNode.class.getMethod("canDestroy", new Class[] {})); // NOI18N
            methods[METHOD_canDestroy3].setDisplayName ( "" );
            methods[METHOD_canRename4] = new MethodDescriptor(org.openide.nodes.BeanNode.class.getMethod("canRename", new Class[] {})); // NOI18N
            methods[METHOD_canRename4].setDisplayName ( "" );
            methods[METHOD_clipboardCopy5] = new MethodDescriptor(org.openide.nodes.AbstractNode.class.getMethod("clipboardCopy", new Class[] {})); // NOI18N
            methods[METHOD_clipboardCopy5].setDisplayName ( "" );
            methods[METHOD_clipboardCut6] = new MethodDescriptor(org.openide.nodes.AbstractNode.class.getMethod("clipboardCut", new Class[] {})); // NOI18N
            methods[METHOD_clipboardCut6].setDisplayName ( "" );
            methods[METHOD_cloneNode7] = new MethodDescriptor(org.openide.nodes.AbstractNode.class.getMethod("cloneNode", new Class[] {})); // NOI18N
            methods[METHOD_cloneNode7].setDisplayName ( "" );
            methods[METHOD_computeProperties8] = new MethodDescriptor(org.openide.nodes.BeanNode.class.getMethod("computeProperties", new Class[] {java.lang.Object.class, java.beans.BeanInfo.class})); // NOI18N
            methods[METHOD_computeProperties8].setDisplayName ( "" );
            methods[METHOD_destroy9] = new MethodDescriptor(org.openide.nodes.BeanNode.class.getMethod("destroy", new Class[] {})); // NOI18N
            methods[METHOD_destroy9].setDisplayName ( "" );
            methods[METHOD_drag10] = new MethodDescriptor(org.openide.nodes.AbstractNode.class.getMethod("drag", new Class[] {})); // NOI18N
            methods[METHOD_drag10].setDisplayName ( "" );
            methods[METHOD_equals11] = new MethodDescriptor(org.openide.nodes.Node.class.getMethod("equals", new Class[] {java.lang.Object.class})); // NOI18N
            methods[METHOD_equals11].setDisplayName ( "" );
            methods[METHOD_getActions12] = new MethodDescriptor(org.openide.nodes.BeanNode.class.getMethod("getActions", new Class[] {boolean.class})); // NOI18N
            methods[METHOD_getActions12].setDisplayName ( "" );
            methods[METHOD_getCookie13] = new MethodDescriptor(org.openide.nodes.AbstractNode.class.getMethod("getCookie", new Class[] {java.lang.Class.class})); // NOI18N
            methods[METHOD_getCookie13].setDisplayName ( "" );
            methods[METHOD_getDropType14] = new MethodDescriptor(org.openide.nodes.AbstractNode.class.getMethod("getDropType", new Class[] {java.awt.datatransfer.Transferable.class, int.class, int.class})); // NOI18N
            methods[METHOD_getDropType14].setDisplayName ( "" );
            methods[METHOD_getPasteTypes15] = new MethodDescriptor(org.openide.nodes.AbstractNode.class.getMethod("getPasteTypes", new Class[] {java.awt.datatransfer.Transferable.class})); // NOI18N
            methods[METHOD_getPasteTypes15].setDisplayName ( "" );
            methods[METHOD_getValue16] = new MethodDescriptor(java.beans.FeatureDescriptor.class.getMethod("getValue", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_getValue16].setDisplayName ( "" );
            methods[METHOD_hasCustomizer17] = new MethodDescriptor(org.openide.nodes.BeanNode.class.getMethod("hasCustomizer", new Class[] {})); // NOI18N
            methods[METHOD_hasCustomizer17].setDisplayName ( "" );
            methods[METHOD_hashCode18] = new MethodDescriptor(org.openide.nodes.Node.class.getMethod("hashCode", new Class[] {})); // NOI18N
            methods[METHOD_hashCode18].setDisplayName ( "" );
            methods[METHOD_setValue19] = new MethodDescriptor(java.beans.FeatureDescriptor.class.getMethod("setValue", new Class[] {java.lang.String.class, java.lang.Object.class})); // NOI18N
            methods[METHOD_setValue19].setDisplayName ( "" );
            methods[METHOD_toString20] = new MethodDescriptor(org.openide.nodes.Node.class.getMethod("toString", new Class[] {})); // NOI18N
            methods[METHOD_toString20].setDisplayName ( "" );
        }
        catch( Exception e) {}//GEN-HEADEREND:Methods

        // Here you can add code for customizing the methods array.
        
        return methods;     }//GEN-LAST:Methods
    private static java.awt.Image iconColor16 = null;//GEN-BEGIN:IconsDef
    private static java.awt.Image iconColor32 = null;
    private static java.awt.Image iconMono16 = null;
    private static java.awt.Image iconMono32 = null;//GEN-END:IconsDef
    private static String iconNameC16 = null;//GEN-BEGIN:Icons
    private static String iconNameC32 = null;
    private static String iconNameM16 = null;
    private static String iconNameM32 = null;//GEN-END:Icons
    private static final int defaultPropertyIndex = -1;//GEN-BEGIN:Idx
    private static final int defaultEventIndex = -1;//GEN-END:Idx

//GEN-FIRST:Superclass
    // Here you can add code for customizing the Superclass BeanInfo.
//GEN-LAST:Superclass
    /**
     * Gets the bean's <code>BeanDescriptor</code>s.
     * 
     * @return BeanDescriptor describing the editable
     * properties of this bean.  May return null if the
     * information should be obtained by automatic analysis.
     */
    @Override
    public BeanDescriptor getBeanDescriptor() {
        return getBdescriptor();
    }

    /**
     * Gets the bean's <code>PropertyDescriptor</code>s.
     * 
     * @return An array of PropertyDescriptors describing the editable
     * properties supported by this bean.  May return null if the
     * information should be obtained by automatic analysis.
     * <p>
     * If a property is indexed, then its entry in the result array will
     * belong to the IndexedPropertyDescriptor subclass of PropertyDescriptor.
     * A client of getPropertyDescriptors can use "instanceof" to check
     * if a given PropertyDescriptor is an IndexedPropertyDescriptor.
     */
    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return getPdescriptor();
    }

    /**
     * Gets the bean's <code>EventSetDescriptor</code>s.
     * 
     * @return  An array of EventSetDescriptors describing the kinds of 
     * events fired by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    @Override
    public EventSetDescriptor[] getEventSetDescriptors() {
        return getEdescriptor();
    }

    /**
     * Gets the bean's <code>MethodDescriptor</code>s.
     * 
     * @return  An array of MethodDescriptors describing the methods 
     * implemented by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    @Override
    public MethodDescriptor[] getMethodDescriptors() {
        return getMdescriptor();
    }

    /**
     * A bean may have a "default" property that is the property that will
     * mostly commonly be initially chosen for update by human's who are 
     * customizing the bean.
     * @return  Index of default property in the PropertyDescriptor array
     * 		returned by getPropertyDescriptors.
     * <P>	Returns -1 if there is no default property.
     */
    @Override
    public int getDefaultPropertyIndex() {
        return defaultPropertyIndex;
    }

    /**
     * A bean may have a "default" event that is the event that will
     * mostly commonly be used by human's when using the bean. 
     * @return Index of default event in the EventSetDescriptor array
     *		returned by getEventSetDescriptors.
     * <P>	Returns -1 if there is no default event.
     */
    @Override
    public int getDefaultEventIndex() {
        return defaultEventIndex;
    }

    /**
     * This method returns an image object that can be used to
     * represent the bean in toolboxes, toolbars, etc.   Icon images
     * will typically be GIFs, but may in future include other formats.
     * <p>
     * Beans aren't required to provide icons and may return null from
     * this method.
     * <p>
     * There are four possible flavors of icons (16x16 color,
     * 32x32 color, 16x16 mono, 32x32 mono).  If a bean choses to only
     * support a single icon we recommend supporting 16x16 color.
     * <p>
     * We recommend that icons have a "transparent" background
     * so they can be rendered onto an existing background.
     *
     * @param  iconKind  The kind of icon requested.  This should be
     *    one of the constant values ICON_COLOR_16x16, ICON_COLOR_32x32, 
     *    ICON_MONO_16x16, or ICON_MONO_32x32.
     * @return  An image object representing the requested icon.  May
     *    return null if no suitable icon is available.
     */
    @Override
    public java.awt.Image getIcon(int iconKind) {
        switch (iconKind) {
            case ICON_COLOR_16x16:
                if (iconNameC16 == null) {
                    return null;
                } else {
                    if (iconColor16 == null) {
                        iconColor16 = loadImage(iconNameC16);
                    }
                    return iconColor16;
                }
            case ICON_COLOR_32x32:
                if (iconNameC32 == null) {
                    return null;
                } else {
                    if (iconColor32 == null) {
                        iconColor32 = loadImage(iconNameC32);
                    }
                    return iconColor32;
                }
            case ICON_MONO_16x16:
                if (iconNameM16 == null) {
                    return null;
                } else {
                    if (iconMono16 == null) {
                        iconMono16 = loadImage(iconNameM16);
                    }
                    return iconMono16;
                }
            case ICON_MONO_32x32:
                if (iconNameM32 == null) {
                    return null;
                } else {
                    if (iconMono32 == null) {
                        iconMono32 = loadImage(iconNameM32);
                    }
                    return iconMono32;
                }
            default:
                return null;
        }
    }
}
