package simple.marauroa.application.api;

import java.beans.*;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class RPObjectBeanInfo extends SimpleBeanInfo {

    // Bean descriptor//GEN-FIRST:BeanDescriptor
    /*lazy BeanDescriptor*/
    private static BeanDescriptor getBdescriptor(){
        BeanDescriptor beanDescriptor = new BeanDescriptor  ( marauroa.common.game.RPObject.class , null ); // NOI18N//GEN-HEADEREND:BeanDescriptor

        // Here you can add code for customizing the BeanDescriptor.

        return beanDescriptor;     }//GEN-LAST:BeanDescriptor
    // Property identifiers//GEN-FIRST:Properties
    private static final int PROPERTY_addedAttributes = 0;
    private static final int PROPERTY_addedMaps = 1;
    private static final int PROPERTY_addedRPSlot = 2;
    private static final int PROPERTY_baseContainer = 3;
    private static final int PROPERTY_contained = 4;
    private static final int PROPERTY_container = 5;
    private static final int PROPERTY_containerBaseOwner = 6;
    private static final int PROPERTY_containerOwner = 7;
    private static final int PROPERTY_containerSlot = 8;
    private static final int PROPERTY_deletedAttributes = 9;
    private static final int PROPERTY_deletedMaps = 10;
    private static final int PROPERTY_deletedRPSlot = 11;
    private static final int PROPERTY_empty = 12;
    private static final int PROPERTY_fromSlots = 13;
    private static final int PROPERTY_hidden = 14;
    private static final int PROPERTY_ID = 15;
    private static final int PROPERTY_RPClass = 16;
    private static final int PROPERTY_storable = 17;

    // Property array 
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[18];
    
        try {
            properties[PROPERTY_addedAttributes] = new PropertyDescriptor ( "addedAttributes", marauroa.common.game.RPObject.class, null, "setAddedAttributes" ); // NOI18N
            properties[PROPERTY_addedMaps] = new PropertyDescriptor ( "addedMaps", marauroa.common.game.RPObject.class, null, "setAddedMaps" ); // NOI18N
            properties[PROPERTY_addedRPSlot] = new PropertyDescriptor ( "addedRPSlot", marauroa.common.game.RPObject.class, null, "setAddedRPSlot" ); // NOI18N
            properties[PROPERTY_baseContainer] = new PropertyDescriptor ( "baseContainer", marauroa.common.game.RPObject.class, "getBaseContainer", null ); // NOI18N
            properties[PROPERTY_contained] = new PropertyDescriptor ( "contained", marauroa.common.game.RPObject.class, "isContained", null ); // NOI18N
            properties[PROPERTY_container] = new PropertyDescriptor ( "container", marauroa.common.game.RPObject.class, "getContainer", null ); // NOI18N
            properties[PROPERTY_containerBaseOwner] = new PropertyDescriptor ( "containerBaseOwner", marauroa.common.game.RPObject.class, "getContainerBaseOwner", null ); // NOI18N
            properties[PROPERTY_containerOwner] = new PropertyDescriptor ( "containerOwner", marauroa.common.game.RPObject.class, "getContainerOwner", null ); // NOI18N
            properties[PROPERTY_containerSlot] = new PropertyDescriptor ( "containerSlot", marauroa.common.game.RPObject.class, "getContainerSlot", null ); // NOI18N
            properties[PROPERTY_deletedAttributes] = new PropertyDescriptor ( "deletedAttributes", marauroa.common.game.RPObject.class, null, "setDeletedAttributes" ); // NOI18N
            properties[PROPERTY_deletedMaps] = new PropertyDescriptor ( "deletedMaps", marauroa.common.game.RPObject.class, null, "setDeletedMaps" ); // NOI18N
            properties[PROPERTY_deletedRPSlot] = new PropertyDescriptor ( "deletedRPSlot", marauroa.common.game.RPObject.class, null, "setDeletedRPSlot" ); // NOI18N
            properties[PROPERTY_empty] = new PropertyDescriptor ( "empty", marauroa.common.game.RPObject.class, "isEmpty", null ); // NOI18N
            properties[PROPERTY_fromSlots] = new IndexedPropertyDescriptor ( "fromSlots", marauroa.common.game.RPObject.class, null, null, "getFromSlots", null ); // NOI18N
            properties[PROPERTY_hidden] = new PropertyDescriptor ( "hidden", marauroa.common.game.RPObject.class, "isHidden", null ); // NOI18N
            properties[PROPERTY_ID] = new PropertyDescriptor ( "ID", marauroa.common.game.RPObject.class, "getID", "setID" ); // NOI18N
            properties[PROPERTY_RPClass] = new PropertyDescriptor ( "RPClass", marauroa.common.game.RPObject.class, "getRPClass", null ); // NOI18N
            properties[PROPERTY_storable] = new PropertyDescriptor ( "storable", marauroa.common.game.RPObject.class, "isStorable", null ); // NOI18N
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
    private static final int METHOD_addEvent1 = 1;
    private static final int METHOD_addLink2 = 2;
    private static final int METHOD_addLink3 = 3;
    private static final int METHOD_addMap4 = 4;
    private static final int METHOD_addSlot5 = 5;
    private static final int METHOD_addSlot6 = 6;
    private static final int METHOD_applyDifferences7 = 7;
    private static final int METHOD_clearEvents8 = 8;
    private static final int METHOD_clearVisible9 = 9;
    private static final int METHOD_clone10 = 10;
    private static final int METHOD_containsKey11 = 11;
    private static final int METHOD_equals12 = 12;
    private static final int METHOD_events13 = 13;
    private static final int METHOD_eventsIterator14 = 14;
    private static final int METHOD_fill15 = 15;
    private static final int METHOD_fill16 = 16;
    private static final int METHOD_get17 = 17;
    private static final int METHOD_get18 = 18;
    private static final int METHOD_getBool19 = 19;
    private static final int METHOD_getBoolean20 = 20;
    private static final int METHOD_getDifferences21 = 21;
    private static final int METHOD_getDouble22 = 22;
    private static final int METHOD_getDouble23 = 23;
    private static final int METHOD_getInt24 = 24;
    private static final int METHOD_getInt25 = 25;
    private static final int METHOD_getLink26 = 26;
    private static final int METHOD_getLinkedObject27 = 27;
    private static final int METHOD_getList28 = 28;
    private static final int METHOD_getMap29 = 29;
    private static final int METHOD_getSlot30 = 30;
    private static final int METHOD_has31 = 31;
    private static final int METHOD_has32 = 32;
    private static final int METHOD_hashCode33 = 33;
    private static final int METHOD_hasLink34 = 34;
    private static final int METHOD_hasMap35 = 35;
    private static final int METHOD_hasSlot36 = 36;
    private static final int METHOD_hide37 = 37;
    private static final int METHOD_instanceOf38 = 38;
    private static final int METHOD_iterator39 = 39;
    private static final int METHOD_maps40 = 40;
    private static final int METHOD_put41 = 41;
    private static final int METHOD_put42 = 42;
    private static final int METHOD_put43 = 43;
    private static final int METHOD_put44 = 44;
    private static final int METHOD_put45 = 45;
    private static final int METHOD_put46 = 46;
    private static final int METHOD_put47 = 47;
    private static final int METHOD_put48 = 48;
    private static final int METHOD_readObject49 = 49;
    private static final int METHOD_remove50 = 50;
    private static final int METHOD_remove51 = 51;
    private static final int METHOD_removeLink52 = 52;
    private static final int METHOD_removeMap53 = 53;
    private static final int METHOD_removeSlot54 = 54;
    private static final int METHOD_resetAddedAndDeleted55 = 55;
    private static final int METHOD_resetAddedAndDeletedAttributes56 = 56;
    private static final int METHOD_resetAddedAndDeletedMaps57 = 57;
    private static final int METHOD_resetAddedAndDeletedRPLink58 = 58;
    private static final int METHOD_resetAddedAndDeletedRPSlot59 = 59;
    private static final int METHOD_setContainer60 = 60;
    private static final int METHOD_setRPClass61 = 61;
    private static final int METHOD_setRPClass62 = 62;
    private static final int METHOD_size63 = 63;
    private static final int METHOD_slots64 = 64;
    private static final int METHOD_slotsIterator65 = 65;
    private static final int METHOD_store66 = 66;
    private static final int METHOD_toAttributeString67 = 67;
    private static final int METHOD_toString68 = 68;
    private static final int METHOD_unhide69 = 69;
    private static final int METHOD_writeObject70 = 70;
    private static final int METHOD_writeObject71 = 71;

    // Method array 
    /*lazy MethodDescriptor*/
    private static MethodDescriptor[] getMdescriptor(){
        MethodDescriptor[] methods = new MethodDescriptor[72];
    
        try {
            methods[METHOD_add0] = new MethodDescriptor(marauroa.common.game.Attributes.class.getMethod("add", new Class[] {java.lang.String.class, int.class})); // NOI18N
            methods[METHOD_add0].setDisplayName ( "" );
            methods[METHOD_addEvent1] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("addEvent", new Class[] {marauroa.common.game.RPEvent.class})); // NOI18N
            methods[METHOD_addEvent1].setDisplayName ( "" );
            methods[METHOD_addLink2] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("addLink", new Class[] {java.lang.String.class, marauroa.common.game.RPObject.class})); // NOI18N
            methods[METHOD_addLink2].setDisplayName ( "" );
            methods[METHOD_addLink3] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("addLink", new Class[] {marauroa.common.game.RPLink.class})); // NOI18N
            methods[METHOD_addLink3].setDisplayName ( "" );
            methods[METHOD_addMap4] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("addMap", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_addMap4].setDisplayName ( "" );
            methods[METHOD_addSlot5] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("addSlot", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_addSlot5].setDisplayName ( "" );
            methods[METHOD_addSlot6] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("addSlot", new Class[] {marauroa.common.game.RPSlot.class})); // NOI18N
            methods[METHOD_addSlot6].setDisplayName ( "" );
            methods[METHOD_applyDifferences7] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("applyDifferences", new Class[] {marauroa.common.game.RPObject.class, marauroa.common.game.RPObject.class})); // NOI18N
            methods[METHOD_applyDifferences7].setDisplayName ( "" );
            methods[METHOD_clearEvents8] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("clearEvents", new Class[] {})); // NOI18N
            methods[METHOD_clearEvents8].setDisplayName ( "" );
            methods[METHOD_clearVisible9] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("clearVisible", new Class[] {boolean.class})); // NOI18N
            methods[METHOD_clearVisible9].setDisplayName ( "" );
            methods[METHOD_clone10] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("clone", new Class[] {})); // NOI18N
            methods[METHOD_clone10].setDisplayName ( "" );
            methods[METHOD_containsKey11] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("containsKey", new Class[] {java.lang.String.class, java.lang.String.class})); // NOI18N
            methods[METHOD_containsKey11].setDisplayName ( "" );
            methods[METHOD_equals12] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("equals", new Class[] {java.lang.Object.class})); // NOI18N
            methods[METHOD_equals12].setDisplayName ( "" );
            methods[METHOD_events13] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("events", new Class[] {})); // NOI18N
            methods[METHOD_events13].setDisplayName ( "" );
            methods[METHOD_eventsIterator14] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("eventsIterator", new Class[] {})); // NOI18N
            methods[METHOD_eventsIterator14].setDisplayName ( "" );
            methods[METHOD_fill15] = new MethodDescriptor(marauroa.common.game.SlotOwner.class.getMethod("fill", new Class[] {marauroa.common.game.Attributes.class})); // NOI18N
            methods[METHOD_fill15].setDisplayName ( "" );
            methods[METHOD_fill16] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("fill", new Class[] {marauroa.common.game.RPObject.class})); // NOI18N
            methods[METHOD_fill16].setDisplayName ( "" );
            methods[METHOD_get17] = new MethodDescriptor(marauroa.common.game.Attributes.class.getMethod("get", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_get17].setDisplayName ( "" );
            methods[METHOD_get18] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("get", new Class[] {java.lang.String.class, java.lang.String.class})); // NOI18N
            methods[METHOD_get18].setDisplayName ( "" );
            methods[METHOD_getBool19] = new MethodDescriptor(marauroa.common.game.Attributes.class.getMethod("getBool", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_getBool19].setDisplayName ( "" );
            methods[METHOD_getBoolean20] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("getBoolean", new Class[] {java.lang.String.class, java.lang.String.class})); // NOI18N
            methods[METHOD_getBoolean20].setDisplayName ( "" );
            methods[METHOD_getDifferences21] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("getDifferences", new Class[] {marauroa.common.game.RPObject.class, marauroa.common.game.RPObject.class})); // NOI18N
            methods[METHOD_getDifferences21].setDisplayName ( "" );
            methods[METHOD_getDouble22] = new MethodDescriptor(marauroa.common.game.Attributes.class.getMethod("getDouble", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_getDouble22].setDisplayName ( "" );
            methods[METHOD_getDouble23] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("getDouble", new Class[] {java.lang.String.class, java.lang.String.class})); // NOI18N
            methods[METHOD_getDouble23].setDisplayName ( "" );
            methods[METHOD_getInt24] = new MethodDescriptor(marauroa.common.game.Attributes.class.getMethod("getInt", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_getInt24].setDisplayName ( "" );
            methods[METHOD_getInt25] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("getInt", new Class[] {java.lang.String.class, java.lang.String.class})); // NOI18N
            methods[METHOD_getInt25].setDisplayName ( "" );
            methods[METHOD_getLink26] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("getLink", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_getLink26].setDisplayName ( "" );
            methods[METHOD_getLinkedObject27] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("getLinkedObject", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_getLinkedObject27].setDisplayName ( "" );
            methods[METHOD_getList28] = new MethodDescriptor(marauroa.common.game.Attributes.class.getMethod("getList", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_getList28].setDisplayName ( "" );
            methods[METHOD_getMap29] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("getMap", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_getMap29].setDisplayName ( "" );
            methods[METHOD_getSlot30] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("getSlot", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_getSlot30].setDisplayName ( "" );
            methods[METHOD_has31] = new MethodDescriptor(marauroa.common.game.Attributes.class.getMethod("has", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_has31].setDisplayName ( "" );
            methods[METHOD_has32] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("has", new Class[] {java.lang.String.class, java.lang.String.class})); // NOI18N
            methods[METHOD_has32].setDisplayName ( "" );
            methods[METHOD_hashCode33] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("hashCode", new Class[] {})); // NOI18N
            methods[METHOD_hashCode33].setDisplayName ( "" );
            methods[METHOD_hasLink34] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("hasLink", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_hasLink34].setDisplayName ( "" );
            methods[METHOD_hasMap35] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("hasMap", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_hasMap35].setDisplayName ( "" );
            methods[METHOD_hasSlot36] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("hasSlot", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_hasSlot36].setDisplayName ( "" );
            methods[METHOD_hide37] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("hide", new Class[] {})); // NOI18N
            methods[METHOD_hide37].setDisplayName ( "" );
            methods[METHOD_instanceOf38] = new MethodDescriptor(marauroa.common.game.Attributes.class.getMethod("instanceOf", new Class[] {marauroa.common.game.RPClass.class})); // NOI18N
            methods[METHOD_instanceOf38].setDisplayName ( "" );
            methods[METHOD_iterator39] = new MethodDescriptor(marauroa.common.game.Attributes.class.getMethod("iterator", new Class[] {})); // NOI18N
            methods[METHOD_iterator39].setDisplayName ( "" );
            methods[METHOD_maps40] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("maps", new Class[] {})); // NOI18N
            methods[METHOD_maps40].setDisplayName ( "" );
            methods[METHOD_put41] = new MethodDescriptor(marauroa.common.game.Attributes.class.getMethod("put", new Class[] {java.lang.String.class, java.lang.String.class})); // NOI18N
            methods[METHOD_put41].setDisplayName ( "" );
            methods[METHOD_put42] = new MethodDescriptor(marauroa.common.game.Attributes.class.getMethod("put", new Class[] {java.lang.String.class, int.class})); // NOI18N
            methods[METHOD_put42].setDisplayName ( "" );
            methods[METHOD_put43] = new MethodDescriptor(marauroa.common.game.Attributes.class.getMethod("put", new Class[] {java.lang.String.class, double.class})); // NOI18N
            methods[METHOD_put43].setDisplayName ( "" );
            methods[METHOD_put44] = new MethodDescriptor(marauroa.common.game.Attributes.class.getMethod("put", new Class[] {java.lang.String.class, java.util.List.class})); // NOI18N
            methods[METHOD_put44].setDisplayName ( "" );
            methods[METHOD_put45] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("put", new Class[] {java.lang.String.class, java.lang.String.class, java.lang.String.class})); // NOI18N
            methods[METHOD_put45].setDisplayName ( "" );
            methods[METHOD_put46] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("put", new Class[] {java.lang.String.class, java.lang.String.class, int.class})); // NOI18N
            methods[METHOD_put46].setDisplayName ( "" );
            methods[METHOD_put47] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("put", new Class[] {java.lang.String.class, java.lang.String.class, double.class})); // NOI18N
            methods[METHOD_put47].setDisplayName ( "" );
            methods[METHOD_put48] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("put", new Class[] {java.lang.String.class, java.lang.String.class, boolean.class})); // NOI18N
            methods[METHOD_put48].setDisplayName ( "" );
            methods[METHOD_readObject49] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("readObject", new Class[] {marauroa.common.net.InputSerializer.class})); // NOI18N
            methods[METHOD_readObject49].setDisplayName ( "" );
            methods[METHOD_remove50] = new MethodDescriptor(marauroa.common.game.Attributes.class.getMethod("remove", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_remove50].setDisplayName ( "" );
            methods[METHOD_remove51] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("remove", new Class[] {java.lang.String.class, java.lang.String.class})); // NOI18N
            methods[METHOD_remove51].setDisplayName ( "" );
            methods[METHOD_removeLink52] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("removeLink", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_removeLink52].setDisplayName ( "" );
            methods[METHOD_removeMap53] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("removeMap", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_removeMap53].setDisplayName ( "" );
            methods[METHOD_removeSlot54] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("removeSlot", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_removeSlot54].setDisplayName ( "" );
            methods[METHOD_resetAddedAndDeleted55] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("resetAddedAndDeleted", new Class[] {})); // NOI18N
            methods[METHOD_resetAddedAndDeleted55].setDisplayName ( "" );
            methods[METHOD_resetAddedAndDeletedAttributes56] = new MethodDescriptor(marauroa.common.game.Attributes.class.getMethod("resetAddedAndDeletedAttributes", new Class[] {})); // NOI18N
            methods[METHOD_resetAddedAndDeletedAttributes56].setDisplayName ( "" );
            methods[METHOD_resetAddedAndDeletedMaps57] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("resetAddedAndDeletedMaps", new Class[] {})); // NOI18N
            methods[METHOD_resetAddedAndDeletedMaps57].setDisplayName ( "" );
            methods[METHOD_resetAddedAndDeletedRPLink58] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("resetAddedAndDeletedRPLink", new Class[] {})); // NOI18N
            methods[METHOD_resetAddedAndDeletedRPLink58].setDisplayName ( "" );
            methods[METHOD_resetAddedAndDeletedRPSlot59] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("resetAddedAndDeletedRPSlot", new Class[] {})); // NOI18N
            methods[METHOD_resetAddedAndDeletedRPSlot59].setDisplayName ( "" );
            methods[METHOD_setContainer60] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("setContainer", new Class[] {marauroa.common.game.SlotOwner.class, marauroa.common.game.RPSlot.class})); // NOI18N
            methods[METHOD_setContainer60].setDisplayName ( "" );
            methods[METHOD_setRPClass61] = new MethodDescriptor(marauroa.common.game.Attributes.class.getMethod("setRPClass", new Class[] {marauroa.common.game.RPClass.class})); // NOI18N
            methods[METHOD_setRPClass61].setDisplayName ( "" );
            methods[METHOD_setRPClass62] = new MethodDescriptor(marauroa.common.game.Attributes.class.getMethod("setRPClass", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_setRPClass62].setDisplayName ( "" );
            methods[METHOD_size63] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("size", new Class[] {})); // NOI18N
            methods[METHOD_size63].setDisplayName ( "" );
            methods[METHOD_slots64] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("slots", new Class[] {})); // NOI18N
            methods[METHOD_slots64].setDisplayName ( "" );
            methods[METHOD_slotsIterator65] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("slotsIterator", new Class[] {})); // NOI18N
            methods[METHOD_slotsIterator65].setDisplayName ( "" );
            methods[METHOD_store66] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("store", new Class[] {})); // NOI18N
            methods[METHOD_store66].setDisplayName ( "" );
            methods[METHOD_toAttributeString67] = new MethodDescriptor(marauroa.common.game.Attributes.class.getMethod("toAttributeString", new Class[] {})); // NOI18N
            methods[METHOD_toAttributeString67].setDisplayName ( "" );
            methods[METHOD_toString68] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("toString", new Class[] {})); // NOI18N
            methods[METHOD_toString68].setDisplayName ( "" );
            methods[METHOD_unhide69] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("unhide", new Class[] {})); // NOI18N
            methods[METHOD_unhide69].setDisplayName ( "" );
            methods[METHOD_writeObject70] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("writeObject", new Class[] {marauroa.common.net.OutputSerializer.class})); // NOI18N
            methods[METHOD_writeObject70].setDisplayName ( "" );
            methods[METHOD_writeObject71] = new MethodDescriptor(marauroa.common.game.RPObject.class.getMethod("writeObject", new Class[] {marauroa.common.net.OutputSerializer.class, marauroa.common.game.DetailLevel.class})); // NOI18N
            methods[METHOD_writeObject71].setDisplayName ( "" );
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
