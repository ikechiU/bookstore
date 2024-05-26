package org.example.bookstore.service.auth.impl;

import org.example.bookstore.model.auth.Permission;

import java.util.List;

public class RoleList {
    public static List<Permission> permissions() {
        Permission permission = new Permission();
        permission.setId(1L);
        permission.setName("read-book");

        Permission permission2 = new Permission();
        permission2.setId(2L);
        permission2.setName("write-book");

        Permission permission3 = new Permission();
        permission3.setId(3L);
        permission3.setName("delete-book");

        Permission permission4 = new Permission();
        permission4.setId(4L);
        permission4.setName("read-profile");

        Permission permission5 = new Permission();
        permission5.setId(5L);
        permission5.setName("create-profile");

        Permission permission6 = new Permission();
        permission6.setId(6L);
        permission6.setName("edit-profile");

        Permission permission7 = new Permission();
        permission7.setId(7L);
        permission7.setName("delete-profile");

        return List.of(permission, permission2, permission3, permission4, permission5, permission6, permission7);
    }
}
