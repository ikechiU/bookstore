package org.example.bookstore.service.auth.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.config.jwt.JwtHelper;
import org.example.bookstore.dto.auth.LoginRequestDTO;
import org.example.bookstore.dto.auth.LoginResponse;
import org.example.bookstore.dto.auth.RoleDTO;
import org.example.bookstore.dto.auth.UserProfileDTO;
import org.example.bookstore.enums.Status;
import org.example.bookstore.exception.BookStoreAuthenticationException;
import org.example.bookstore.model.auth.Permission;
import org.example.bookstore.model.auth.Role;
import org.example.bookstore.model.auth.UserAuthProfile;
import org.example.bookstore.repository.auth.UserAuthProfileRepository;
import org.example.bookstore.service.auth.LoginService;
import org.example.bookstore.service.auth.RoleService;
import org.example.bookstore.service.auth.UserAuthService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService, LoginService<LoginResponse, LoginRequestDTO> {

    private final UserAuthProfileRepository userAuthProfileRepository;
    private final RoleService<RoleDTO, Role> roleServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserAuthProfile user = userAuthProfileRepository
                .findFirstByUsernameOrderByCreatedDate(username)
                .orElseThrow(() -> new BookStoreAuthenticationException("Invalid username or password"));

        if (Status.INACTIVE.equals(user.getStatus())) {
            throw new BookStoreAuthenticationException("Account is not active");
        };
        return getUserProfileDTO(username, user);
    }

    @Override
    public LoginResponse authenticate(LoginRequestDTO loginRequestDTO) {
        UserProfileDTO userDetails = (UserProfileDTO) loadUserByUsername(loginRequestDTO.getEmail());
        if (passwordEncoder.matches(loginRequestDTO.getPassword(), userDetails.getPassword())) {
            return getLoginResponse(loginRequestDTO, userDetails);
        } else {
            throw new BookStoreAuthenticationException("Invalid username or password");
        }
    }

    private Set<SimpleGrantedAuthority> getGrantedAuthorities(String assignedRole) {
        Role role = roleServiceImpl.retrieveRole(assignedRole);
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (Permission permission : role.getPermissions()) {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
        }
        return authorities;
    }

    private UserProfileDTO getUserProfileDTO(String username, UserAuthProfile user) {
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUsername(username);
        userProfileDTO.setFirstName(user.getUserProfile().getFirstName());
        userProfileDTO.setLastName(user.getUserProfile().getLastName());
        userProfileDTO.setAssignedRole(user.getAssignedRole());
        userProfileDTO.setEmail(user.getUserProfile().getEmail());
        userProfileDTO.setProfileId(user.getUserProfile().getProfileId());
        userProfileDTO.setPassword(user.getPassword());
        userProfileDTO.setDefaultPassword(user.isDefaultPassword());
        userProfileDTO.setPermissions(getGrantedAuthorities(user.getAssignedRole()));

        return userProfileDTO;
    }

    private LoginResponse getLoginResponse(LoginRequestDTO loginRequestDTO, UserProfileDTO userDetails) {
        Map<String, String> claims = buildClaims(userDetails.getUsername(), userDetails);
        Map<String, Object> additionalInformation = new HashMap<>();
        additionalInformation.put("firstName", userDetails.getFirstName());
        additionalInformation.put("email", userDetails.getEmail());
        additionalInformation.put("role", userDetails.getAssignedRole());
        additionalInformation.put("name", userDetails.getFirstName().concat(" ").concat(userDetails.getLastName()));
        additionalInformation.put("isDefaultPassword", userDetails.isDefaultPassword());

        return LoginResponse.builder()
                .accessToken(jwtHelper.createJwtForClaims(loginRequestDTO.getEmail(), claims))
                .additionalInformation(additionalInformation)
                .build();
    }

    private static Map<String, String> buildClaims(String username, UserProfileDTO userDetails) {
        Map<String, String> claims = new HashMap<>();
        claims.put("username", username);
        String authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        claims.put("permissions", authorities);
        claims.put("email", userDetails.getEmail());
        claims.put("name", userDetails.getFirstName().concat(" ").concat(userDetails.getLastName()));
        claims.put("role", userDetails.getAssignedRole());
        claims.put("proceed", String.valueOf(userDetails.isDefaultPassword()));

        return claims;
    }


}

