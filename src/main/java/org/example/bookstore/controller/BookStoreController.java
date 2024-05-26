package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.ControllerResponse;
import org.example.bookstore.dto.bookstore.BookDTO;
import org.example.bookstore.dto.bookstore.UpdateBookDTO;
import org.example.bookstore.enums.AvailabilityStatus;
import org.example.bookstore.service.bookstore.impl.BookServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-25(Sat)-2024
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Tag(name = "BookStore-Module", description = "This module contains all required APIs for the bookstore. ")
public class BookStoreController {

    private final BookServiceImpl bookServiceImpl;

    @GetMapping("/books")
    @Operation(
            summary = "Fetch all books",
            description = "This API is used to get all the books in the system. You will have access to synopsis without authentication")
    public ResponseEntity<Object> getBooks(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        return ControllerResponse.getResponse(bookServiceImpl.retrieveAllBooks(page, limit));
    }

    @GetMapping("/books-{bookId}")
    @Operation(
            summary = "Fetch one book",
            description = "This API is used to view a single book in the system. You will have access to synopsis without authentication")
    public ResponseEntity<Object> getBook(@PathVariable UUID bookId) {
        return ControllerResponse.getResponse(bookServiceImpl.getBook(bookId));
    }

    @PostMapping("/books")
    @PreAuthorize("hasAuthority('write-book')")
    @Operation(
            summary = "Add a book",
            description = "This API is used to add a single book into the system. This endpoint is protected")
    public ResponseEntity<Object> createBook(@Valid @RequestBody BookDTO bookDTO) {
        return ControllerResponse.getResponse(bookServiceImpl.createBook(bookDTO));
    }

    @PutMapping("/books-{bookId}")
    @PreAuthorize("hasAuthority('write-book')")
    @Operation(
            summary = "Update a book",
            description = "This API is used to update a single book in the system. This endpoint is protected")
    public ResponseEntity<Object> updateBook(@PathVariable UUID bookId, @RequestBody UpdateBookDTO updateBookDTO) {
        return ControllerResponse.getResponse(bookServiceImpl.updateBook(bookId, updateBookDTO));
    }

    @PostMapping("/books-{bookId}")
    @PreAuthorize("hasAuthority('write-book')")
    @Operation(
            summary = "Manage availability status of a book",
            description = "This API is used to manage the availability status of a book. This endpoint is protected")
    public ResponseEntity<Object> manageAvailabilityStatusOfBook(@PathVariable UUID bookId, @RequestParam AvailabilityStatus availabilityStatus) {
        return ControllerResponse.getResponse(bookServiceImpl.manageAvailabilityStatus(bookId, availabilityStatus));
    }

    @DeleteMapping("/books-{bookId}")
    @PreAuthorize("hasAuthority('delete-book')")
    @Operation(
            summary = "Delete a book",
            description = "This API is used to delete a single book in the system. This endpoint is protected")
    public ResponseEntity<Object> deleteBook(@PathVariable UUID bookId) {
        bookServiceImpl.deleteBook(bookId);
        return ControllerResponse.getResponse("Book successfully deleted");
    }

}
