package org.example.bookstore.service.auth.impl;

import org.example.bookstore.dto.auth.PermissionDTO;
import org.example.bookstore.model.auth.Permission;
import org.example.bookstore.model.auth.Role;
import org.example.bookstore.model.bookstore.Book;
import org.example.bookstore.repository.auth.RoleRepository;
import org.example.bookstore.service.auth.PermissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionService<PermissionDTO, Permission> permissionServiceImpl;

    @InjectMocks
    private RoleServiceImpl roleServiceImpl;

    @Test
    void retrieveRole() {
        Role role = new Role();
        role.setName("USER");

        // Mock repository behavior
        when(roleRepository.findFirstByNameAndIsDeletedOrderByCreatedDate("USER", false))
                .thenReturn(Optional.of(role)); // Mocking existing role

        // When
        Role retrievedRole = roleServiceImpl.retrieveRole("USER");

        // Then Verify the repository methods are called correctly
        verify(roleRepository).findFirstByNameAndIsDeletedOrderByCreatedDate("USER", false);
        assertEquals("USER", retrievedRole.getName());
        assertNotNull(retrievedRole);
    }


    @Test
    void systemRolesChecks_Role_Created() {
        when(roleRepository.count())
                .thenReturn(0L);
        when(roleRepository.findFirstByNameOrderByCreatedDate("SUPER_ADMIN"))
                .thenReturn(Optional.empty());
        when(roleRepository.findFirstByNameOrderByCreatedDate("ADMIN"))
                .thenReturn(Optional.empty());
        when(roleRepository.findFirstByNameOrderByCreatedDate("USER"))
                .thenReturn(Optional.empty());

        Set<String> _superAdminPermission = Set.of("read-book", "write-book", "delete-book", "read-profile", "create-profile", "edit-profile", "delete-profile");
        Set<String> _adminPermission = Set.of("read-book", "write-book", "delete-book");
        Set<String> _userPermission = Set.of("read-book");

        Set<Permission> superAdminPermission = Set.of(permissions().get(0), permissions().get(1), permissions().get(2), permissions().get(3), permissions().get(4), permissions().get(5), permissions().get(6));
        Set<Permission> adminPermission = Set.of(permissions().get(0), permissions().get(1), permissions().get(2));
        Set<Permission> userPermission = Set.of(permissions().get(0));

        when(permissionServiceImpl.getValidPermissions(_superAdminPermission))
                .thenReturn(superAdminPermission);
        when(permissionServiceImpl.getValidPermissions(_adminPermission))
                .thenReturn(adminPermission);
        when(permissionServiceImpl.getValidPermissions(_userPermission))
                .thenReturn(userPermission);

        roleServiceImpl.systemRolesChecks();

        verify(roleRepository).count();
        verify(roleRepository).findFirstByNameOrderByCreatedDate("SUPER_ADMIN");
        verify(roleRepository).findFirstByNameOrderByCreatedDate("ADMIN");
        verify(roleRepository).findFirstByNameOrderByCreatedDate("USER");
        verify(permissionServiceImpl).getValidPermissions(_superAdminPermission);
        verify(permissionServiceImpl).getValidPermissions(_adminPermission);
        verify(permissionServiceImpl).getValidPermissions(_userPermission);
        verify(roleRepository, times(3)).save(any());
    }


    @Test
    void systemRolesChecks_No_Role_Created() {
        when(roleRepository.count())
                .thenReturn(4L);

        roleServiceImpl.systemRolesChecks();

        verify(roleRepository).count();
    }

    @Test
    void createSystemDefaultRole() {
        Set<String> _userPermission = Set.of("read-book");
        Set<Permission> userPermission = Set.of(permissions().get(0));

        when(permissionServiceImpl.getValidPermissions(_userPermission))
                .thenReturn(userPermission);

        roleServiceImpl.createSystemDefaultRole("USER", "This is a system default user role", _userPermission);

        verify(permissionServiceImpl).getValidPermissions(_userPermission);
        verify(roleRepository, times(1)).save(any());
    }

    private List<Permission> permissions() {
        return RoleList.permissions();
    }

}