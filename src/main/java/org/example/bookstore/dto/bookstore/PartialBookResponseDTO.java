package org.example.bookstore.dto.bookstore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookstore.enums.AvailabilityStatus;

import java.util.UUID;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-25(Sat)-2024
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartialBookResponseDTO {
    private UUID bookId;
    private String bookYear;
    private String genre;
    private String author;
    private String title;
    private String synopsis;
    private AvailabilityStatus availabilityStatus;
}
