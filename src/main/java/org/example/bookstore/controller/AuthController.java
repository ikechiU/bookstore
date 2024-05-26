package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.example.bookstore.dto.ControllerResponse;
import org.example.bookstore.dto.auth.LoginRequestDTO;
import org.example.bookstore.service.auth.impl.UserAuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-25(Sat)-2024
 */

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Authentication-Module", description = "This module contains all required APIs to complete authentication.")
@RequiredArgsConstructor
public class AuthController {

    private final UserAuthServiceImpl userAuthServiceImpl;

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate a user",
            description = "This API is used to authenticate a user."
    )
    public ResponseEntity<Object> authenticate(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return ControllerResponse.getResponse(
                "Authentication Successful", userAuthServiceImpl.authenticate(loginRequestDTO));
    }

}