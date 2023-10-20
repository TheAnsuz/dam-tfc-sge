package dev.amrv.sge.auth;

import dev.amrv.sge.SGEFileSystem;
import dev.amrv.sge.io.PermissionsFile;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map.Entry;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public final class UserCredentials {

    private static final UserCredentials anonymous = new UserCredentials("anonymous", "");

    public static UserCredentials getUserCredentials(String username, String password) {

        UserCredentials credentials = new UserCredentials(username, password);

        if (!credentials.isValid())
            return anonymous();

        return credentials;
    }

    private final PermissionRoot permissions;
    private final String username;
    private final String password;
    protected final PermissionsFile file;

    private UserCredentials(String username, String password) {
        this.username = username;
        this.password = callDigest(password);
        this.permissions = new PermissionRoot();
        this.file = new PermissionsFile(SGEFileSystem.getSourceFile("user", this.password));
    }

    protected boolean isValid() {
        return true;
    }

    private String callDigest(String password) {
        return digest(password.getBytes());
    }

    protected String digest(byte[] bytes) {
        return String.valueOf(Arrays.hashCode(bytes) * (long) Arrays.hashCode(bytes));
    }

    public void loadPermissions() throws IOException {
        file.read();

        for (String line : file.content())
            if (line.startsWith("!"))
                permissions.setPermission(line.substring(1), false);
            else
                permissions.setPermission(line, true);
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

    public String getPassword() {
        return password;
    }

    public PermissionRoot getPermissions() {
        return permissions;
    }

    public boolean hasPermission(String permission) {
        return permissions.hasPermission(permission);
    }

    public static UserCredentials anonymous() {
        return anonymous;
    }
}
