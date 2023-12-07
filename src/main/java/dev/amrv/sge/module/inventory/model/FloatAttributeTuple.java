package dev.amrv.sge.module.inventory.model;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class FloatAttributeTuple extends AttributeTuple {

    public FloatAttributeTuple(String key) {
        super(key);
    }

    public FloatAttributeTuple(String key, String value) {
        super(key, value);
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
    }

    @Override
    public boolean accepts(String value) {
        if (value == null)
            return false;

        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }

    }
}
