package org.example.bookstore.service.auth;

import org.example.bookstore.exception.BookStoreException;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

public interface UserRegistrationService<T, V> {

    default void systemUsersChecks() {
        throw new BookStoreException("No contract found for this implementation");
    }

    default T createProfile(V request, String assignedRole) {
        throw new BookStoreException("No contract found for this implementation");
    }

}
