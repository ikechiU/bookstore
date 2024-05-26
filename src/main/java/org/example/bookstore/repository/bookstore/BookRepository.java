package org.example.bookstore.repository.bookstore;

import org.example.bookstore.model.bookstore.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-25(Fri)-2024
 */

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findFirstByTitleAndAuthor(String title, String author);
    Optional<Book> findOneByBookId(UUID bookId);

}
