package org.example.bookstore.service.auth.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.dto.PaginatedResponseDTO;
import org.example.bookstore.dto.auth.UserDTO;
import org.example.bookstore.dto.auth.UserProfileDTO;
import org.example.bookstore.dto.auth.UserRegistrationDTO;
import org.example.bookstore.model.auth.UserProfile;
import org.example.bookstore.repository.auth.UserProfileRepository;
import org.example.bookstore.service.auth.UserProfileService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.bookstore.util.AppUtil.getPageable;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRegistrationServiceImpl userRegistrationServiceImpl;

    @Override
    public PaginatedResponseDTO retrieveAllUserProfiles(int page, int size) {
        Page<UserProfile> pagedUserProfile = userProfileRepository.findAll(getPageable(page, size));
        return PaginatedResponseDTO.getPagination(pagedUserProfile, map(pagedUserProfile.getContent()));
    }

    @Override
    public UserProfileDTO createUserProfile(UserRegistrationDTO userRegistrationDTO) {
        return userRegistrationServiceImpl.createProfile(userRegistrationDTO, "USER");
    }

    private UserDTO map(UserProfile userProfile) {
        return UserDTO.builder()
                .username(userProfile.getEmail())
                .firstName(userProfile.getFirstName())
                .lastName(userProfile.getLastName())
                .email(userProfile.getEmail())
                .phoneNumber(userProfile.getPhoneNumber())
                .build();
    }

    private List<UserDTO> map(List<UserProfile> userProfiles) {
        return userProfiles.stream().map(this::map)
                .toList();
    }

}
