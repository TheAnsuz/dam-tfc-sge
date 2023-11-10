package dev.amrv.sge.module.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class InventoryAttributes {

    private final Map<String, String> map = new HashMap<>();
    private final int id;

    public InventoryAttributes(int id) {
        this.id = id;
    }

    public void remove(String key) {
        map.remove(key);
    }

    public void set(String key, String value) {
        map.put(key, value);
    }

    public String get(String key, String defValue) {
        return map.getOrDefault(key, defValue);
    }

    public String get(String key) {
        return map.getOrDefault(key, null);
    }

    public void clear() {
        map.clear();
    }

    public Set<Entry<String, String>> entrySet() {
        return map.entrySet();
    }
}
