package dev.amrv.sge.auth;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public final class PermissionRoot {

    private final Map<String, Boolean> permissions;

    PermissionRoot() {
        permissions = new HashMap<>();
    }

    public boolean hasPermission(String permission) {
        Boolean val = permissions.get(permission);

        if (val != null)
            return val;

        String result = permission;
        int index;
        do {
            index = result.lastIndexOf('.');

            if (index == -1)
                break;

            result = permission.substring(0, index);

            if (permissions.getOrDefault(result + ".*", Boolean.FALSE))
                return true;

        } while (index > 0);

        return false;
    }

    public void setPermission(String permission, boolean value) {
        permissions.put(permission.trim(), value);
    }

    Map<String, Boolean> getPermissions() {
        return permissions;
    }
}
