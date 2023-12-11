package dev.amrv.sge.auth;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map.Entry;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class AuthTest {

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
        UserCredentials credentials = UserCredentials.loadAdministrator();

        System.out.println(credentials.getUsername());
//        System.out.println(credentials.getPasswordHash());

        //credentials.loadPermissions();
        PermissionRoot perm = credentials.getPermissions();

        for (Entry<String,Boolean> permission : perm.getPermissions().entrySet())
            System.out.println(permission.getKey() + ": " + permission.getValue());
        //perm.setPermission("*", true);

        System.out.println(credentials.hasPermission("test.subperm.deeper"));
        System.out.println(credentials.hasPermission("test"));
        System.out.println(credentials.hasPermission("configuration.see"));

        //credentials.savePermissions();
    }

}
