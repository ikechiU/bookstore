package org.example.bookstore.service.auth.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.auth.PermissionDTO;
import org.example.bookstore.dto.auth.RoleDTO;
import org.example.bookstore.model.auth.Permission;
import org.example.bookstore.model.auth.Role;
import org.example.bookstore.repository.auth.RoleRepository;
import org.example.bookstore.service.auth.PermissionService;
import org.example.bookstore.service.auth.RoleService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService<RoleDTO, Role> {

    private final RoleRepository roleRepository;
    private final PermissionService<PermissionDTO, Permission> permissionServiceImpl;
    @Override
    public Role retrieveRole(String name) throws UsernameNotFoundException {
        return roleRepository
                .findFirstByNameAndIsDeletedOrderByCreatedDate(name, false)
                .orElseThrow(() -> new UsernameNotFoundException("Role does not exist"));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public void systemRolesChecks() {
        long count = this.roleRepository.count();
        if (count < 4L) {
            Optional<Role> superAdminRole = roleRepository.findFirstByNameOrderByCreatedDate("SUPER_ADMIN");
            Optional<Role> adminRole = roleRepository.findFirstByNameOrderByCreatedDate("ADMIN");
            Optional<Role> userRole = roleRepository.findFirstByNameOrderByCreatedDate("USER");
            if (superAdminRole.isEmpty()) {
                createSystemDefaultRole("SUPER_ADMIN", "This is a system default super admin role used to invite admin and perform every other action in the system", Set.of("read-book", "write-book", "delete-book", "read-profile", "create-profile", "edit-profile", "delete-profile"));
            }
            if (adminRole.isEmpty()) {
                createSystemDefaultRole("ADMIN", "This is a system default admin role used to read, write and delete books", Set.of("read-book", "write-book", "delete-book"));
            }
            if (userRole.isEmpty()) {
                createSystemDefaultRole("USER", "This is a system default user role", Set.of("read-book"));
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public void createSystemDefaultRole(String roleName, String description, Set<String> permissions) {
        Role role = new Role();
        role.setName(roleName);
        role.setDescription(description);
        role.setDeleted(false);
        role.setPermissions(permissionServiceImpl.getValidPermissions(permissions));
        this.roleRepository.save(role);
    }

}
