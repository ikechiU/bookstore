package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.ControllerResponse;
import org.example.bookstore.dto.auth.UserRegistrationDTO;
import org.example.bookstore.service.auth.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-25(Sat)-2024
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Tag(name = "User-Module", description = "This module contains all required APIs to complete user activities.")
public class UserController {

    private final UserProfileService userProfileServiceImpl;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('read-profile')")
    @Operation(
            summary = "Fetch all users",
            description = "This API is used to fetch all users.")
    public ResponseEntity<Object> getUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        return ControllerResponse.getResponse(userProfileServiceImpl.retrieveAllUserProfiles(page, limit));
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('create-profile')")
    @Operation(
            summary = "Create a user",
            description = "This API is used to create a user.")
    public ResponseEntity<Object> createUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        return ControllerResponse.getResponse(userProfileServiceImpl.createUserProfile(userRegistrationDTO));
    }

}

