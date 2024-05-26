package org.example.bookstore.service.auth.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.auth.PermissionDTO;
import org.example.bookstore.model.auth.Permission;
import org.example.bookstore.repository.auth.PermissionRepository;
import org.example.bookstore.service.auth.PermissionService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */


@RequiredArgsConstructor
@Service
public class PermissionServiceImpl implements PermissionService<PermissionDTO, Permission> {

    private final PermissionRepository permissionRepository;

    @Override
    public void addSystemPermissions(Set<Permission> newPermissions) {
        newPermissions.forEach((permission) -> {
            Optional<Permission> existingPermission = permissionRepository.findFirstByNameOrderByCreatedDate(permission.getName());
            if (existingPermission.isPresent()) {
                permission.setId(existingPermission.get().getId());
                permission.setPermissionType(existingPermission.get().getPermissionType());
                permission.setName(existingPermission.get().getName());
                permission.setDeleted(existingPermission.get().isDeleted());
                permission.setDescription(existingPermission.get().getDescription());
                permission.setPermissionType(existingPermission.get().getPermissionType());
            }

        });
        permissionRepository.saveAll(newPermissions);
    }

    @Override
    public Set<Permission> getValidPermissions(Set<String> permissions) {
        Set<Permission> returnValue = new HashSet<>();
        for (String permission: permissions) {
            Optional<Permission> existingPermission = permissionRepository.findFirstByNameOrderByCreatedDate(permission);
            existingPermission.ifPresent(returnValue::add);
        }
        return returnValue;
    }

}
