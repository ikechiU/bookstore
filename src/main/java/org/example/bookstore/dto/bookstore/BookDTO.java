package org.example.bookstore.dto.bookstore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.bookstore.enums.AvailabilityStatus;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-25(Sat)-2024
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookDTO {
    @NotBlank(message = "Book year is mandatory")
    private String bookYear;
    @NotBlank(message = "Book genre is mandatory")
    private String genre;
    @NotBlank(message = "Book author is mandatory")
    private String author;
    @NotBlank(message = "Book title is mandatory")
    private String title;
    @NotBlank(message = "Book synopsis is mandatory")
    private String synopsis;
    @NotBlank(message = "Book content is mandatory")
    private String content;
    @NotNull(message = "Availability status is mandatory")
    private AvailabilityStatus availabilityStatus;
}
