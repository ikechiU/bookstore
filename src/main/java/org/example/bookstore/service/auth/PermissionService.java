package org.example.bookstore.service.auth;

import java.util.Set;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

public interface PermissionService<T, V> {
    void addSystemPermissions(Set<V> newPermissions);
    Set<V> getValidPermissions(Set<String> permissions);
}
