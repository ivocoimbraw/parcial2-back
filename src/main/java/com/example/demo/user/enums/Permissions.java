package com.example.demo.user.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum Permissions {

    // 📌 Permisos CRUD para
    PERMISSION_READ,
    PERMISSION_CREATE,
    PERMISSION_UPDATE,
    PERMISSION_DELETE;

    static {
        permissionListEnums = Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    private static final Set<String> permissionListEnums;

    // Método para obtener la lista de nombres de permisos
    public static Set<String> getAllPermissionNames() {
        return permissionListEnums;
    }
}
