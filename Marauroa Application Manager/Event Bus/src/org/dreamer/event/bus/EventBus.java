package org.dreamer.event.bus;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.openide.util.Lookup.Result;

/**
 * Based on work from Fabrizio Giudici
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class EventBus {

    private static final EventBus instance = new EventBus();
    private final CentralLookup centralLookup = CentralLookup.getDefault();
    private final Map<Class<?>, Result<?>> resultMapByClass = new HashMap<Class<?>, Result<?>>();
    private final Map<EventBusListener<?>, ListenerAdapter<?>> adapterMapByListener = new HashMap<EventBusListener<?>, ListenerAdapter<?>>();

    private EventBus() {
    }

    public static EventBus getDefault() {
        return instance;
    }

    public void clearLookup(final Class clazz) {
        for (final Object old : getCentralLookup().lookupAll(clazz)) {
            getCentralLookup().remove(old);
        }
    }

    public void publish(final Object object) {
        if (object == null) {
            throw new IllegalArgumentException("object is mandatory");
        }

        clearLookup(object.getClass());
        getCentralLookup().add(object);
    }

    public void unpublish(final Class<?> topic) {
        clearLookup(topic);
    }

    public <T extends Object> Collection<? extends T> lookupAll(Class<T> clazz) {
        return EventBus.getDefault().getCentralLookup().lookupAll(clazz);
    }

    public synchronized <T> void subscribe(final Class<T> topic, final EventBusListener<T> listener) {
        Result<?> result = resultMapByClass.get(topic);

        if (result == null) {
            result = getCentralLookup().lookupResult(topic);
            resultMapByClass.put(topic, result);
            result.allInstances();
        }

        final ListenerAdapter<T> adapter = new ListenerAdapter<T>(listener);
        adapterMapByListener.put(listener, adapter);
        result.addLookupListener(adapter);
    }

    public synchronized <T> void unsubscribe(final Class<T> topic, final EventBusListener<T> listener) {
        final Result<?> result = resultMapByClass.get(topic);

        if (result == null) {
            throw new IllegalArgumentException(String.format("Never subscribed to %s", topic));
        }

        final ListenerAdapter<T> adapter = (ListenerAdapter<T>) adapterMapByListener.remove(listener);
        result.removeLookupListener(adapter);
    }

    /**
     * @return the centralLookup
     */
    public CentralLookup getCentralLookup() {
        return centralLookup;
    }

    public void add(Object instance) {
        getCentralLookup().add(instance);
    }

    public void remove(Object instance) {
        getCentralLookup().remove(instance);
    }

    public final <T extends Object> T lookup(Class<T> clazz) {
        return getCentralLookup().lookup(clazz);
    }
}
