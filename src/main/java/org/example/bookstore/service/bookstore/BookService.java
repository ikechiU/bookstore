package org.example.bookstore.service.bookstore;

import org.example.bookstore.dto.PaginatedResponseDTO;
import org.example.bookstore.dto.bookstore.BookDTO;
import org.example.bookstore.dto.bookstore.CompleteBookResponseDTO;
import org.example.bookstore.dto.bookstore.UpdateBookDTO;
import org.example.bookstore.enums.AvailabilityStatus;

import java.util.Set;
import java.util.UUID;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-25(Sat)-2024
 */

public interface BookService<V> {
    void addSystemBooks(Set<V> newPermissions);

    PaginatedResponseDTO retrieveAllBooks(int page, int size);

    Object getBook(UUID bookId);

    CompleteBookResponseDTO createBook(BookDTO bookDTO);

    CompleteBookResponseDTO updateBook(UUID bookId, UpdateBookDTO updateBookDTO);

    CompleteBookResponseDTO manageAvailabilityStatus(UUID bookId, AvailabilityStatus newStatus);

    void deleteBook(UUID bookId);

}
