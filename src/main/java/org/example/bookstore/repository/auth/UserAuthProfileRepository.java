package org.example.bookstore.repository.auth;

import org.example.bookstore.model.auth.UserAuthProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@Repository
public interface UserAuthProfileRepository extends JpaRepository<UserAuthProfile, Long> {
    Optional<UserAuthProfile> findFirstByUsernameOrderByCreatedDate(String username);
    boolean existsByUsername(String username);
}
