package org.example.bookstore.service.bookstore.impl;

import org.example.bookstore.dto.PaginatedResponseDTO;
import org.example.bookstore.dto.bookstore.BookDTO;
import org.example.bookstore.dto.bookstore.CompleteBookResponseDTO;
import org.example.bookstore.dto.bookstore.PartialBookResponseDTO;
import org.example.bookstore.dto.bookstore.UpdateBookDTO;
import org.example.bookstore.enums.AvailabilityStatus;
import org.example.bookstore.enums.BookGenre;
import org.example.bookstore.exception.BookStoreException;
import org.example.bookstore.model.bookstore.Book;
import org.example.bookstore.repository.bookstore.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-25(Sat)-2024
 */

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;


    @Test
    void addSystemBooks() {
        Set<Book> books = Set.of(books().get(0), books().get(1));
        // Mock repository behavior
        when(bookRepository.findFirstByTitleAndAuthor("Hello World", "Sam Ala"))
                .thenReturn(Optional.of(books().get(0))); // Mocking existing book for book1
        when(bookRepository.findFirstByTitleAndAuthor("Wonder Man", "Gary Sam"))
                .thenReturn(Optional.empty()); // No existing book for book2

        // When
        bookServiceImpl.addSystemBooks(books);

        // Then Verify the repository methods are called correctly
        verify(bookRepository).findFirstByTitleAndAuthor("Hello World", "Sam Ala");
        verify(bookRepository).findFirstByTitleAndAuthor("Wonder Man", "Gary Sam");
        verify(bookRepository).saveAll(books);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void retrieveAllBooks() {
        Page<Book> pagedBooks = new PageImpl<>(books());
        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "id"));
        when(bookRepository.findAll(pageable))
                .thenReturn(pagedBooks);

        PaginatedResponseDTO paginatedResponseDTO = bookServiceImpl.retrieveAllBooks(0, 20);

        assertNotNull(paginatedResponseDTO);
        assertNotNull(paginatedResponseDTO.getContent());
        assertEquals(2, paginatedResponseDTO.getContent().size());
        verify(bookRepository).findAll(pageable);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void getBook_Success() {
        UUID bookId = UUID.fromString("c382e7d3-8fb6-4f6d-9255-3a44b1e99409");
        when(bookRepository.findOneByBookId(bookId))
                .thenReturn(Optional.of(books().get(0)));

        PartialBookResponseDTO partialBookResponseDTO = (PartialBookResponseDTO) bookServiceImpl.getBook(bookId);

        assertNotNull(partialBookResponseDTO);
        assertNotNull(partialBookResponseDTO.getSynopsis());
        verify(bookRepository).findOneByBookId(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void getBook_Failed() {
        UUID bookId = UUID.fromString("c382e7d3-8fb6-4f6d-9255-3a44b1e99409");
        when(bookRepository.findOneByBookId(bookId))
                .thenReturn(Optional.empty());

        BookStoreException thrown = assertThrows(BookStoreException.class,
                () -> bookServiceImpl.getBook(bookId));

        assertEquals(thrown.getMessage(), String.format("Book with id %s does not exist", bookId));
        verify(bookRepository).findOneByBookId(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void createBook_Success() {
        when(bookRepository.findFirstByTitleAndAuthor("Hello World", "Sam Ala"))
                .thenReturn(Optional.empty());

        BookDTO bookDTO = new BookDTO();
        BeanUtils.copyProperties(books().get(0), bookDTO);
        bookDTO.setAvailabilityStatus(AvailabilityStatus.IN_STOCK);

        Book book = new Book();
        BeanUtils.copyProperties(bookDTO, book);

        when(bookRepository.save(any(Book.class)))
                .thenReturn(book);

        CompleteBookResponseDTO completeBookResponseDTO = bookServiceImpl.createBook(bookDTO);

        assertNotNull(completeBookResponseDTO);
        assertNotNull(completeBookResponseDTO.getSynopsis());
        assertFalse(completeBookResponseDTO.getSynopsis().contains("\n\n[SIGN UP ... to read complete story]"));
        verify(bookRepository).findFirstByTitleAndAuthor("Hello World", "Sam Ala");
        verify(bookRepository).save(any(Book.class));
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void createBook_Failed_Title_And_Author_Exist() {
        when(bookRepository.findFirstByTitleAndAuthor("Hello World", "Sam Ala"))
                .thenReturn(Optional.of(books().get(0)));

        BookDTO bookDTO = new BookDTO();
        BeanUtils.copyProperties(books().get(0), bookDTO);
        bookDTO.setAvailabilityStatus(AvailabilityStatus.IN_STOCK);

        BookStoreException thrown = assertThrows(BookStoreException.class,
                () -> bookServiceImpl.createBook(bookDTO));

        assertEquals(thrown.getMessage(), "A book with the same title and author already exist.");
        verify(bookRepository).findFirstByTitleAndAuthor("Hello World", "Sam Ala");
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void createBook_Failed_Availability_Status_Discontinued() {
        when(bookRepository.findFirstByTitleAndAuthor("Hello World", "Sam Ala"))
                .thenReturn(Optional.empty());

        BookDTO bookDTO = new BookDTO();
        BeanUtils.copyProperties(books().get(0), bookDTO);
        bookDTO.setAvailabilityStatus(AvailabilityStatus.DISCONTINUED);

        BookStoreException thrown = assertThrows(BookStoreException.class,
                () -> bookServiceImpl.createBook(bookDTO));

        assertEquals(thrown.getMessage(), "A new book cannot be discontinued.");
        verify(bookRepository).findFirstByTitleAndAuthor("Hello World", "Sam Ala");
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void updateBook_Success() {
        UUID bookId = UUID.fromString("c382e7d3-8fb6-4f6d-9255-3a44b1e99409");
        when(bookRepository.findOneByBookId(bookId))
                .thenReturn(Optional.of(books().get(0)));

        when(bookRepository.findFirstByTitleAndAuthor("Hello World", "Sam Ala"))
                .thenReturn(Optional.empty());

        Book book = new Book();
        BeanUtils.copyProperties(books().get(0), book);
        book.setAvailabilityStatus(AvailabilityStatus.IN_STOCK);

        when(bookRepository.save(any(Book.class)))
                .thenReturn(book);

        UpdateBookDTO updateBookDTO = new UpdateBookDTO();
        BeanUtils.copyProperties(books().get(0), updateBookDTO);

        CompleteBookResponseDTO completeBookResponseDTO = bookServiceImpl.updateBook(bookId, updateBookDTO);

        assertNotNull(completeBookResponseDTO);
        assertNotNull(completeBookResponseDTO.getSynopsis());
        assertFalse(completeBookResponseDTO.getSynopsis().contains("\n\n[SIGN UP ... to read complete story]"));
        verify(bookRepository).findOneByBookId(bookId);
        verify(bookRepository).findFirstByTitleAndAuthor("Hello World", "Sam Ala");
        verify(bookRepository).save(any(Book.class));
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void updateBook_Failed() {
        UUID bookId = UUID.fromString("c382e7d3-8fb6-4f6d-9255-3a44b1e99409");
        when(bookRepository.findOneByBookId(bookId))
                .thenReturn(Optional.of(books().get(0)));

        when(bookRepository.findFirstByTitleAndAuthor("Hello World", "Sam Ala"))
                .thenReturn(Optional.of(books().get(1)));

        UpdateBookDTO updateBookDTO = new UpdateBookDTO();
        BeanUtils.copyProperties(books().get(0), updateBookDTO);

        BookStoreException thrown = assertThrows(BookStoreException.class,
                () -> bookServiceImpl.updateBook(bookId, updateBookDTO));

        assertEquals(thrown.getMessage(), "A book with the same title and author already exists.");
        verify(bookRepository).findOneByBookId(bookId);
        verify(bookRepository).findFirstByTitleAndAuthor("Hello World", "Sam Ala");
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void manageAvailabilityStatus_Success() {
        UUID bookId = UUID.fromString("c382e7d3-8fb6-4f6d-9255-3a44b1e99409");
        Book book = books().get(0);
        book.setAvailabilityStatus(AvailabilityStatus.OUT_OF_STOCK);
        when(bookRepository.findOneByBookId(bookId))
                .thenReturn(Optional.of(book));

        Book newBook = new Book();
        BeanUtils.copyProperties(book, newBook);
        newBook.setAvailabilityStatus(AvailabilityStatus.PRE_ORDER);

        when(bookRepository.save(book))
                .thenReturn(newBook);

        CompleteBookResponseDTO completeBookResponseDTO = bookServiceImpl.manageAvailabilityStatus(bookId, AvailabilityStatus.PRE_ORDER);

        assertNotNull(completeBookResponseDTO);
        assertNotNull(completeBookResponseDTO.getSynopsis());
        assertFalse(completeBookResponseDTO.getSynopsis().contains("\n\n[SIGN UP ... to read complete story]"));
        verify(bookRepository).findOneByBookId(bookId);
        verify(bookRepository).save(book);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void manageAvailabilityStatus_Failed_Same_Availability_Status() {
        UUID bookId = UUID.fromString("c382e7d3-8fb6-4f6d-9255-3a44b1e99409");
        Book book = books().get(0);
        book.setAvailabilityStatus(AvailabilityStatus.PRE_ORDER);
        when(bookRepository.findOneByBookId(bookId))
                .thenReturn(Optional.of(book));

        BookStoreException thrown = assertThrows(BookStoreException.class,
                () -> bookServiceImpl.manageAvailabilityStatus(bookId, AvailabilityStatus.PRE_ORDER));

        assertEquals("No change made to book availability status.", thrown.getMessage());
        verify(bookRepository).findOneByBookId(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void manageAvailabilityStatus_Failed_Only_PreOder_Books_In_Out_Of_Stock() {
        UUID bookId = UUID.fromString("c382e7d3-8fb6-4f6d-9255-3a44b1e99409");
        Book book = books().get(0);
        book.setAvailabilityStatus(AvailabilityStatus.IN_STOCK);
        when(bookRepository.findOneByBookId(bookId))
                .thenReturn(Optional.of(book));

        BookStoreException thrown = assertThrows(BookStoreException.class,
                () -> bookServiceImpl.manageAvailabilityStatus(bookId, AvailabilityStatus.PRE_ORDER));

        assertEquals("Only preorder books that are out of stock.", thrown.getMessage());
        verify(bookRepository).findOneByBookId(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void manageAvailabilityStatus_Failed_Cannot_Change_Discontinued_Availability_Status() {
        UUID bookId = UUID.fromString("c382e7d3-8fb6-4f6d-9255-3a44b1e99409");
        Book book = books().get(0);
        book.setAvailabilityStatus(AvailabilityStatus.DISCONTINUED);
        when(bookRepository.findOneByBookId(bookId))
                .thenReturn(Optional.of(book));

        BookStoreException thrown = assertThrows(BookStoreException.class,
                () -> bookServiceImpl.manageAvailabilityStatus(bookId, AvailabilityStatus.IN_STOCK));

        assertEquals(String.format("Cannot mark a discontinued book as %s.", AvailabilityStatus.IN_STOCK.getDisplayName().toLowerCase()), thrown.getMessage());
        verify(bookRepository).findOneByBookId(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void deleteBook() {
        UUID bookId = UUID.fromString("c382e7d3-8fb6-4f6d-9255-3a44b1e99409");
        Book book = books().get(0);
        when(bookRepository.findOneByBookId(bookId))
                .thenReturn(Optional.of(book));

        bookServiceImpl.deleteBook(bookId);

        verify(bookRepository).findOneByBookId(bookId);
        verify(bookRepository).save(book);
        verifyNoMoreInteractions(bookRepository);
    }

    private List<Book> books() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setBookId(UUID.fromString("c382e7d3-8fb6-4f6d-9255-3a44b1e99409"));
        book1.setBookYear("2000");
        book1.setDeleted(false);
        book1.setGenre(BookGenre.MYSTERY.getDisplayName());
        book1.setAuthor("Sam Ala");
        book1.setTitle("Hello World");
        book1.setSynopsis("Sample synopsis");
        book1.setContent("Sample content");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setBookId(UUID.randomUUID());
        book2.setBookYear("2002");
        book2.setDeleted(false);
        book2.setGenre(BookGenre.ROMANCE.getDisplayName());
        book2.setAuthor("Gary Sam");
        book2.setTitle("Wonder Man");
        book2.setSynopsis("Sample synopsis2");
        book2.setContent("Sample content2");

        return List.of(book1, book2);
    }


}