package org.example.bookstore.service.auth.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.config.properties.AppPropertyConfig;
import org.example.bookstore.dto.auth.PermissionDTO;
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
import org.example.bookstore.service.auth.UserRegistrationService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationServiceImpl implements UserRegistrationService<UserProfileDTO, UserRegistrationDTO> {

    private final UserProfileRepository userProfileRepository;
    private final UserAuthProfileRepository userAuthProfileRepository;
    private final RoleService<RoleDTO, Role> roleServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final AppPropertyConfig propertyConfig;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public void systemUsersChecks() {
        if (!userProfileRepository.existsByEmail(propertyConfig.getSystemDefaultSuperAdmin())) {
            UserRegistrationDTO superAdmin = new UserRegistrationDTO();
            superAdmin.setEmail(propertyConfig.getSystemDefaultSuperAdmin());
            superAdmin.setFirstName("SUPER_ADMIN");
            superAdmin.setLastName("SUPER_ADMIN");
            superAdmin.setPassword(propertyConfig.getSystemDefaultPassword());
            createProfile(superAdmin, "SUPER_ADMIN");
        }
        if (!userProfileRepository.existsByEmail(propertyConfig.getSystemDefaultAdmin())) {
            UserRegistrationDTO admin = new UserRegistrationDTO();
            admin.setEmail(propertyConfig.getSystemDefaultAdmin());
            admin.setFirstName("ADMIN");
            admin.setLastName("ADMIN");
            admin.setPassword(propertyConfig.getSystemDefaultPassword());
            createProfile(admin, "ADMIN");
        }
        if (!userProfileRepository.existsByEmail(propertyConfig.getSystemDefaultUser())) {
            UserRegistrationDTO user = new UserRegistrationDTO();
            user.setEmail(propertyConfig.getSystemDefaultUser());
            user.setFirstName("USER");
            user.setLastName("USER");
            user.setPassword(propertyConfig.getSystemDefaultPassword());
            createProfile(user, "USER");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public UserProfileDTO createProfile(UserRegistrationDTO request, String assignedRole) {
        boolean existsByEmail = userProfileRepository.existsByEmail(request.getEmail());
        if (existsByEmail) {
            log.error("Email already exist");
            throw new BookStoreException("Email already exist");
        }

        return saveNewProfile(request, roleServiceImpl.retrieveRole(assignedRole));
    }

    private UserProfileDTO saveNewProfile(UserRegistrationDTO request, Role role) {
        UserProfile userProfile = createUserProfile(request);

        boolean existsByEmail = userAuthProfileRepository.existsByUsername(request.getEmail());
        if (existsByEmail) {
            log.error("Email already exist");
            throw new BookStoreException("Email already exist");
        }
        UserAuthProfile userAuthProfile = new UserAuthProfile();
        BeanUtils.copyProperties(request, userAuthProfile);
        userAuthProfile.setUserProfile(userProfile);
        userAuthProfile.setUsername(request.getEmail());
        userAuthProfile.setPassword(passwordEncoder.encode(request.getPassword()));
        userAuthProfile.setDefaultPassword(true);
        userAuthProfile.setStatus(Status.PENDING);
        userAuthProfile.setAssignedRole(role.getName());
        userAuthProfile.setPermissions(new HashSet<>(role.getPermissions()));
        UserAuthProfile savedAuthProfile = userAuthProfileRepository.save(userAuthProfile);

        return getUserProfileDTO(savedAuthProfile, request);
    }

    private UserProfile createUserProfile(UserRegistrationDTO request) {
        UserProfile userProfile = new UserProfile();
        BeanUtils.copyProperties(request, userProfile);
        userProfile.setProfileId(UUID.randomUUID());
        userProfileRepository.save(userProfile);
        return userProfile;
    }

    public static UserProfileDTO getUserProfileDTO(UserAuthProfile userAuthProfile, UserRegistrationDTO registrationRequest) {
        UserProfileDTO profileDto = new UserProfileDTO();
        BeanUtils.copyProperties(userAuthProfile, profileDto);

        profileDto.setPassword(registrationRequest.getPassword());
        profileDto.setUserPermissions(permissionDTOS(userAuthProfile));
        profileDto.setFirstName(registrationRequest.getFirstName());
        profileDto.setLastName(registrationRequest.getLastName());
        profileDto.setUsername(registrationRequest.getEmail());
        profileDto.setEmail(userAuthProfile.getUsername());
        profileDto.setProfileId(userAuthProfile.getUserProfile().getProfileId());
        profileDto.setPermissions(authorities(userAuthProfile));

        return profileDto;
    }

    private static Set<PermissionDTO> permissionDTOS(UserAuthProfile userAuthProfile) {
        Set<Permission> permissions = userAuthProfile.getPermissions();
        Set<PermissionDTO> permissionDTOS = new HashSet<>();
        for (Permission permission: permissions) {
            permissionDTOS.add(PermissionDTO.builder()
                    .name(permission.getName())
                    .description(permission.getDescription())
                    .permissionType(permission.getPermissionType())
                    .build());
        }
        return permissionDTOS;
    }

    private static Set<SimpleGrantedAuthority> authorities(UserAuthProfile userAuthProfile) {
        return userAuthProfile.getPermissions().stream()
                .map(Permission::getName)
                .collect(Collectors.toSet())
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

}
