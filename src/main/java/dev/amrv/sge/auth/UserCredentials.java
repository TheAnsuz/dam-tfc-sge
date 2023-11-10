package dev.amrv.sge.auth;

import dev.amrv.sge.SGE;
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

    private static final UserCredentials anonymous = new UserCredentials("anonymous", "anonymous");

    public synchronized static boolean changeUsername(SGE sge, String newUsername, String newPassword, String password) throws IOException {
        if (!sge.getUser().getPasswordHash().equals(sge.getUser().callDigest(password)))
            return false;

        final UserCredentials newCredentials = new UserCredentials(newUsername, newPassword, sge.getUser().permissions);

        newCredentials.file.save();
        sge.getUser().file.getFile().delete();
        sge.getUser().file.getFile().deleteOnExit();

        sge.setUser(newCredentials);
        return true;
    }

    public static UserCredentials createUser(String username, String password) throws IOException {
        UserCredentials credentials = new UserCredentials(username, password);

        credentials.savePermissions();

        if (!credentials.isValid())
            return anonymous();

        return credentials;
    }

    public static UserCredentials getUserCredentials(String username, String password) {
        return new UserCredentials(username, password);
    }

    private final PermissionRoot permissions;
    private final String username;
    private final String password;
    protected final PermissionsFile file;

    private UserCredentials(String username, String password) {
        this(username, password, null);
    }

    private UserCredentials(String username, String password, PermissionRoot permissions) {
        this.username = username;
//        System.out.println("USER: " + username + " -- " + password);
        this.password = callDigest(password);
        this.permissions = permissions == null ? new PermissionRoot() : permissions;
        this.file = new PermissionsFile(SGEFileSystem.getSourceFile("user", getFilename()));
//        System.out.println("FILE: " + this.file.getFile().getName());
    }

    protected String getFilename() {
        return callDigest(username);
    }

    public boolean isValid() {
        return file.getFile().isFile();
    }

    private String callDigest(String password) {
        return digest(password.getBytes());
    }

    protected String digest(byte[] bytes) {
        return String.valueOf(Arrays.hashCode(bytes) * (long) Arrays.hashCode(bytes));
    }

    public void loadPermissions() throws IOException {
        file.read();

        for (String line : file.content()) {
            if (line.startsWith("@")) {
                // Header
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

    public String getPasswordHash() {
        return password;
    }

    public PermissionRoot getPermissions() {
        return permissions;
    }

    public boolean hasPermission(String permission) {
        return permissions.hasPermission(permission);
    }

    public static UserCredentials loadAnonymous() throws IOException {
        anonymous.file.read();

        return anonymous;
    }

    public static UserCredentials anonymous() {
        return anonymous;
    }
}
