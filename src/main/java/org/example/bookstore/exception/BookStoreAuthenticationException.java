package org.example.bookstore.exception;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

public class BookStoreAuthenticationException extends RuntimeException {
    public BookStoreAuthenticationException(String message) {
        super(message);
    }
}
