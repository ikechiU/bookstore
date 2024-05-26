package org.example.bookstore.repository.auth;

import org.example.bookstore.model.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findFirstByNameAndIsDeletedOrderByCreatedDate(String name, boolean isDeleted);
    Optional<Role> findFirstByNameOrderByCreatedDate(String name);
}
