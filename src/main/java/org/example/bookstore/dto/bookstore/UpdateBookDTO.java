package org.example.bookstore.dto.bookstore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-25(Sat)-2024
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateBookDTO {
    private String bookYear;
    private String genre;
    private String author;
    private String title;
    private String synopsis;
    private String content;
}
