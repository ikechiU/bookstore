package org.example.bookstore.service.auth.impl;

import org.example.bookstore.model.auth.Permission;
import org.example.bookstore.repository.auth.PermissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@ExtendWith(MockitoExtension.class)
class PermissionServiceImplTest {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionServiceImpl permissionServiceImpl;


    @Test
    void addSystemPermissions() {
        Set<Permission> permissions = Set.of(getPermissions().get(0), getPermissions().get(1));
        // Mock repository behavior
        when(permissionRepository.findFirstByNameOrderByCreatedDate("read"))
                .thenReturn(Optional.of(getPermissions().get(0))); // Mocking existing permission1
        when(permissionRepository.findFirstByNameOrderByCreatedDate("write"))
                .thenReturn(Optional.empty()); // No existing permission2

        // When
        permissionServiceImpl.addSystemPermissions(permissions);

        // Then Verify the repository methods are called correctly
        verify(permissionRepository).findFirstByNameOrderByCreatedDate("read");
        verify(permissionRepository).findFirstByNameOrderByCreatedDate("write");
        verify(permissionRepository).saveAll(permissions);
        verifyNoMoreInteractions(permissionRepository);
    }

    @Test
    void getValidPermissions() {
        when(permissionRepository.findFirstByNameOrderByCreatedDate("read"))
                .thenReturn(Optional.of(getPermissions().get(0)));
        when(permissionRepository.findFirstByNameOrderByCreatedDate("write"))
                .thenReturn(Optional.empty());

        Set<Permission> validPermissions = permissionServiceImpl.getValidPermissions(Set.of("read", "write"));

        assertNotNull(validPermissions);
        assertEquals(1, validPermissions.size());
        verify(permissionRepository).findFirstByNameOrderByCreatedDate("read");
        verify(permissionRepository).findFirstByNameOrderByCreatedDate("write");
        verifyNoMoreInteractions(permissionRepository);
    }

    private List<Permission> getPermissions() {
        Permission permission = new Permission();
        permission.setId(1L);
        permission.setPermissionType("BOOK");
        permission.setName("read");

        Permission permission2 = new Permission();
        permission2.setId(2L);
        permission2.setPermissionType("BOOK");
        permission2.setName("write");

        return  List.of(permission, permission2);
    }


}