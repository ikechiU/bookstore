package org.example.bookstore.model.bookstore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.bookstore.enums.AvailabilityStatus;
import org.example.bookstore.model.BaseModel;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-25(Sat)-2024
 */

@Entity
@Table(name = "book")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
@NoArgsConstructor
@Where(clause = "is_deleted = false")
public class Book extends BaseModel implements Serializable {
    @Column(nullable = false, unique = true)
    private UUID bookId;
    private String bookYear;
    @Column(nullable = false)
    private String genre;
    private String author;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String synopsis;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;
}
