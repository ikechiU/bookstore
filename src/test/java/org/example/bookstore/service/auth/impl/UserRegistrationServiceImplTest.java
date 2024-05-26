package org.example.bookstore.service.auth.impl;

import org.example.bookstore.config.properties.AppPropertyConfig;
import org.example.bookstore.dto.auth.RoleDTO;
import org.example.bookstore.dto.auth.UserProfileDTO;
import org.example.bookstore.dto.auth.UserRegistrationDTO;
import org.example.bookstore.enums.Status;
import org.example.bookstore.exception.BookStoreException;
import org.example.bookstore.model.auth.Permission;
import org.example.bookstore.model.auth.Role;
import org.example.bookstore.model.auth.UserAuthProfile;
import org.example.bookstore.model.auth.UserProfile;
import org.example.bookstore.repository.auth.UserAuthProfileRepository;
import org.example.bookstore.repository.auth.UserProfileRepository;
import org.example.bookstore.service.auth.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceImplTest {

    private static final UUID profileId = UUID.randomUUID();
    @Mock
    private UserProfileRepository userProfileRepository;
    @Mock
    private UserAuthProfileRepository userAuthProfileRepository;
    @Mock
    private RoleService<RoleDTO, Role> roleServiceImpl;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AppPropertyConfig propertyConfig;
    @InjectMocks
    UserRegistrationServiceImpl userRegistrationServiceImpl;

    @Test
    void systemUsersChecks() {
        //Mock repository behavior
        when(propertyConfig.getSystemDefaultSuperAdmin())
                .thenReturn("superadmin@bookstore.com");
        when(propertyConfig.getSystemDefaultAdmin())
                .thenReturn("admin@bookstore.com");
        when(propertyConfig.getSystemDefaultUser())
                .thenReturn("reader@bookstore.com");
        when(propertyConfig.getSystemDefaultPassword())
                .thenReturn("B00kSt0r3Df@Pwd");

        when(userProfileRepository.existsByEmail(any()))
                .thenReturn(false);
        when(userProfileRepository.existsByEmail(any()))
                .thenReturn(false);
        when(userProfileRepository.existsByEmail(any()))
                .thenReturn(false);

        createSystemProfile("SUPER_ADMIN");
        createSystemProfile("ADMIN");
        createSystemProfile("USER");

        // When
        userRegistrationServiceImpl.systemUsersChecks();

        // Then Verify the repository methods are called correctly
        verify(userAuthProfileRepository, times(3)).existsByUsername(any());
        verifyNoMoreInteractions(userAuthProfileRepository);
    }

    void createSystemProfile(String roleName) {
        Set<Permission> superAdminPermission = Set.of(permissions().get(0), permissions().get(1), permissions().get(2), permissions().get(3), permissions().get(4), permissions().get(5), permissions().get(6));
        Set<Permission> adminPermission = Set.of(permissions().get(0), permissions().get(1), permissions().get(2));
        Set<Permission> userPermission = Set.of(permissions().get(0));

        Role role = new Role();
        switch (roleName) {
            case "SUPER_ADMIN" -> {
                when(userAuthProfileRepository.save(any()))
                        .thenReturn(getSuperAdminAuthProfile());

                role.setName("SUPER_ADMIN");
                role.setDescription("SUPER_ADMIN");
                role.setPermissions(superAdminPermission);
            }
            case "ADMIN" -> {
                when(userAuthProfileRepository.save(any()))
                        .thenReturn(getAdminAuthProfile());

                role.setName("ADMIN");
                role.setDescription("ADMIN");
                role.setPermissions(adminPermission);
            }
            case "USER" -> {
                when(userAuthProfileRepository.save(any()))
                        .thenReturn(getUserAuthProfile());

                role.setName("USER");
                role.setDescription("USER");
                role.setPermissions(userPermission);
            }
        }

        when(roleServiceImpl.retrieveRole(roleName))
                .thenReturn(role);

        when(passwordEncoder.encode(any()))
                .thenReturn("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJz");
    }

    @Test
    void createProfile() {
        when(userProfileRepository.existsByEmail("hello@world.com"))
                .thenReturn(false);

        Permission permission = new Permission();
        permission.setName("read");
        Role role = new Role();
        role.setPermissions(Set.of(permission));

        when(roleServiceImpl.retrieveRole("USER"))
                .thenReturn(role);

        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setPassword("12345");
        userRegistrationDTO.setEmail("hello@world.com");
        userRegistrationDTO.setFirstName("Hello");
        userRegistrationDTO.setLastName("World");
        userRegistrationDTO.setPhoneNumber("+23480000000000");

        UserProfile userProfile = new UserProfile();
        BeanUtils.copyProperties(userRegistrationDTO, userProfile);

        when(userProfileRepository.save(any()))
                .thenReturn(userProfile);

        when(userAuthProfileRepository.existsByUsername(userRegistrationDTO.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode(userRegistrationDTO.getPassword()))
                .thenReturn("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJz");

        when(userAuthProfileRepository.save(any()))
                .thenReturn(getUserAuthProfile());

        UserProfileDTO user = userRegistrationServiceImpl.createProfile(userRegistrationDTO, "USER");

        assertNotNull(user);
        assertNotNull(user.getAssignedRole());
        verify(roleServiceImpl, times(1)).retrieveRole("USER");
        verify(userProfileRepository, times(1)).existsByEmail(userRegistrationDTO.getEmail());
        verify(userAuthProfileRepository, times(1)).existsByUsername(userRegistrationDTO.getEmail());
        verify(userProfileRepository).save(any());
        verify(userAuthProfileRepository).save(any());
        verify(passwordEncoder, times(1)).encode(userRegistrationDTO.getPassword());
        verifyNoMoreInteractions(userProfileRepository);
    }

    private UserAuthProfile getSuperAdminAuthProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(1L);
        userProfile.setFirstName("SUPER_ADMIN");
        userProfile.setLastName("SUPER_ADMIN");
        userProfile.setEmail("SUPER_ADMIN");
        userProfile.setProfileId(UUID.randomUUID());

        UserAuthProfile user = new UserAuthProfile();
        user.setId(1L);
        user.setAssignedRole("USER");
        user.setPassword("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJz");
        user.setDefaultPassword(true);
        user.setUserProfile(userProfile);
        user.setStatus(Status.PENDING);

        return user;
    }

    private UserAuthProfile getAdminAuthProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(2L);
        userProfile.setFirstName("ADMIN");
        userProfile.setLastName("ADMIN");
        userProfile.setEmail("ADMIN");
        userProfile.setProfileId(UUID.randomUUID());

        UserAuthProfile user = new UserAuthProfile();
        user.setId(2L);
        user.setAssignedRole("ADMIN");
        user.setPassword("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJz");
        user.setDefaultPassword(true);
        user.setUserProfile(userProfile);
        user.setStatus(Status.PENDING);

        return user;
    }

    private UserAuthProfile getUserAuthProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(3L);
        userProfile.setFirstName("USER");
        userProfile.setLastName("USER");
        userProfile.setEmail("USER");
        userProfile.setProfileId(profileId);

        UserAuthProfile user = new UserAuthProfile();
        user.setId(3L);
        user.setAssignedRole("USER");
        user.setPassword("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJz");
        user.setDefaultPassword(true);
        user.setUserProfile(userProfile);
        user.setStatus(Status.PENDING);

        return user;
    }


    @Test
    void getUserProfileDTO() {

    }

    private List<Permission> permissions() {
        return RoleList.permissions();
    }


}