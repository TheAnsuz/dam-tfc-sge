package dev.amrv.sge.auth;

import dev.amrv.sge.SGEFileSystem;
import dev.amrv.sge.io.PermissionsFile;
import java.io.IOException;
import java.util.Map.Entry;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public final class UserCredentials {

    private static final UserCredentials anonymous = new UserCredentials("anonymous", "anonymous", null, 0);

    public static UserCredentials loadAnonymous() throws IOException {
        anonymous.savePermissions();
        return anonymous;
    }

    private static final UserCredentials administrator = new UserCredentials("administrador", "administrador", null, 0);

    public static UserCredentials loadAdministrator() throws IOException {
        administrator.getPermissions().setPermission("*", true);
        administrator.savePermissions();
        return administrator;
    }

    private final PermissionRoot permissions;
    private final String username;
    private final String credentialsHash;
    protected final PermissionsFile file;

    public static UserCredentials tryGetUser(String name, String password) {
        UserCredentials mentioned = new UserCredentials(name, password, null, 0);

        // Si no existe el usuario
        if (!mentioned.file.isValid())
            return null;

        return mentioned;
    }

    public static UserCredentials createUser(String username, String password) throws IOException {
        UserCredentials mentioned = new UserCredentials(username, password, null, 0);
        mentioned.savePermissions();
        return mentioned;
    }

    private UserCredentials(String username, String password, PermissionRoot permissions, long cipher) {
        this.username = username;
        this.credentialsHash = ((password.hashCode() >> 2) + username.hashCode()) + "";
        this.permissions = permissions == null ? new PermissionRoot() : permissions;

        if (cipher == 0)
            cipher = password.hashCode();

        this.file = new PermissionsFile(SGEFileSystem.getSourceFile("user", credentialsHash), cipher);
    }

    public void loadPermissions() throws IOException {
        file.read();

        for (String line : file.content()) {
            if (line.startsWith("@")) {
                // Metadata

            } else {
                if (line.startsWith("!"))
                    // Revoke permission
                    permissions.setPermission(line.substring(1), false);
                else
                    // Grant permission
                    permissions.setPermission(line, true);
            }
        }
    }

    public void savePermissions() throws IOException {
        for (Entry<String, Boolean> perm : permissions.getPermissions().entrySet()) {
            if (perm.getValue())
                file.content().add(perm.getKey());
            else
                file.content().add("!" + perm.getKey());
        }

        file.save();
    }

    public String getUsername() {
        return username;
    }

    public PermissionRoot getPermissions() {
        return permissions;
    }

    public boolean hasPermission(String permission) {
        return permissions.hasPermission(permission);
    }
}
