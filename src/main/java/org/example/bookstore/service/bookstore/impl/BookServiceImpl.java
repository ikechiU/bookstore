package org.example.bookstore.service.bookstore.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.bookstore.dto.PaginatedResponseDTO;
import org.example.bookstore.dto.bookstore.BookDTO;
import org.example.bookstore.dto.bookstore.CompleteBookResponseDTO;
import org.example.bookstore.dto.bookstore.PartialBookResponseDTO;
import org.example.bookstore.dto.bookstore.UpdateBookDTO;
import org.example.bookstore.enums.AvailabilityStatus;
import org.example.bookstore.exception.BookStoreException;
import org.example.bookstore.model.bookstore.Book;
import org.example.bookstore.repository.bookstore.BookRepository;
import org.example.bookstore.service.bookstore.BookService;
import org.example.bookstore.util.AppUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.example.bookstore.util.AppUtil.getPageable;


/**
 * @author Ikechi Ucheagwu
 * @createdOn May-25(Sat)-2024
 */

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService<Book> {

    private final BookRepository bookRepository;

    @Override
    public void addSystemBooks(Set<Book> books) {
        books.forEach((book) -> {
            Optional<Book> existingBook = bookRepository.findFirstByTitleAndAuthor(book.getTitle(), book.getAuthor());
            if (existingBook.isPresent()) {
                book.setId(existingBook.get().getId());
                book.setBookId(existingBook.get().getBookId());
                book.setBookYear(existingBook.get().getBookYear());
                book.setDeleted(existingBook.get().isDeleted());
                book.setGenre(existingBook.get().getGenre());
                book.setAuthor(existingBook.get().getAuthor());
                book.setSynopsis(existingBook.get().getSynopsis());
                book.setContent(existingBook.get().getContent());
            }

        });
        bookRepository.saveAll(books);
    }

    @Override
    public PaginatedResponseDTO retrieveAllBooks(int page, int size) {
        Page<Book> pagedBooks = bookRepository.findAll(getPageable(page, size));
        return getPaginatedResponse(pagedBooks);
    }

    @Override
    public Object getBook(UUID bookId) {
        Book book = findOneBook(bookId);
        return getBook(book);
    }

    @Override
    public CompleteBookResponseDTO createBook(BookDTO bookDTO) {
        Optional<Book> existingBook = bookRepository.findFirstByTitleAndAuthor(bookDTO.getTitle(), bookDTO.getAuthor());
        if (existingBook.isPresent()) {
            throw new BookStoreException("A book with the same title and author already exist.");
        }

        if (bookDTO.getAvailabilityStatus().equals(AvailabilityStatus.DISCONTINUED)) {
            throw new BookStoreException("A new book cannot be discontinued.");
        }

        if (bookDTO.getAvailabilityStatus().equals(AvailabilityStatus.OUT_OF_STOCK)) {
            throw new BookStoreException("A new book cannot be out of stock.");
        }

        Book book = new Book();
        BeanUtils.copyProperties(bookDTO, book);
        String synopsis = bookDTO.getSynopsis().concat("\n\n[SIGN UP ... to read complete story]");
        book.setSynopsis(synopsis);
        book.setBookId(UUID.randomUUID());

        Book savedBook = bookRepository.save(book);
        return mapCompleteBook(savedBook);
    }

    @Override
    public CompleteBookResponseDTO updateBook(UUID bookId, UpdateBookDTO updateBookDTO) {
        Book book = findOneBook(bookId);

        String newTitle = StringUtils.isNotBlank(updateBookDTO.getTitle()) ? updateBookDTO.getTitle() : book.getTitle();
        String newAuthor = StringUtils.isNotBlank(updateBookDTO.getAuthor()) ? updateBookDTO.getAuthor() : book.getAuthor();

        // Check for an existing book with the same title and author, excluding the current book being updated
        Optional<Book> optionalBook = bookRepository.findFirstByTitleAndAuthor(newTitle, newAuthor);
        if (optionalBook.isPresent() && !optionalBook.get().getBookId().equals(bookId)) {
            // If a different book with the same title and author exists, we throw an exception
            throw new BookStoreException("A book with the same title and author already exists.");
        }

        String bookYear = StringUtils.isNotBlank(updateBookDTO.getBookYear()) ? updateBookDTO.getBookYear() : book.getBookYear();
        String genre = StringUtils.isNotBlank(updateBookDTO.getGenre()) ? updateBookDTO.getGenre() : book.getGenre();
        String synopsis = StringUtils.isNotBlank(updateBookDTO.getSynopsis()) ?
                updateBookDTO.getSynopsis().concat("\n\n[SIGN UP ... to read complete story]") : book.getSynopsis();
        String content = StringUtils.isNotBlank(updateBookDTO.getContent()) ? updateBookDTO.getContent() : book.getContent();

        book.setBookYear(bookYear);
        book.setGenre(genre);
        book.setTitle(newTitle);
        book.setAuthor(newAuthor);
        book.setSynopsis(synopsis);
        book.setContent(content);

        Book updatedBook = bookRepository.save(book);
        return mapCompleteBook(updatedBook);
    }

    @Override
    public CompleteBookResponseDTO manageAvailabilityStatus(UUID bookId, AvailabilityStatus newStatus) {
        Book book = findOneBook(bookId);

        if (book.getAvailabilityStatus() == newStatus) {
            throw new BookStoreException("No change made to book availability status.");
        }

        if (newStatus == AvailabilityStatus.PRE_ORDER && book.getAvailabilityStatus() != AvailabilityStatus.OUT_OF_STOCK) {
            throw new BookStoreException("Only preorder books that are out of stock.");
        }

        if (book.getAvailabilityStatus() == AvailabilityStatus.DISCONTINUED) {
            throw new BookStoreException(String.format("Cannot mark a discontinued book as %s.", newStatus.getDisplayName().toLowerCase()));
        }

        book.setAvailabilityStatus(newStatus);
        Book updatedBook = bookRepository.save(book);
        return mapCompleteBook(updatedBook);
    }

    @Override
    public void deleteBook(UUID bookId) {
        Book book = findOneBook(bookId);
        book.setDeleted(true);
        bookRepository.save(book);
    }

    private Book findOneBook(UUID bookId) {
        return bookRepository.findOneByBookId(bookId)
                .orElseThrow(()-> new BookStoreException(String.format("Book with id %s does not exist", bookId)));
    }

    private CompleteBookResponseDTO mapCompleteBook(Book book) {
        CompleteBookResponseDTO completeBookResponseDTO = new CompleteBookResponseDTO();
        BeanUtils.copyProperties(book, completeBookResponseDTO);
        String synopsis = completeBookResponseDTO.getSynopsis().replace("\n\n[SIGN UP ... to read complete story]", "");
        completeBookResponseDTO.setSynopsis(synopsis);
        return completeBookResponseDTO;
    }

    private PartialBookResponseDTO mapPartialBook(Book book) {
        PartialBookResponseDTO partialBookResponseDTO = new PartialBookResponseDTO();
        BeanUtils.copyProperties(book, partialBookResponseDTO);
        return partialBookResponseDTO;
    }

    private List<CompleteBookResponseDTO> mapCompleteBook(List<Book> books) {
        return books.stream().map(this::mapCompleteBook)
                .toList();
    }

    private List<PartialBookResponseDTO> mapPartialBook(List<Book> books) {
        return books.stream().map(this::mapPartialBook)
                .toList();
    }

    private PaginatedResponseDTO getPaginatedResponse(Page<Book> books) {
        if (AppUtil.getLoggedInUsername().equals("SYSTEM") || AppUtil.getLoggedInUsername().equals("anonymousUser")) {
            return PaginatedResponseDTO.getPagination(books, mapPartialBook(books.getContent()));
        } else {
            return PaginatedResponseDTO.getPagination(books, mapCompleteBook(books.getContent()));
        }
    }

    private Object getBook(Book book) {
        if (AppUtil.getLoggedInUsername().equals("SYSTEM") || AppUtil.getLoggedInUsername().equals("anonymousUser")) {
            return mapPartialBook(book);
        } else {
            return mapCompleteBook(book);
        }
    }

}
