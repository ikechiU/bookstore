package org.example.bookstore.service.auth;

import org.example.bookstore.exception.BookStoreAuthenticationException;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

public interface LoginService<T, V> {
    default T authenticate(V request) {
        throw new BookStoreAuthenticationException("No implementation found for this contract");
    }
}