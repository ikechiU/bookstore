package org.example.bookstore.repository.auth;

import org.example.bookstore.model.auth.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    boolean existsByEmail(String email);

}
