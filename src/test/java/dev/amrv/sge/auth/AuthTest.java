package dev.amrv.sge.auth;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class AuthTest {

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
        String username = "adrian";
        String password = "clave";
        UserCredentials credentials = UserCredentials.anonymous();

//        UserCredentials credentials = new UserCredentials(username, password) {
//            @Override
//            public void loadPermissions() throws IOException {
//                System.out.println(file.getFile());
//                super.loadPermissions();
//            }
//
//        };

        System.out.println(credentials.getUsername());
        System.out.println(credentials.getPassword());

        credentials.loadPermissions();
        PermissionRoot perm = credentials.getPermissions();

        perm.setPermission("test.subperm.*", true);
        perm.setPermission("test.subperm.explicit", false);

        System.out.println(credentials.hasPermission("test.subperm.deeper"));
        System.out.println(credentials.hasPermission("test."));
        System.out.println(credentials.hasPermission("test.subperm.explicit"));

        credentials.savePermissions();

    }

}
