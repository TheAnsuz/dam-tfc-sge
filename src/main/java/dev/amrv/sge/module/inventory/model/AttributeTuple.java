package dev.amrv.sge.module.inventory.model;

import java.util.Objects;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class AttributeTuple {

    private final String key;
    private String value;

    public AttributeTuple(String key) {
        this.key = key;
    }

    public AttributeTuple(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public boolean accepts(String value) {
        return true;
    }

    public boolean isValid() {
        return accepts(value);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.key);
        hash = 37 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof AttributeTuple) {
            return this.key.equals(((AttributeTuple) other).key);
        }
        return false;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + key + "=" + value + ']';
    }

}
