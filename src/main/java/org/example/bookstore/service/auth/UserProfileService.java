package org.example.bookstore.service.auth;

import org.example.bookstore.dto.PaginatedResponseDTO;
import org.example.bookstore.dto.auth.UserProfileDTO;
import org.example.bookstore.dto.auth.UserRegistrationDTO;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

public interface UserProfileService {

    PaginatedResponseDTO retrieveAllUserProfiles(int page, int size);
    UserProfileDTO createUserProfile(UserRegistrationDTO userRegistrationDTO);

}
