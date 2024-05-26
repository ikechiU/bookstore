package org.example.bookstore.service.auth.impl;

import org.example.bookstore.dto.PaginatedResponseDTO;
import org.example.bookstore.dto.auth.UserProfileDTO;
import org.example.bookstore.dto.auth.UserRegistrationDTO;
import org.example.bookstore.model.auth.UserProfile;
import org.example.bookstore.repository.auth.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@ExtendWith(MockitoExtension.class)
class UserProfileServiceImplTest {

    @Mock
    private UserProfileRepository userProfileRepository;
    @Mock
    private UserRegistrationServiceImpl userRegistrationServiceImpl;
    @InjectMocks
    private UserProfileServiceImpl userProfileServiceImpl;


    @Test
    void retrieveAllUserProfiles() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        UserProfile userProfile = new UserProfile(UUID.randomUUID(), "user@bookstore.com", "USER", "USER", null);
        Page<UserProfile> pagedResult = new PageImpl<>(List.of(userProfile));

        // Mock repository behavior
        when(userProfileRepository.findAll(pageable))
                .thenReturn(pagedResult);

        // When
        PaginatedResponseDTO paginatedResponseDTO = userProfileServiceImpl.retrieveAllUserProfiles(0, 10);

        // Then Verify the repository methods are called correctly
        assertNotNull(paginatedResponseDTO);
        assertNotNull(paginatedResponseDTO.getContent());
        assertEquals(1, paginatedResponseDTO.getContent().size());
        verify(userProfileRepository).findAll(pageable);
        verifyNoMoreInteractions(userProfileRepository);
    }

    @Test
    void createUserProfile() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setPassword("12345");
        userRegistrationDTO.setEmail("hello@world.com");
        userRegistrationDTO.setFirstName("Hello");
        userRegistrationDTO.setLastName("World");
        userRegistrationDTO.setPhoneNumber("+23480000000000");

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        BeanUtils.copyProperties(userRegistrationDTO, userProfileDTO);


        when(userRegistrationServiceImpl.createProfile(any(), eq("USER")))
                .thenReturn(userProfileDTO);

        UserProfileDTO user = userRegistrationServiceImpl.createProfile(userRegistrationDTO, "USER");

        assertNotNull(user);
        verify(userRegistrationServiceImpl).createProfile(userRegistrationDTO, "USER");
        verifyNoMoreInteractions(userProfileRepository);
    }

}