package dev.amrv.sge.module.inventory;

import dev.amrv.sge.bbdd.Database;
import dev.amrv.sge.module.InventoryModule;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class InventoryUtils {

    private final InventoryModule module;

    public InventoryUtils(InventoryModule module) {
        this.module = module;
    }

    private Database database() {
        return module.getSge().getDatabase();
    }

    /*
     * HEYY, he cambiado de planteamiento ante como desarrollar esto.
     *
     * Basicamente es mejor opcion que los objetos solo se instancien desde la
     * propia clase (private)
     *
     * y cada clase permite obtenerlo mediante su id o crearlo (asigna una nueva
     * id). Es decir, siempre tienes la opcion de buscar directamente mediante
     * una ID
     *
     * Es decir, empezamos con una categoria null, de ahi podemos hacer un
     * getCategories(), getCategory(id), createCategory() para obtener
     * subcategorias o se puede hacer un getProducts(), getProduct(id) o
     * createProduct() para obtener productos.
     *
     * De los productos se podria sacar de nuevo la categoria a la que
     * pertenecen (si se cachea el objeto perfecto, sino, con la ID se vuelve a
     * buscar)
     *
     * De los productos se pueden sacar sus atributos como mapa
     * (String,Attribute)
     *
     * Todos estos objetos tendrán dos opciones mas: commit y rollback, commit
     * hará un update del objeto con toda la informacion y rollback volverá a
     * cargarla de bbdd
     */
}
