package org.example.bookstore.service.auth.impl;

import org.example.bookstore.config.jwt.JwtHelper;
import org.example.bookstore.dto.auth.LoginRequestDTO;
import org.example.bookstore.dto.auth.LoginResponse;
import org.example.bookstore.dto.auth.RoleDTO;
import org.example.bookstore.enums.Status;
import org.example.bookstore.exception.BookStoreAuthenticationException;
import org.example.bookstore.model.auth.Permission;
import org.example.bookstore.model.auth.Role;
import org.example.bookstore.model.auth.UserAuthProfile;
import org.example.bookstore.model.auth.UserProfile;
import org.example.bookstore.repository.auth.UserAuthProfileRepository;
import org.example.bookstore.service.auth.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@ExtendWith(MockitoExtension.class)
class UserAuthServiceImplTest {

    private static final UUID profileId = UUID.randomUUID();

    @Mock
    private UserAuthProfileRepository userAuthProfileRepository;
    @Mock
    private RoleService<RoleDTO, Role> roleServiceImpl;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtHelper jwtHelper;
    @InjectMocks
    private UserAuthServiceImpl userAuthServiceImpl;


    @Test
    void loadUserByUsername() {
        String username = "user@bookstore.com";

        Permission permission = new Permission();
        permission.setName("read");
        Role role = new Role();
        role.setPermissions(Set.of(permission));

        // Mock repository behavior
        when(userAuthProfileRepository.findFirstByUsernameOrderByCreatedDate(username))
                .thenReturn(Optional.of(getUserAuthProfile()));
        when(roleServiceImpl.retrieveRole("USER"))
                .thenReturn(role);

        // When
        UserDetails userDetails = userAuthServiceImpl.loadUserByUsername(username);

        // Then Verify the repository methods are called correctly
        assertNotNull(userDetails);
        verify(userAuthProfileRepository).findFirstByUsernameOrderByCreatedDate(username);
        verifyNoMoreInteractions(userAuthProfileRepository);
    }

    @Test
    void loadUserByUsername_Username_Does_Not_Exist() {
        String username = "user@bookstore.com";

        when(userAuthProfileRepository.findFirstByUsernameOrderByCreatedDate(username))
                .thenReturn(Optional.empty());

        BookStoreAuthenticationException thrown = assertThrows(BookStoreAuthenticationException.class,
                () -> userAuthServiceImpl.loadUserByUsername(username));

        assertEquals("Invalid username or password", thrown.getMessage());
        verify(userAuthProfileRepository).findFirstByUsernameOrderByCreatedDate(username);
        verifyNoMoreInteractions(userAuthProfileRepository);
    }

    @Test
    void loadUserByUsername_Status_Inactive() {
        String username = "user@bookstore.com";

        UserAuthProfile userAuthProfile = getUserAuthProfile();
        userAuthProfile.setStatus(Status.INACTIVE);

        when(userAuthProfileRepository.findFirstByUsernameOrderByCreatedDate(username))
                .thenReturn(Optional.of(userAuthProfile));

        BookStoreAuthenticationException thrown = assertThrows(BookStoreAuthenticationException.class,
                () -> userAuthServiceImpl.loadUserByUsername(username));

        assertEquals("Account is not active", thrown.getMessage());
        verify(userAuthProfileRepository).findFirstByUsernameOrderByCreatedDate(username);
        verifyNoMoreInteractions(userAuthProfileRepository);
    }

    @Test
    void authenticate() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("user@bookstore.com", "B00kSt0r3Df@Pwd");

        Permission permission = new Permission();
        permission.setName("read");
        Role role = new Role();
        role.setPermissions(Set.of(permission));

        when(userAuthProfileRepository.findFirstByUsernameOrderByCreatedDate(loginRequestDTO.getEmail()))
                .thenReturn(Optional.of(getUserAuthProfile()));

        when(roleServiceImpl.retrieveRole("USER"))
                .thenReturn(role);

        when(passwordEncoder.matches(loginRequestDTO.getPassword(), getUserAuthProfile().getPassword()))
                .thenReturn(true);

        when(jwtHelper.createJwtForClaims(any(), anyMap()))
                .thenReturn("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyZWFkZXJAYm9va3N0b3JlLmNvbSIsInJvbGUiOiJVU0VSI");

        LoginResponse authenticate = userAuthServiceImpl.authenticate(loginRequestDTO);

        assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyZWFkZXJAYm9va3N0b3JlLmNvbSIsInJvbGUiOiJVU0VSI", authenticate.getAccessToken());
        verify(passwordEncoder).matches(loginRequestDTO.getPassword(), getUserAuthProfile().getPassword());
        verifyNoMoreInteractions(userAuthProfileRepository);
    }


    @Test
    void authenticate_Failed() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("user@bookstore.com", "B00kSt0r3Df@Pwd");

        Permission permission = new Permission();
        permission.setName("read");
        Role role = new Role();
        role.setPermissions(Set.of(permission));

        when(userAuthProfileRepository.findFirstByUsernameOrderByCreatedDate(loginRequestDTO.getEmail()))
                .thenReturn(Optional.of(getUserAuthProfile()));

        when(roleServiceImpl.retrieveRole("USER"))
                .thenReturn(role);

        when(passwordEncoder.matches(loginRequestDTO.getPassword(), getUserAuthProfile().getPassword()))
                .thenReturn(false);

        BookStoreAuthenticationException thrown = assertThrows(BookStoreAuthenticationException.class,
                () -> userAuthServiceImpl.authenticate(loginRequestDTO));

        assertEquals("Invalid username or password", thrown.getMessage());
        verify(passwordEncoder).matches(loginRequestDTO.getPassword(), getUserAuthProfile().getPassword());
        verifyNoMoreInteractions(userAuthProfileRepository);
    }

    private UserAuthProfile getUserAuthProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(1L);
        userProfile.setFirstName("USER");
        userProfile.setLastName("USER");
        userProfile.setEmail("USER");
        userProfile.setProfileId(profileId);

        UserAuthProfile user = new UserAuthProfile();
        user.setId(1L);
        user.setAssignedRole("USER");
        user.setPassword("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJz");
        user.setDefaultPassword(true);
        user.setUserProfile(userProfile);
        user.setStatus(Status.PENDING);

        return user;
    }

}