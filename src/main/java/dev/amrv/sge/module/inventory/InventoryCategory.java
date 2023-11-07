package dev.amrv.sge.module.inventory;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class InventoryCategory {

    private InventoryCategory parent;
    private final List<InventoryCategory> subcategories = new ArrayList<>();
    private String name;

    InventoryCategory(String name) {
        parent = null;
        this.name = name;
    }

    InventoryCategory(InventoryCategory parent, String name) {
        this.parent = parent;
        this.name = name;
    }

}
