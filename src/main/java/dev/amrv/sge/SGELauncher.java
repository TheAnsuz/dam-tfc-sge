package dev.amrv.sge;

import dev.amrv.sge.module.ConfigurationModule;
import dev.amrv.sge.module.DatabaseModule;
import dev.amrv.sge.module.SellModule;
import dev.amrv.sge.module.InventoryModule;
import dev.amrv.sge.module.ProvidersModule;
import dev.amrv.sge.module.UsersModule;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class SGELauncher {

    public static SGE instance;

    public static void main(String[] args) {
        instance = new SGE();
        instance.addModule(new UsersModule());
        instance.addModule(new ConfigurationModule());
        instance.addModule(new ProvidersModule());
        instance.addModule(new DatabaseModule());
        instance.addModule(new InventoryModule());
        instance.addModule(new SellModule());
        instance.start();
    }

}
