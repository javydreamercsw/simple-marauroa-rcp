package simple.marauroa.application.api;

import java.beans.*;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class IRPZoneBeanInfo extends SimpleBeanInfo {

    // Bean descriptor//GEN-FIRST:BeanDescriptor
    /*lazy BeanDescriptor*/
    private static BeanDescriptor getBdescriptor(){
        BeanDescriptor beanDescriptor = new BeanDescriptor  ( marauroa.common.game.IRPZone.class , null ); // NOI18N//GEN-HEADEREND:BeanDescriptor

        // Here you can add code for customizing the BeanDescriptor.

        return beanDescriptor;     }//GEN-LAST:BeanDescriptor
    // Property identifiers//GEN-FIRST:Properties
    private static final int PROPERTY_ID = 0;

    // Property array 
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[1];
    
        try {
            properties[PROPERTY_ID] = new PropertyDescriptor ( "ID", marauroa.common.game.IRPZone.class, "getID", null ); // NOI18N
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Properties

        // Here you can add code for customizing the properties array.

        return properties;     }//GEN-LAST:Properties
    // EventSet identifiers//GEN-FIRST:Events

    // EventSet array
    /*lazy EventSetDescriptor*/
    private static EventSetDescriptor[] getEdescriptor(){
        EventSetDescriptor[] eventSets = new EventSetDescriptor[0];//GEN-HEADEREND:Events

        // Here you can add code for customizing the event sets array.

        return eventSets;     }//GEN-LAST:Events
    // Method identifiers//GEN-FIRST:Methods
    private static final int METHOD_add0 = 0;
    private static final int METHOD_assignRPObjectID1 = 1;
    private static final int METHOD_get2 = 2;
    private static final int METHOD_getPerception3 = 3;
    private static final int METHOD_has4 = 4;
    private static final int METHOD_hide5 = 5;
    private static final int METHOD_iterator6 = 6;
    private static final int METHOD_modify7 = 7;
    private static final int METHOD_nextTurn8 = 8;
    private static final int METHOD_onFinish9 = 9;
    private static final int METHOD_onInit10 = 10;
    private static final int METHOD_remove11 = 11;
    private static final int METHOD_size12 = 12;
    private static final int METHOD_unhide13 = 13;

    // Method array 
    /*lazy MethodDescriptor*/
    private static MethodDescriptor[] getMdescriptor(){
        MethodDescriptor[] methods = new MethodDescriptor[14];
    
        try {
            methods[METHOD_add0] = new MethodDescriptor(marauroa.common.game.IRPZone.class.getMethod("add", new Class[] {marauroa.common.game.RPObject.class})); // NOI18N
            methods[METHOD_add0].setDisplayName ( "" );
            methods[METHOD_assignRPObjectID1] = new MethodDescriptor(marauroa.common.game.IRPZone.class.getMethod("assignRPObjectID", new Class[] {marauroa.common.game.RPObject.class})); // NOI18N
            methods[METHOD_assignRPObjectID1].setDisplayName ( "" );
            methods[METHOD_get2] = new MethodDescriptor(marauroa.common.game.IRPZone.class.getMethod("get", new Class[] {marauroa.common.game.RPObject.ID.class})); // NOI18N
            methods[METHOD_get2].setDisplayName ( "" );
            methods[METHOD_getPerception3] = new MethodDescriptor(marauroa.common.game.IRPZone.class.getMethod("getPerception", new Class[] {marauroa.common.game.RPObject.class, byte.class})); // NOI18N
            methods[METHOD_getPerception3].setDisplayName ( "" );
            methods[METHOD_has4] = new MethodDescriptor(marauroa.common.game.IRPZone.class.getMethod("has", new Class[] {marauroa.common.game.RPObject.ID.class})); // NOI18N
            methods[METHOD_has4].setDisplayName ( "" );
            methods[METHOD_hide5] = new MethodDescriptor(marauroa.common.game.IRPZone.class.getMethod("hide", new Class[] {marauroa.common.game.RPObject.class})); // NOI18N
            methods[METHOD_hide5].setDisplayName ( "" );
            methods[METHOD_iterator6] = new MethodDescriptor(marauroa.common.game.IRPZone.class.getMethod("iterator", new Class[] {})); // NOI18N
            methods[METHOD_iterator6].setDisplayName ( "" );
            methods[METHOD_modify7] = new MethodDescriptor(marauroa.common.game.IRPZone.class.getMethod("modify", new Class[] {marauroa.common.game.RPObject.class})); // NOI18N
            methods[METHOD_modify7].setDisplayName ( "" );
            methods[METHOD_nextTurn8] = new MethodDescriptor(marauroa.common.game.IRPZone.class.getMethod("nextTurn", new Class[] {})); // NOI18N
            methods[METHOD_nextTurn8].setDisplayName ( "" );
            methods[METHOD_onFinish9] = new MethodDescriptor(marauroa.common.game.IRPZone.class.getMethod("onFinish", new Class[] {})); // NOI18N
            methods[METHOD_onFinish9].setDisplayName ( "" );
            methods[METHOD_onInit10] = new MethodDescriptor(marauroa.common.game.IRPZone.class.getMethod("onInit", new Class[] {})); // NOI18N
            methods[METHOD_onInit10].setDisplayName ( "" );
            methods[METHOD_remove11] = new MethodDescriptor(marauroa.common.game.IRPZone.class.getMethod("remove", new Class[] {marauroa.common.game.RPObject.ID.class})); // NOI18N
            methods[METHOD_remove11].setDisplayName ( "" );
            methods[METHOD_size12] = new MethodDescriptor(marauroa.common.game.IRPZone.class.getMethod("size", new Class[] {})); // NOI18N
            methods[METHOD_size12].setDisplayName ( "" );
            methods[METHOD_unhide13] = new MethodDescriptor(marauroa.common.game.IRPZone.class.getMethod("unhide", new Class[] {marauroa.common.game.RPObject.class})); // NOI18N
            methods[METHOD_unhide13].setDisplayName ( "" );
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
