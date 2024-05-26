package org.example.bookstore.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@RequiredArgsConstructor
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseDTO<Object> buildErrorResponse(Object error, HttpStatus status) {
        return ResponseDTO.builder()
                .statusCode(String.valueOf(status.value()))
                .statusMessage(status.getReasonPhrase())
                .data(Collections.emptyList())
                .errors(Collections.singletonList(error))
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO<Object> handleValidationExceptions(MethodArgumentNotValidException e) {
        List<String> response = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            response.add(String.format("%s : %s", fieldName, errorMessage));
        });
        log.error(e.getMessage());
        return buildErrorResponse(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BookStoreException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO<Object> handleBookStoreException(BookStoreException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            BookStoreAuthenticationException.class,
            AuthenticationException.class,
            InvalidBearerTokenException.class,
            AccessDeniedException.class,
            InsufficientAuthenticationException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseDTO<Object> handleAuthenticationException(Exception ex) {
        String message = "You are not authorized to make this request";

        if (ex instanceof InvalidBearerTokenException || ex instanceof BadCredentialsException) {
            message = "The access token supplied is invalid or expired";
        }

        if (ex instanceof BookStoreAuthenticationException) {
            message = ex.getMessage();
        }

        log.error("{}:: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return buildErrorResponse(message, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDTO<Object> handleException(Exception ex) {
        log.error("ERROR OCCURRED! {}", ex.getMessage());
        return buildErrorResponse("Something went wrong, please try again", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}