package org.example.bookstore.service.auth;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

public interface RoleService<T, V> {
    V retrieveRole(String name) throws UsernameNotFoundException;
    default void systemRolesChecks() {}

}
